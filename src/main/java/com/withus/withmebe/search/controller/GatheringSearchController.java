package com.withus.withmebe.search.controller;

import com.withus.withmebe.search.document.GatheringSearch;
import com.withus.withmebe.search.service.GatheringSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search/gathering")
public class GatheringSearchController {

  private final GatheringSearchService gatheringSearchService;

  @GetMapping("/title")
  public ResponseEntity<Page<GatheringSearch>> getGatheringSearches(@RequestParam(value = "query") String query,
      @PageableDefault(size = 10) Pageable pageble) {
    return ResponseEntity.ok(gatheringSearchService.readGatheringSearches(query, pageble));
  }
}
