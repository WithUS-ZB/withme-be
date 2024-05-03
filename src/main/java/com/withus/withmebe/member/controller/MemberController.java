package com.withus.withmebe.member.controller;

import com.withus.withmebe.member.dto.MemberDetailDto;
import com.withus.withmebe.member.dto.MemberSimpleDetailDto;
import com.withus.withmebe.member.dto.UpdateMemberNickNameDto;
import com.withus.withmebe.member.dto.UpdateMemberProfileImgDto;
import com.withus.withmebe.member.service.MemberService;
import com.withus.withmebe.security.anotation.RoleMember;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RoleMember
@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/simple_detail/{userId}")
  public ResponseEntity<MemberSimpleDetailDto> get(@PathVariable Long userId) {
    return ResponseEntity.ok(memberService.read(userId));
  }

  @GetMapping("/detail")
  public ResponseEntity<MemberDetailDto> getCurrentLoginMemberDetail() {
    return ResponseEntity.ok(memberService.readCurretLoginMemberDetail());
  }

  @GetMapping("/check/email")
  public ResponseEntity<Boolean> existsEmail(@RequestParam String email) {
    return ResponseEntity.ok(memberService.existsEmail(email));
  }

  @GetMapping("/check/nickname")
  public ResponseEntity<Boolean> existsNickname(@RequestParam String nickname) {
    return ResponseEntity.ok(memberService.existsNickname(nickname));
  }

  @PutMapping("/profile_img")
  public ResponseEntity<UpdateMemberProfileImgDto.Response> setProfileImg(
      @Valid @RequestBody UpdateMemberProfileImgDto.Request request) {
    return ResponseEntity.ok(memberService.updateProfileImg(request));
  }

  @PutMapping("/nickname")
  public ResponseEntity<UpdateMemberNickNameDto.Response> setNickname(
      @Valid @RequestBody UpdateMemberNickNameDto.Request request) {
    return ResponseEntity.ok(memberService.updateNickname(request));
  }
}
