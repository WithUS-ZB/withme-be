package com.withus.withmebe.festival.service;

import static com.withus.withmebe.common.exception.ExceptionCode.FAIL_TO_REQUEST_OPEN_API;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.festival.dto.FestivalSimpleInfo;
import com.withus.withmebe.festival.entity.Festival;
import com.withus.withmebe.festival.repository.FestivalRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class FestivalService {

  private final FestivalRepository festivalRepository;

  @Value("${festival.api.url}")
  private String apiUrl;

  @Transactional(readOnly = true)
  public Page<FestivalSimpleInfo> readFestivals(Pageable pageable) {
    Page<Festival> festivals = festivalRepository.findFestivalByStartDttmBeforeAndEndDttmAfter(
        LocalDateTime.now(), LocalDateTime.now(), pageable);
    return festivals.map(Festival::toFestivalSimpleInfo);
  }

  @Transactional
  @Scheduled(cron = "0 0 4 1/1 * ?")
  public void createFestivals() {

    long savedFestivalCount = festivalRepository.count();
    int totalCount = getFestivalCountFromOpenApi();

    if (savedFestivalCount >= totalCount) {
      return;
    }

    long toLoadDataCount = totalCount - savedFestivalCount;
    long startIndex = 1;

    while (toLoadDataCount > startIndex) {
      long endIndex = Math.min(startIndex + 999, toLoadDataCount);

      JsonArray jsonArray = readFestivalsFromOpenApi(startIndex, endIndex);
      List<Festival> festivals = new ArrayList<>(jsonArray.size());

      for (JsonElement jsonElement : jsonArray) {
        festivals.add(Festival.builder()
            .title(jsonElement.getAsJsonObject().get("TITLE").getAsString())
            .img(jsonElement.getAsJsonObject().get("MAIN_IMG").getAsString())
            .link(jsonElement.getAsJsonObject().get("ORG_LINK").getAsString())
            .startDttm(
                LocalDateTime.parse(jsonElement.getAsJsonObject().get("STRTDATE").getAsString(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")))
            .endDttm(
                LocalDateTime.parse(jsonElement.getAsJsonObject().get("END_DATE").getAsString(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S")).plusDays(1))
            .build());
      }
      festivalRepository.saveAll(festivals);
      startIndex = endIndex + 1;
    }
  }

  private int getFestivalCountFromOpenApi() {
    String url = apiUrl + "/1/1";

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

    if (responseEntity.getStatusCode() != HttpStatusCode.valueOf(200)) {
      throw new CustomException(FAIL_TO_REQUEST_OPEN_API);
    }

    return JsonParser.parseString(responseEntity.getBody())
        .getAsJsonObject().get("culturalEventInfo")
        .getAsJsonObject().get("list_total_count")
        .getAsInt();
  }

  private JsonArray readFestivalsFromOpenApi(long startIndex, long endIndex) {
    String url = apiUrl + "/" + startIndex + "/" + endIndex;

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

    if (responseEntity.getStatusCode() != HttpStatusCode.valueOf(200)) {
      throw new CustomException(FAIL_TO_REQUEST_OPEN_API);
    }
    return JsonParser.parseString(responseEntity.getBody())
        .getAsJsonObject().get("culturalEventInfo")
        .getAsJsonObject().get("row")
        .getAsJsonArray();
  }
}
