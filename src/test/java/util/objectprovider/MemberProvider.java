package util.objectprovider;

import com.withus.withmebe.member.entity.Member;
import java.time.LocalDate;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberProvider {

  private MemberProvider() {
  }

  public static Member getStubbedMember(long memberId) {
    return getStubbedMemberWithBirthDate(memberId, LocalDate.now().minusYears(20));
  }

  public static Member getStubbedMinorMember(long memberId) {
    return getStubbedMemberWithBirthDate(memberId, LocalDate.now().minusYears(10));
  }



  public static Member getStubbedMemberWithBirthDate(long memberId, LocalDate birthDate) {
    Member member = Member.builder()
        .nickName("홍길동" + memberId)
        .birthDate(birthDate)
        .build();
    ReflectionTestUtils.setField(member, "id", memberId);
    ReflectionTestUtils.setField(member, "profileImg", "프로필 이미지" + memberId);
    return member;
  }
}
