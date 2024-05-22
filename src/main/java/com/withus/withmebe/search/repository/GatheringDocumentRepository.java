package com.withus.withmebe.search.repository;

import com.withus.withmebe.search.document.GatheringDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatheringDocumentRepository extends
    ElasticsearchRepository<GatheringDocument, String> {

}
