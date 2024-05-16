package com.withus.withmebe.member.dto.member;

import com.withus.withmebe.member.type.Gender;
import com.withus.withmebe.member.type.Membership;
import com.withus.withmebe.member.type.SignupPath;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MemberDetailDto(
    Long memberId,
    String email,
    String nickName,
    LocalDate birthDate,
    Gender gender,
    String phoneNumber,
    String profileImg,
    SignupPath signupPath,
    LocalDateTime signupDttm,
    Membership membership

) {
}
