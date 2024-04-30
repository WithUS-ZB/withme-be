package com.withus.withmebe.member.controller;

import com.withus.withmebe.member.dto.MemberDetailDto;
import com.withus.withmebe.member.dto.MemberSimpleDetailDto;
import com.withus.withmebe.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;

  @GetMapping("/detail/{userId}")
  public ResponseEntity<MemberSimpleDetailDto> get(@PathVariable Long userId){
    return ResponseEntity.ok(memberService.read(userId));
  }
  @GetMapping("/detail")
  public ResponseEntity<MemberDetailDto> getCurrentLoginMemberDetail(){
    return ResponseEntity.ok(memberService.readCurretLoginMemberDetail());
  }
}
