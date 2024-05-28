package com.withus.withmebe.search.service;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import co.elastic.clients.json.JsonData;
import com.withus.withmebe.gathering.Type.Status;
import com.withus.withmebe.search.document.GatheringDocument;
import com.withus.withmebe.search.dto.GatheringSearchResponse;
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
  private static final Query ALL_MATCH_QUERY = QueryBuilders.matchAll().build()._toQuery();
  private static final String TARGET_INDEX_NAME = "gathering";

  public Page<GatheringSearchResponse> searchGatheringDocumentsByTitle(SearchOption range,
      String title, Pageable pageable, SearchOption option) {

    SearchHits<GatheringDocument> searchHits = elasticsearchOperations.search(
        getSearchQuery(range, title, pageable, option),
        GatheringDocument.class, IndexCoordinates.of(TARGET_INDEX_NAME));
    List<GatheringSearchResponse> gatheringSearchResponses = StreamUtils.createStreamFromIterator(
            searchHits.iterator())
        .map(hit -> hit.getContent().toGatheringSearchResponse())
        .collect(Collectors.toList());

    return new PageImpl<GatheringSearchResponse>(gatheringSearchResponses, pageable,
        searchHits.getTotalHits());
  }

  private NativeQuery getSearchQuery(SearchOption range, String title, Pageable pageable,
      SearchOption option) {
    return new NativeQueryBuilder()
        .withQuery(getBoolQuery(range, title, option))
        .withPageable(pageable)
        .build();
  }

  private Query getBoolQuery(SearchOption range, String title, SearchOption option) {
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

  private Query getOptionQuery(SearchOption option) {
    if (option.equals(SearchOption.ALL)) {
      return ALL_MATCH_QUERY;
    } else if (option.equals(SearchOption.PAY_HAS)) {
      return QueryBuilders.range().field(option.getField()).gt(JsonData.fromJson(option.getValue()))
          .build()._toQuery();
    } else {
      return getMatchQuery(option.getField(), option.getValue());
    }
  }
}
