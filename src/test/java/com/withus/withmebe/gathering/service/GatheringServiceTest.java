//package com.withus.withmebe.gathering.service;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.withus.withmebe.gathering.dto.request.AddGatheringRequest;
//import com.withus.withmebe.gathering.entity.Gathering;
//import com.withus.withmebe.gathering.repository.GatheringRepository;
//import com.withus.withmebe.member.entity.Member;
//import com.withus.withmebe.member.repository.MemberRepository;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class GatheringServiceTest {
//
//    @Mock
//    private GatheringRepository gatheringRepository;
//
//    @Mock
//    private MemberRepository memberRepository;
//
//    private GatheringService gatheringService;
//
//    @BeforeEach
//    void setUp() {
//        gatheringService = new GatheringService(gatheringRepository, memberRepository);
//    }
//
//    @Test
//    void readGatheringList() {
//        // Given
//        List<Gathering> expectedGatherings = List.of(new Gathering(), new Gathering());
//        when(gatheringRepository.findAll()).thenReturn(expectedGatherings);
//
//        // When
//        List<Gathering> actualGatherings = gatheringService.readGatheringList();
//
//        // Then
//        assertEquals(expectedGatherings.size(), actualGatherings.size());
//    }
//
//    @Test
//    void updateGathering() {
//        // Given
//        long memberId = 1L;
//        long gatheringId = 1L;
//        AddGatheringRequest addGatheringRequest = new AddGatheringRequest();
//        Gathering existingGathering = new Gathering();
//        existingGathering.setMemberId(memberId);
//        when(gatheringRepository.findById(gatheringId)).thenReturn(Optional.of(existingGathering));
//        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(new Member()));
//
//        // When
//        Gathering updatedGathering = gatheringService.updateGathering(memberId, gatheringId, addGatheringRequest);
//
//        // Then
//        assertNotNull(updatedGathering);
//    }
//
//    @Test
//    void readGathering() {
//        // Given
//        long gatheringId = 1L;
//        Gathering expectedGathering = new Gathering();
//        when(gatheringRepository.findById(gatheringId)).thenReturn(Optional.of(expectedGathering));
//
//        // When
//        Gathering actualGathering = gatheringService.readGathering(gatheringId);
//
//        // Then
//        assertEquals(expectedGathering, actualGathering);
//    }
//
//    @Test
//    void deleteGathering() {
//        // Given
//        long memberId = 1L;
//        long gatheringId = 1L;
//        Gathering existingGathering = new Gathering();
//        existingGathering.setMemberId(memberId);
//        when(gatheringRepository.findById(gatheringId)).thenReturn(Optional.of(existingGathering));
//
//        // When
//        assertDoesNotThrow(() -> gatheringService.deleteGathering(memberId, gatheringId));
//
//        // Then
//        verify(gatheringRepository, times(1)).deleteById(gatheringId);
//    }
//}
