package com.withus.withmebe.search.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static util.objectprovider.GatheringDocumentProvider.getStubbedGatheringDocument;

import com.withus.withmebe.search.document.GatheringDocument;
import com.withus.withmebe.search.dto.GatheringSearchResponse;
import com.withus.withmebe.search.type.SearchOption;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchHitsImpl;
import org.springframework.data.elasticsearch.core.TotalHitsRelation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;

@ExtendWith(MockitoExtension.class)
class GatheringDocumentServiceTest {

  @Mock
  private ElasticsearchOperations elasticsearchOperations;

  @InjectMocks
  private GatheringDocumentService gatheringDocumentService;

  @Test
  void successToSearchGatheringDocumentsByTitle() {
    //given
    GatheringDocument gatheringDocument1 = getStubbedGatheringDocument(1L);
    GatheringDocument gatheringDocument2 = getStubbedGatheringDocument(2L);
    List<SearchHit<GatheringDocument>> searchHitList =
        List.of(
            new SearchHit<GatheringDocument>(null, null, null, 0.f, null, null, null, null, null,
                null, gatheringDocument1),
            new SearchHit<GatheringDocument>(null, null, null, 0.f, null, null, null, null, null,
                null, gatheringDocument2)
        );
    SearchHits<GatheringDocument> searchHits =
        new SearchHitsImpl<>(2, TotalHitsRelation.EQUAL_TO, 1.0f, null, null, searchHitList, null,
            null);

    given(elasticsearchOperations.search(any(NativeQuery.class),
        argThat(aClass -> aClass.equals(GatheringDocument.class)),
        argThat(indexCoordinates -> indexCoordinates.equals(IndexCoordinates.of("gathering")))))
        .willAnswer(invocation -> searchHits);

    //when
    Page<GatheringSearchResponse> gatheringSearchResponses = gatheringDocumentService.searchGatheringDocumentsByTitle(
        SearchOption.ALL, "제목", Pageable.ofSize(10), SearchOption.ALL);
    //then
    assertEquals(2, gatheringSearchResponses.getTotalElements());
    assertEquals(1, gatheringSearchResponses.getTotalPages());
    assertEquals(0, gatheringSearchResponses.getNumber());

    GatheringSearchResponse searchResult1 = gatheringSearchResponses.getContent().get(0);
    assertEquals(gatheringDocument1.id(), searchResult1.gatheringId());
    assertEquals(gatheringDocument1.memberId(), searchResult1.memberId());
    assertEquals(gatheringDocument1.nickName(), searchResult1.nickName());
    assertEquals(gatheringDocument1.profileImg(), searchResult1.profileImg());
    assertEquals(gatheringDocument1.title(), searchResult1.title());
    assertEquals(gatheringDocument1.content(), searchResult1.content());
    assertEquals(gatheringDocument1.gatheringType(), searchResult1.gatheringType());
    assertEquals(gatheringDocument1.maximumParticipant(), searchResult1.maximumParticipant());
    assertEquals(gatheringDocument1.day().toLocalDate(), searchResult1.day());
    assertEquals(gatheringDocument1.time().toLocalTime(), searchResult1.time());
    assertEquals(gatheringDocument1.recruitmentStartDt(), searchResult1.recruitmentStartDt());
    assertEquals(gatheringDocument1.recruitmentEndDt(), searchResult1.recruitmentEndDt());
    assertEquals(gatheringDocument1.category(), searchResult1.category());
    assertEquals(gatheringDocument1.address(), searchResult1.address());
    assertEquals(gatheringDocument1.mainImg(), searchResult1.mainImg());
    assertEquals(gatheringDocument1.participantsType(), searchResult1.participantsType());
    assertEquals(gatheringDocument1.fee(), searchResult1.fee());
    assertEquals(gatheringDocument1.participantSelectionMethod(),
        searchResult1.participantSelectionMethod());
    assertEquals(gatheringDocument1.likeCount(), searchResult1.likeCount());
    assertEquals(gatheringDocument1.createdDttm(), searchResult1.createdDttm());
    assertEquals(gatheringDocument1.status(), searchResult1.status());

    GatheringSearchResponse searchResult2 = gatheringSearchResponses.getContent().get(1);
    assertEquals(gatheringDocument2.id(), searchResult2.gatheringId());
    assertEquals(gatheringDocument2.memberId(), searchResult2.memberId());
    assertEquals(gatheringDocument2.nickName(), searchResult2.nickName());
    assertEquals(gatheringDocument2.profileImg(), searchResult2.profileImg());
    assertEquals(gatheringDocument2.title(), searchResult2.title());
    assertEquals(gatheringDocument2.content(), searchResult2.content());
    assertEquals(gatheringDocument2.gatheringType(), searchResult2.gatheringType());
    assertEquals(gatheringDocument2.maximumParticipant(), searchResult2.maximumParticipant());
    assertEquals(gatheringDocument2.day().toLocalDate(), searchResult2.day());
    assertEquals(gatheringDocument2.time().toLocalTime(), searchResult2.time());
    assertEquals(gatheringDocument2.recruitmentStartDt(), searchResult2.recruitmentStartDt());
    assertEquals(gatheringDocument2.recruitmentEndDt(), searchResult2.recruitmentEndDt());
    assertEquals(gatheringDocument2.category(), searchResult2.category());
    assertEquals(gatheringDocument2.address(), searchResult2.address());
    assertEquals(gatheringDocument2.mainImg(), searchResult2.mainImg());
    assertEquals(gatheringDocument2.participantsType(), searchResult2.participantsType());
    assertEquals(gatheringDocument2.fee(), searchResult2.fee());
    assertEquals(gatheringDocument2.participantSelectionMethod(),
        searchResult2.participantSelectionMethod());
    assertEquals(gatheringDocument2.likeCount(), searchResult2.likeCount());
    assertEquals(gatheringDocument2.createdDttm(), searchResult2.createdDttm());
    assertEquals(gatheringDocument2.status(), searchResult2.status());
  }

}