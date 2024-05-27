package com.withus.withmebe.common.exception;


import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {
  // BAD_REQUEST: 400
  PASSWORD_CHK_MISMATCH(BAD_REQUEST, "비밀번호 확인이 비밀번호와 일치하지 않습니다."),
  AUTH_CODE_MISMATCH(BAD_REQUEST, "인증번호가 일치하지 않습니다."),
  INVALID_TIME(BAD_REQUEST, "가능한 시간이 아닙니다."),
  INVALID_PATH_FORMAT(BAD_REQUEST, "유효하지 않은 경로입니다."),

  // Unauthorized: 401
  TOKEN_EXPIRED(UNAUTHORIZED, "토큰이 만료되었습니다."),
  AUTHENTICATION_ISSUE(UNAUTHORIZED, "인증 이슈가 있습니다."),
  PASSWORD_MISMATCH(UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),

  // FORBIDDEN: 403
  AUTHORIZATION_ISSUE(FORBIDDEN, "권한이 없는 사용자입니다."),

  // NOT_FOUND: 404
  ENTITY_NOT_FOUND(NOT_FOUND, "개체를 찾지 못했습니다."),
  STOMP_HEADER_ACCESSOR_NOT_FOUND_EXCEPTION(NOT_FOUND, "메시지에서 STOMP 헤더 접근자를 가져오지 못했습니다."),
  AUTH_SMS_CODE_NOT_FOUND(NOT_FOUND, "인증번호를 발급해주세요."),

  // Conflict: 409
  EMAIL_CONFLICT(CONFLICT, "이메일이 중복됩니다."),
  NICKNAME_CONFLICT(CONFLICT, "닉네임이 중복됩니다."),
  PARTICIPATION_DUPLICATED(CONFLICT, "이미 참여 신청을 하였습니다."),
  PARTICIPATION_CONFLICT(CONFLICT, "참여 상태 변경이 불가능합니다."),
  GATHERING_STATUS_CONFLICT(CONFLICT, "참여 불가능한 상태의 모임입니다."),
  MEMBERSHIP_CONFLICT(CONFLICT, "이미 프리미엄 회원입니다."),
  PAYMENT_CONFLICT(CONFLICT, "결제 상태 변경이 불가능합니다."),
  PARTICIPANTSTYPE_CONFLICT(CONFLICT, "참여 조건에 맞지 않습니다."),
  REACHED_AT_MAXIMUM_PARTICIPANT(CONFLICT, "최대 참여 인원에 도달했습니다."),
  NOT_PARTICIPATION_PERIOD(CONFLICT, "참여 신청 기간이 아닙니다."),
  SUBSCRIPTION_DUPLICATED(CONFLICT, "이미 구독중입니다."),
  REACHED_AT_MAXIMUM_PARTICIPATION(CONFLICT, "참여 신청 가능한 모임 최대 한도에 도달했습니다."),

  // Internal Server Error
  FAIL_TO_REQUEST_OPEN_API(INTERNAL_SERVER_ERROR, "오픈 API 정보 요청에 실패했습니다."),
  FAIL_TO_SEND_NOTIFICATION(INTERNAL_SERVER_ERROR, "알림 전송에 실패했습니다."),
  FAIL_TO_REQUEST_APPROVE_PAYMENT(INTERNAL_SERVER_ERROR, "결제 승인 요청에 실패했습니다.")
  ;

  private final HttpStatus status;
  private final String message;
}
