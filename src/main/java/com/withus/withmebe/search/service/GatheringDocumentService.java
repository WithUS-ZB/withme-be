package com.withus.withmebe.search.service;

import com.withus.withmebe.search.document.GatheringDocument;
import com.withus.withmebe.search.repository.GatheringDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GatheringDocumentService {

  private final GatheringDocumentRepository gatheringDocumentRepository;

  public Page<GatheringDocument> readGatheringSearches(String query,
      String status, Pageable pageable) {
    return gatheringDocumentRepository.searchByTitleAndStatusEqualsAndDeletedDttmIsNull(query,
        status, pageable);
  }
}
