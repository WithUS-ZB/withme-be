package util.StubbedObjectProvider;

import com.withus.withmebe.member.entity.Member;
import java.time.LocalDate;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberProvider {
  public static Member getStubbedMember(long memberId) {
    return getStubbedMemberWithBirthDate(memberId, LocalDate.now().minusYears(20));
  }

  public  static Member getStubbedMemberWithBirthDate(long memberId, LocalDate birthDate) {
    Member member = Member.builder()
        .nickName("홍길동" + memberId)
        .birthDate(birthDate)
        .build();
    ReflectionTestUtils.setField(member, "id", memberId);
    return member;
  }
}
