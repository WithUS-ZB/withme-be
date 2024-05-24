package util.objectprovider;

import static util.objectprovider.MemberProvider.getStubbedMember;

import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.ParticipantSelectionMethod;
import com.withus.withmebe.gathering.Type.ParticipantsType;
import com.withus.withmebe.gathering.Type.Status;
import com.withus.withmebe.gathering.entity.Gathering;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.test.util.ReflectionTestUtils;

public class GatheringProvider {

  private GatheringProvider() {
  }

  /**
   * 입력된 gatheringId와 hostId를 이용해 모든 필드의 내용이 채워진 gathering객체를 만들어 반환합니다. Enum 타입은 gatheringId의 홀짝에
   * 따라 값이 바뀝니다. 짝수 : GatheringType.EVENT, ParticipantsType.MINOR,
   * ParticipantSelectionMethod.FIRST_COME, Status.CANCELED 홀수 : GatheringType.MEETING,
   * ParticipantsType.ADULT, ParticipantSelectionMethod.UNLIMITED_APPLICATION, Status.PROGRESS
   */
  public static Gathering getStubbedGathering(long gatheringId, long hostId) {
    return getStubbedGatheringByPeriod(gatheringId, hostId, LocalDate.now().minusDays(1),
        LocalDate.now().plusDays(1));
  }

  public static Gathering getStubbedGatheringPeriodAlwaysFalse(long gatheringId, long hostId) {
    return getStubbedGatheringByPeriod(gatheringId, hostId, LocalDate.now().plusDays(1), LocalDate.now().minusDays(1));
  }

  public static Gathering getStubbedGatheringByPeriod(
      long gatheringId, long hostId,
      LocalDate recruitmentStartDt, LocalDate recruitmentEndDt) {
    return getStubbedGatheringByPeriodAndGatheringDateTime(
        gatheringId, hostId, recruitmentStartDt, recruitmentEndDt
        , recruitmentEndDt.plusDays(1).atTime(LocalTime.of(13,0)));
  }

  public static Gathering getStubbedGatheringByPeriodAndGatheringDateTime(
      long gatheringId, long hostId,
      LocalDate recruitmentStartDt, LocalDate recruitmentEndDt, LocalDateTime gatheringDateTime) {
    return getStubbedGatheringByPeriodAndGatheringDateTimeAndStatus(
        gatheringId, hostId, recruitmentStartDt, recruitmentEndDt, gatheringDateTime
        ,  (gatheringId % 2 == 0) ? Status.CANCELED : Status.PROGRESS);
  }

  public static Gathering getStubbedGatheringByPeriodAndGatheringDateTimeAndStatus(
      long gatheringId
      , long hostId,
      LocalDate recruitmentStartDt
      , LocalDate recruitmentEndDt
      , LocalDateTime gatheringDateTime
      , Status status) {
    Gathering gathering = Gathering.builder()
        .member(getStubbedMember(hostId))
        .title("모임제목" + gatheringId)
        .content("모임본문" + gatheringId)
        .gatheringType((gatheringId % 2 == 0) ? GatheringType.EVENT : GatheringType.MEETING)
        .maximumParticipant(10 + gatheringId)
        .day(gatheringDateTime.toLocalDate())
        .time(gatheringDateTime.toLocalTime())
        .recruitmentStartDt(recruitmentStartDt)
        .recruitmentEndDt(recruitmentEndDt)
        .category("모임카테고리" + gatheringId)
        .address("주소" + gatheringId)
        .detailedAddress("상세주소" + gatheringId)
        .lat(gatheringId * 10 + gatheringId + (double) gatheringId / 10)
        .lng(gatheringId * 5 + gatheringId + (double) gatheringId / 5)
        .mainImg("메인 이미지" + gatheringId)
        .fee(100 + gatheringId)
        .participantsType((gatheringId % 2 == 0) ? ParticipantsType.MINOR : ParticipantsType.ADULT)
        .participantSelectionMethod((gatheringId % 2 == 0) ? ParticipantSelectionMethod.FIRST_COME
            : ParticipantSelectionMethod.UNLIMITED_APPLICATION)
        .build();
    ReflectionTestUtils.setField(gathering, "id", gatheringId);
    ReflectionTestUtils.setField(gathering, "likeCount", 100 + gatheringId);
    ReflectionTestUtils.setField(gathering, "status", status);

    return gathering;
  }
}
