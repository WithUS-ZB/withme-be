package com.withus.withmebe.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.json.JsonData;
import com.withus.withmebe.gathering.Type.Status;
import com.withus.withmebe.search.document.GatheringDocument;
import com.withus.withmebe.search.dto.GatheringSearchResponse;
import com.withus.withmebe.search.type.Option;
import com.withus.withmebe.search.type.SearchRange;
import com.withus.withmebe.search.type.SearchOption;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.util.StreamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GatheringDocumentService {

  private final ElasticsearchOperations elasticsearchOperations;
  private static final Query ALL_MATCH_QUERY = QueryBuilders.matchAll().build()._toQuery();

  public Page<GatheringSearchResponse> searchGatheringDocumentsByTitle(SearchRange range,
      String title, Pageable pageable, SearchOption option) {
    title = decodeURL(title);

    SearchHits<GatheringDocument> searchHits = elasticsearchOperations.search(
        getSearchQuery(range, title, pageable, option),
        GatheringDocument.class, IndexCoordinates.of("gathering"));
    List<GatheringSearchResponse> gatheringSearchResponses = StreamUtils.createStreamFromIterator(
            searchHits.iterator())
        .map(hit -> hit.getContent().toGatheringSearchResponse())
        .collect(Collectors.toList());

    return new PageImpl<GatheringSearchResponse>(gatheringSearchResponses, pageable,
        searchHits.getTotalHits());
  }

  private NativeQuery getSearchQuery(SearchRange range, String title, Pageable pageable,
      SearchOption option) {
    return new NativeQueryBuilder()
        .withQuery(getBoolQuery(range, title, option))
        .withPageable(pageable)
        .build();
  }

  private Query getBoolQuery(SearchRange range, String title, SearchOption option) {
    return QueryBuilders.bool()
        .must(
            getTitleQuery(title),
            getMatchQuery("status", Status.PROGRESS.toString()),
            getOptionQuery(range),
            getOptionQuery(option)
        )
        .mustNot(getExistsQuery("deleted_dttm"))
        .build()._toQuery();
  }

  private Query getTitleQuery(String title) {
    if (title == null || title.isEmpty()) {
      return ALL_MATCH_QUERY;
    }
    return getMultiMatchQuery(title, List.of("title", "ngram_title"));
  }

  private Query getExistsQuery(String fieldName) {
    return QueryBuilders.exists().field(fieldName).build()._toQuery();
  }

  private Query getMatchQuery(String fieldName, String query) {
    return QueryBuilders.match().field(fieldName).query(query).build()._toQuery();
  }

  private Query getMultiMatchQuery(String query, List<String> fields) {
    return QueryBuilders.multiMatch().query(query).fields(fields).type(TextQueryType.CrossFields).build()._toQuery();
  }

  private Query getOptionQuery(Option option) {
    if (option.getName().equals("ALL")) {
      return ALL_MATCH_QUERY;
    } else if (option.getName().equals("PAY_HAS")) {
      return QueryBuilders.range().field(option.getField()).gt(JsonData.fromJson(option.getValue()))
          .build()._toQuery();
    } else {
      return getMatchQuery(option.getField(), option.getValue());
    }
  }

  private String decodeURL(String encodedURL) {
    return URLDecoder.decode(encodedURL, StandardCharsets.UTF_8);
  }
}
