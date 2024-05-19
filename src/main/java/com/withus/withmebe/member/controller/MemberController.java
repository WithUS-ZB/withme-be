package com.withus.withmebe.member.controller;

import com.withus.withmebe.member.dto.member.MemberDetailDto;
import com.withus.withmebe.member.dto.member.MemberInfoDto;
import com.withus.withmebe.member.dto.member.UpdateMemberNickNameDto;
import com.withus.withmebe.member.dto.member.UpdateMemberProfileImgDto;
import com.withus.withmebe.member.dto.member.request.AdditionalInfoRequestDto;
import com.withus.withmebe.member.service.MemberService;
import com.withus.withmebe.security.anotation.CurrentMemberId;
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

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  @GetMapping("/info/{userId}")
  public ResponseEntity<MemberInfoDto> get(@PathVariable Long userId) {
    return ResponseEntity.ok(memberService.read(userId));
  }

  @GetMapping("/detail")
  public ResponseEntity<MemberDetailDto> getCurrentLoginMemberDetail(
      @CurrentMemberId Long currentMemberId) {
    return ResponseEntity.ok(memberService.readCurrentLoginMemberDetail(currentMemberId));
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
      @Valid UpdateMemberProfileImgDto.Request request,
      @CurrentMemberId Long currentMemberId) {
    return ResponseEntity.ok(memberService.updateProfileImg(request, currentMemberId));
  }

  @PutMapping("/nickname")
  public ResponseEntity<UpdateMemberNickNameDto.Response> setNickname(
      @Valid @RequestBody UpdateMemberNickNameDto.Request request,
      @CurrentMemberId Long currentMemberId) {
    return ResponseEntity.ok(memberService.updateNickname(request, currentMemberId));
  }

  @PutMapping("/additionalInfo")
  public ResponseEntity<MemberDetailDto> setAdditionalInfo(
      @Valid @RequestBody AdditionalInfoRequestDto request, @CurrentMemberId Long currentMemberId) {
    return ResponseEntity.ok(memberService.updateAdditionalInfo(request, currentMemberId));
  }
}
