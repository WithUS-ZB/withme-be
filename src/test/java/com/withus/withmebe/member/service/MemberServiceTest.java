package com.withus.withmebe.member.service;

import static com.withus.withmebe.member.type.Gender.FEMALE;
import static com.withus.withmebe.member.type.Gender.MALE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.member.dto.member.MemberDetailDto;
import com.withus.withmebe.member.dto.member.MemberInfoDto;
import com.withus.withmebe.member.dto.member.UpdateMemberNickNameDto;
import com.withus.withmebe.member.dto.member.UpdateMemberProfileImgDto;
import com.withus.withmebe.member.dto.member.UpdateMemberProfileImgDto.Request;
import com.withus.withmebe.member.dto.member.request.AdditionalInfoRequestDto;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.s3.dto.S3Dto;
import com.withus.withmebe.s3.service.S3Service;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import util.security.WithMockCustomUser;

@ExtendWith(MockitoExtension.class)
@WithMockCustomUser
class MemberServiceTest {

  @Mock
  private MemberRepository memberRepository;
  @Mock
  private S3Service s3Service;

  @InjectMocks
  private MemberService memberService;
  private Member createEntity() {
    return Member.builder()
        .email("aaa@gmail.com")
        .nickName("홍길동")
        .birthDate(LocalDate.of(2020,1,1))
        .gender(MALE)
        .signupDttm(LocalDateTime.now())
        .build();
  }
  @Test
  void read() {
    // given
    Long memberId = 1L;
    Member member = createEntity();
    when(memberRepository.findById(memberId)).thenReturn(java.util.Optional.of(member));

    // when
    MemberInfoDto result = memberService.read(memberId);

    // then
    assertNotNull(result);
    assertEquals("홍길동", result.nickName());
  }

  @Test
  void readCurrentLoginMemberDetail() {
    // given
    Long currentMemberId = 1L;
    Member member = createEntity();
    when(memberRepository.findById(currentMemberId)).thenReturn(java.util.Optional.of(member));

    // when
    MemberDetailDto result = memberService.readCurrentLoginMemberDetail(currentMemberId);

    // then
    assertNotNull(result);
    assertEquals("홍길동", result.nickName());
  }

  @Test
  void existsEmail() {
    // given
    String email = "test@example.com";
    when(memberRepository.existsByEmail(email)).thenReturn(true);

    // when
    boolean result = memberService.existsEmail(email);

    // then
    assertTrue(result);
  }

  @Test
  void existsNickname() {
    // given
    String nickname = "test";
    when(memberRepository.existsByNickName(nickname)).thenReturn(true);

    // when
    boolean result = memberService.existsNickname(nickname);

    // then
    assertTrue(result);
  }

  @Test
  void updateProfileImg() throws IOException {
    // given
    Long currentMemberId = 1L;
    Member member = createEntity();
    MockMultipartFile mockMultipartFile = new MockMultipartFile("request", "보라색배경화면.jpeg", "jpeg",
        new FileInputStream("src/test/resources/img/보라색배경화면.jpeg"));
    when(memberRepository.findById(currentMemberId)).thenReturn(Optional.of(member));
    when(s3Service.uploadFile(mockMultipartFile)).thenReturn(new S3Dto("url"));
    // when
    UpdateMemberProfileImgDto.Response result = memberService.updateProfileImg(
        new Request(mockMultipartFile),
        currentMemberId);

    // then
    assertNotNull(result);
    assertEquals("홍길동", result.nickName());
  }

  @Test
  void updateNickname() {
    // given
    Long currentMemberId = 1L;
    UpdateMemberNickNameDto.Request request = new UpdateMemberNickNameDto.Request("newNickname");
    Member member = createEntity();
    when(memberRepository.existsByNickName(request.nickName())).thenReturn(false);
    when(memberRepository.findById(currentMemberId)).thenReturn(java.util.Optional.of(member));

    // when
    UpdateMemberNickNameDto.Response result = memberService.updateNickname(request,
        currentMemberId);

    // then
    assertNotNull(result);
    assertEquals("newNickname", result.nickName());
  }

  @Test
  void updateNickname_NicknameConflict() {
    // given
    Long currentMemberId = 1L;
    UpdateMemberNickNameDto.Request request = new UpdateMemberNickNameDto.Request("existingNickname");
    when(memberRepository.existsByNickName(request.nickName())).thenReturn(true);

    // then
    assertThrows(CustomException.class, () -> {
      // when
      memberService.updateNickname(request, currentMemberId);
    });
  }

  @Test
  void updateAdditionalInfo() {
    // given
    Long currentMemberId = 1L;
    AdditionalInfoRequestDto request = new AdditionalInfoRequestDto(LocalDate.of(1997,4,7), FEMALE);
    Member member = createEntity();
    when(memberRepository.findById(currentMemberId)).thenReturn(java.util.Optional.of(member));

    // when
    MemberDetailDto result = memberService.updateAdditionalInfo(request, currentMemberId);

    // then
    assertNotNull(result);
    assertEquals(LocalDate.of(1997,4,7), result.birthDate());
    assertEquals(FEMALE, result.gender());
  }
}