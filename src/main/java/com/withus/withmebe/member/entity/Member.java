package com.withus.withmebe.member.entity;

import static com.withus.withmebe.member.type.Membership.FREE;
import static com.withus.withmebe.member.type.Membership.PREMIUM;
import static com.withus.withmebe.member.type.Role.ROLE_MEMBER;
import static com.withus.withmebe.member.type.SignupPath.NORMAL;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.withus.withmebe.common.entity.BaseEntity;
import com.withus.withmebe.member.dto.member.MemberDetailDto;
import com.withus.withmebe.member.dto.member.MemberInfoDto;
import com.withus.withmebe.member.dto.member.request.AdditionalInfoRequestDto;
import com.withus.withmebe.member.type.Gender;
import com.withus.withmebe.member.type.Membership;
import com.withus.withmebe.member.type.Role;
import com.withus.withmebe.member.type.SignupPath;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {
  @Transient
  private static final int MINOR_AGE_LIMIT = 19;

  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(unique = true, nullable = false)
  private String email;

  private String password;

  @Setter
  @Column(unique = true, nullable = false)
  private String nickName;

  private LocalDate birthDate;

  @Enumerated(EnumType.STRING)
  private Gender gender;

  @Setter
  private String phoneNumber;

  @Setter
  private String profileImg;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role = ROLE_MEMBER;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Setter
  private SignupPath signupPath = NORMAL;

  @Column(nullable = false)
  private LocalDateTime signupDttm;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Setter
  private Membership membership = FREE;


  @Builder
  public Member(String email, String password, String nickName, LocalDate birthDate, Gender gender,
      LocalDateTime signupDttm) {
    this.email = email;
    this.password = password;
    this.nickName = nickName;
    this.birthDate = birthDate;
    this.gender = gender;
    this.signupDttm = signupDttm;
  }

  public boolean isPremiumMember() {
    return membership == PREMIUM;
  }

  public int getAge() {
    return Period.between(this.birthDate, LocalDate.now()).getYears();
  }

  public boolean isMinor() { // 미성년자
    return getAge() < MINOR_AGE_LIMIT;
  }

  public boolean isAdult() { // 성인
    return !isMinor();
  }

  public Member updateAdditionalInfo(AdditionalInfoRequestDto request) {
    this.gender = request.gender();
    this.birthDate = request.birthDate();
    return this;
  }

  public MemberDetailDto toMemberDetailDto() {
    return new MemberDetailDto(
        this.id,
        this.email,
        this.nickName,
        this.birthDate,
        this.gender,
        this.phoneNumber,
        this.profileImg,
        this.signupPath,
        this.signupDttm,
        this.membership
    );
  }
  public MemberInfoDto toMemberInfoDto(){
    return new MemberInfoDto(
        this.nickName,
        this.profileImg,
        this.membership
    );
  }
}
