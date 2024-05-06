package com.withus.withmebe.search.service;

import com.withus.withmebe.search.document.GatheringSearch;
import com.withus.withmebe.search.repository.GatheringSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GatheringSearchService {

  private final GatheringSearchRepository gatheringSearchRepository;

  public Page<GatheringSearch> readGatheringSearches(String query, Pageable pageable) {
    return gatheringSearchRepository.findGatheringSearchesByTitle(query, pageable);
  }
}
