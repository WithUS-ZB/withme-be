package com.withus.withmebe.search.repository;

import com.withus.withmebe.search.document.GatheringSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GatheringSearchRepository extends ElasticsearchRepository<GatheringSearch, String> {

  Page<GatheringSearch> findGatheringSearchesByTitle(String title, Pageable pageable);
}
