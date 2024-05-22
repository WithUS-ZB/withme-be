package util.objectprovider;

import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.type.Status;
import java.time.LocalDateTime;
import org.springframework.test.util.ReflectionTestUtils;

public class ParticipationProvider {

  private ParticipationProvider() {
  }

  public static Participation getStubbedParticipation(long participationId, Member participant,
      Gathering gathering) {
    return getStubbedParticipationByStatus(participationId, participant, gathering, Status.CREATED);
  }

  public static Participation getStubbedParticipationByStatus(long participationId, Member participant,
      Gathering gathering, Status status) {
    Participation participation = Participation.builder()
        .gathering(gathering)
        .participant(participant)
        .status(status)
        .build();
    ReflectionTestUtils.setField(participation, "id", participationId);
    ReflectionTestUtils.setField(participation, "createdDttm", LocalDateTime.now());
    ReflectionTestUtils.setField(participation, "updatedDttm", LocalDateTime.now());
    return participation;
  }
}
