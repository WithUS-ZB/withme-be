package com.withus.withmebe.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.json.JsonData;
import com.withus.withmebe.gathering.Type.Status;
import com.withus.withmebe.search.document.GatheringDocument;
import com.withus.withmebe.search.dto.GatheringSearchResponse;
import com.withus.withmebe.search.type.Option;
import com.withus.withmebe.search.type.SearchRange;
import com.withus.withmebe.search.type.SearchOption;
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

  public Page<GatheringSearchResponse> searchGatheringDocumentsByTitle(SearchRange range,
      String title, Pageable pageable, SearchOption option) {

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
            getMatchQuery("title", title),
            getMatchQuery("status", Status.PROGRESS.toString()),
            getOptionQuery(range),
            getOptionQuery(option)
        )
        .mustNot(getExistsQuery("deleted_dttm"))
        .build()._toQuery();
  }

  private Query getExistsQuery(String fieldName) {
    return QueryBuilders.exists().field(fieldName).build()._toQuery();
  }

  private Query getMatchQuery(String fieldName, String query) {
    return QueryBuilders.match().field(fieldName).query(query).build()._toQuery();
  }

  private Query getOptionQuery(Option option) {
    if (option.getName().equals("ALL")) {
      return QueryBuilders.matchAll().build()._toQuery();
    } else if (option.getName().equals("PAY_HAS")) {
      return QueryBuilders.range().field(option.getField()).gt(JsonData.fromJson(option.getValue()))
          .build()._toQuery();
    }
    else {
      return getMatchQuery(option.getField(), option.getValue());
    }
  }
}
