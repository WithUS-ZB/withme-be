package util.StubbedObjectProvider;

import com.withus.withmebe.member.entity.Member;
import org.springframework.test.util.ReflectionTestUtils;

public class MemberProvider {
  public static Member getStubbedMember(long memberId) {
    Member member = Member.builder()
        .nickName("홍길동" + memberId)
        .build();
    ReflectionTestUtils.setField(member, "id", memberId);
    return member;
  }
}
