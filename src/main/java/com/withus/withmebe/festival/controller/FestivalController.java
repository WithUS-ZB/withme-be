package com.withus.withmebe.festival.controller;

import com.withus.withmebe.festival.dto.FestivalSimpleInfo;
import com.withus.withmebe.festival.service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/festival")
@RequiredArgsConstructor
public class FestivalController {

  private final FestivalService festivalService;

  @GetMapping
  public ResponseEntity<Page<FestivalSimpleInfo>> getFestivals(
      @PageableDefault(size = 6) Pageable pageable) {
    return ResponseEntity.ok(festivalService.readFestivals(pageable));
  }
}
