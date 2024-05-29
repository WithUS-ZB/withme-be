## 1. 문제 상황
 - Entity에서 일부 필드 제거 후 INSERT문이 실행되지 않음

## 2. 원인
 1. ddl-auto가 update로 설정된 상태에서 NotNull 설정의 필드를 제거
 2. 스키마에는 제거된 필드가 남아 있어 INSERT시 null 체크로 인해 실행 실패

## 3. 해결 방안
 - 스키마에서 제거된 필드 Drop
