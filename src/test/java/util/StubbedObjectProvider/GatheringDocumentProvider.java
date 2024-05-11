package util.StubbedObjectProvider;

import com.withus.withmebe.search.document.GatheringDocument;
import java.time.LocalDateTime;

public class GatheringDocumentProvider {

  private GatheringDocumentProvider() {
  }

  public static GatheringDocument getStubbedGatheringDocument(long documentId) {
    return GatheringDocument.builder()
        .id(documentId)
        .nickName("nickName" + documentId)
        .title("title" + documentId)
        .gatheringType("gatheringType" + documentId)
        .maximumParticipant(10 + documentId)
        .startDttm(LocalDateTime.now())
        .endDttm(LocalDateTime.now())
        .applicationDeadLine(LocalDateTime.now())
        .address("address" + documentId)
        .detailedAddress("detailedAddress" + documentId)
        .mainImg("mainImg" + documentId)
        .participantsType("participantsType" + documentId)
        .fee(200 + documentId)
        .participantSelectionMethod("participantSelectionMethod" + documentId)
        .likeCount(100 + documentId)
        .createdDttm(LocalDateTime.now())
        .status("status" + documentId)
        .build();
  }
}
