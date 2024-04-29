package com.withus.withmebe.member.entity;

import static com.withus.withmebe.member.type.Membership.FREE;
import static com.withus.withmebe.member.type.SignupPath.NORMAL;
import static jakarta.persistence.GenerationType.IDENTITY;

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

@Entity
@Getter
@NoArgsConstructor(force = true)
public class Member {
  @Id
  @GeneratedValue(strategy = IDENTITY)
  @Column(name = "member_id")
  private final Long id;

  @Column(unique = true, nullable = false)
  private final String email;

  private String password;

  @Column(unique = true, nullable = false)
  private String nickName;

  private final LocalDate birthDate;

  @Enumerated(EnumType.STRING)
  private final Gender gender;

  private String phoneNumber;

  private String profileImg;

  @Enumerated(EnumType.STRING)
  private Role role;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private SignupPath signupPath = NORMAL;

  @Column(nullable = false)
  private LocalDateTime signupDttm;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Membership membership = FREE;

  @Builder
  public Member(Long memberId, String email, String password, LocalDate birthDate, Gender gender){
    this.id = memberId;
    this.email = email;
    this.password = password;
    this.birthDate = birthDate;
    this.gender = gender;
  }
}
