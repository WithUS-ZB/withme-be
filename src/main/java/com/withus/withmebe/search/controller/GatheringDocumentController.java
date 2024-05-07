package com.withus.withmebe.search.controller;

import com.withus.withmebe.search.document.GatheringDocument;
import com.withus.withmebe.search.service.GatheringDocumentService;
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
public class GatheringDocumentController {

  private final GatheringDocumentService gatheringDocumentService;

  @GetMapping("/title")
  public ResponseEntity<Page<GatheringDocument>> getGatheringSearches(
      @RequestParam(value = "query") String query,
      @RequestParam(value = "status", defaultValue = "PROGRESS", required = false) String status
      , @PageableDefault Pageable pageable) {
    return ResponseEntity.ok(
        gatheringDocumentService.readGatheringSearches(query, status, pageable));
  }
}
