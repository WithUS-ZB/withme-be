package util.objectprovider;

import com.withus.withmebe.search.document.GatheringDocument;
import java.time.LocalDateTime;

public class GatheringDocumentProvider {

  private GatheringDocumentProvider() {
  }

  public static GatheringDocument getStubbedGatheringDocument(long documentId) {
    return GatheringDocument.builder()
        .id(documentId)
        .memberId(10 + documentId)
        .nickName("홍길동" + documentId)
        .profileImg("프로필이미지" + documentId)
        .title("제목" + documentId)
        .content("본문" + documentId)
        .gatheringType("모임종류" + documentId)
        .maximumParticipant(100 + documentId)
        .day(LocalDateTime.now())
        .time(LocalDateTime.now())
        .recruitmentStartDt(LocalDateTime.now())
        .recruitmentEndDt(LocalDateTime.now())
        .category("카테고리" + documentId)
        .address("주소" + documentId)
        .mainImg("메인이미지" + documentId)
        .participantsType("참여제한" + documentId)
        .fee(1000 + documentId)
        .participantSelectionMethod("모집유형" + documentId)
        .likeCount(10000 + documentId)
        .createdDttm(LocalDateTime.now())
        .status("모임상태" + documentId)
        .build();
  }
}
