package com.withus.withmebe.search.repository;

import com.withus.withmebe.search.document.GatheringDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatheringDocumentRepository extends
    ElasticsearchRepository<GatheringDocument, String> {

  Page<GatheringDocument> searchByTitleAndStatusEqualsAndDeletedDttmIsNull(String title,
      String status, Pageable pageable);
}
