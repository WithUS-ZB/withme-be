package com.withus.withmebe.search.service;

import static util.objectprovider.GatheringDocumentProvider.getStubbedGatheringDocument;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.withus.withmebe.search.document.GatheringDocument;
import com.withus.withmebe.search.repository.GatheringDocumentRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class GatheringDocumentServiceTest {

  @Mock
  private GatheringDocumentRepository gatheringDocumentRepository;

  @InjectMocks
  private GatheringDocumentService gatheringDocumentService;

  @Test
  void successToSearchGatheringDocumentsByTitleAndStatus() {
    //given
    Pageable pageable = PageRequest.of(0, 10);
    GatheringDocument gatheringDocument1 = getStubbedGatheringDocument(1L);
    GatheringDocument gatheringDocument2 = getStubbedGatheringDocument(2L);

    given(gatheringDocumentRepository.searchByTitleAndStatusEqualsAndDeletedDttmIsNull(anyString(),
        anyString(), any()))
        .willReturn(new PageImpl<GatheringDocument>(List.of(gatheringDocument1, gatheringDocument2),
            pageable, 2));

    //when
    Page<GatheringDocument> gatheringDocuments = gatheringDocumentService.searchGatheringDocumentsByTitleAndStatus(
        "query", "status", pageable);
    //then
    assertEquals(2, gatheringDocuments.getTotalElements());
    assertEquals(1, gatheringDocuments.getTotalPages());
    assertEquals(0, gatheringDocuments.getNumber());

    GatheringDocument searchResult1 = gatheringDocuments.getContent().get(0);
    assertEquals(gatheringDocument1.id(), searchResult1.id());
    assertEquals(gatheringDocument1.nickName(), searchResult1.nickName());
    assertEquals(gatheringDocument1.title(), searchResult1.title());
    assertEquals(gatheringDocument1.gatheringType(), searchResult1.gatheringType());
    assertEquals(gatheringDocument1.maximumParticipant(), searchResult1.maximumParticipant());
    assertEquals(gatheringDocument1.startDttm(), searchResult1.startDttm());
    assertEquals(gatheringDocument1.endDttm(), searchResult1.endDttm());
    assertEquals(gatheringDocument1.applicationDeadLine(), searchResult1.applicationDeadLine());
    assertEquals(gatheringDocument1.address(), searchResult1.address());
    assertEquals(gatheringDocument1.detailedAddress(), searchResult1.detailedAddress());
    assertEquals(gatheringDocument1.mainImg(), searchResult1.mainImg());
    assertEquals(gatheringDocument1.participantsType(), searchResult1.participantsType());
    assertEquals(gatheringDocument1.fee(), searchResult1.fee());
    assertEquals(gatheringDocument1.participantSelectionMethod(), searchResult1.participantSelectionMethod());
    assertEquals(gatheringDocument1.likeCount(), searchResult1.likeCount());
    assertEquals(gatheringDocument1.createdDttm(), searchResult1.createdDttm());
    assertEquals(gatheringDocument1.status(), searchResult1.status());

    GatheringDocument searchResult2 = gatheringDocuments.getContent().get(1);
    assertEquals(gatheringDocument2.id(), searchResult2.id());
    assertEquals(gatheringDocument2.nickName(), searchResult2.nickName());
    assertEquals(gatheringDocument2.title(), searchResult2.title());
    assertEquals(gatheringDocument2.gatheringType(), searchResult2.gatheringType());
    assertEquals(gatheringDocument2.maximumParticipant(), searchResult2.maximumParticipant());
    assertEquals(gatheringDocument2.startDttm(), searchResult2.startDttm());
    assertEquals(gatheringDocument2.endDttm(), searchResult2.endDttm());
    assertEquals(gatheringDocument2.applicationDeadLine(), searchResult2.applicationDeadLine());
    assertEquals(gatheringDocument2.address(), searchResult2.address());
    assertEquals(gatheringDocument2.detailedAddress(), searchResult2.detailedAddress());
    assertEquals(gatheringDocument2.mainImg(), searchResult2.mainImg());
    assertEquals(gatheringDocument2.participantsType(), searchResult2.participantsType());
    assertEquals(gatheringDocument2.fee(), searchResult2.fee());
    assertEquals(gatheringDocument2.participantSelectionMethod(), searchResult2.participantSelectionMethod());
    assertEquals(gatheringDocument2.likeCount(), searchResult2.likeCount());
    assertEquals(gatheringDocument2.createdDttm(), searchResult2.createdDttm());
    assertEquals(gatheringDocument2.status(), searchResult2.status());
  }

}