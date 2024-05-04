package com.withus.withmebe.member.entity;

import static com.withus.withmebe.member.type.Membership.FREE;
import static com.withus.withmebe.member.type.Role.ROLE_MEMBER;
import static com.withus.withmebe.member.type.SignupPath.NORMAL;
import static jakarta.persistence.GenerationType.IDENTITY;

import com.withus.withmebe.common.entity.BaseEntity;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class Member extends BaseEntity {
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
  private SignupPath signupPath = NORMAL;

  @Column(nullable = false)
  private LocalDateTime signupDttm;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Membership membership = FREE;

  @Builder
  public Member(String email, String password, String nickName, LocalDate birthDate, Gender gender, LocalDateTime signupDttm){
    this.email = email;
    this.password = password;
    this.nickName = nickName;
    this.birthDate = birthDate;
    this.gender = gender;
    this.signupDttm = signupDttm;
  }
}
