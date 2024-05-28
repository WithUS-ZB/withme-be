package com.withus.withmebe.gathering.service;

import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import com.withus.withmebe.gathering.annotation.GatheringLimit;
import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.Type.GatheringType;
import com.withus.withmebe.gathering.Type.Status;
import com.withus.withmebe.gathering.dto.request.AddGatheringRequest;
import com.withus.withmebe.gathering.dto.request.SetGatheringRequest;
import com.withus.withmebe.gathering.dto.response.AddGatheringResponse;
import com.withus.withmebe.gathering.dto.response.DeleteGatheringResponse;
import com.withus.withmebe.gathering.dto.response.GetGatheringResponse;
import com.withus.withmebe.gathering.dto.response.SetGatheringResponse;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.event.DeleteGatheringEvent;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import com.withus.withmebe.notification.event.NotificationEvent;
import com.withus.withmebe.notification.type.NotificationType;
import com.withus.withmebe.participation.entity.Participation;
import com.withus.withmebe.participation.repository.ParticipationRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.context.ApplicationEventPublisher;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class GatheringService {

  private final ApplicationEventPublisher eventPublisher;
  private final ParticipationRepository participationRepository;
  private final GatheringRepository gatheringRepository;
  private final MemberRepository memberRepository;
  private final ImgService imgService;
  private static final String GATHERING_REMINDER_MESSAGE = "%s [%s] 시작 하루 전입니다.";

  @Transactional
  @GatheringLimit(value = 5)
  public AddGatheringResponse createGathering(long currentMemberId,
      AddGatheringRequest addGatheringRequest) {
    Member newMember = findByMemberId(currentMemberId);
    Gathering gathering = gatheringRepository.save(addGatheringRequest.toEntity(newMember));
    return gathering.toAddGatheringResponse();
  }

  @Transactional
  public SetGatheringResponse createGathering(long gathering, MultipartFile mainImg,
      MultipartFile subImg1,
      MultipartFile subImg2, MultipartFile subImg3) throws IOException {
    Result s3UpdateUrl = updateImages(mainImg, subImg1, subImg2, subImg3);
    Gathering newGathering = findByGatheringId(gathering);
    newGathering.initiateGatheringImages(s3UpdateUrl);
    return newGathering.toSetGatheringResponse();
  }

  @Transactional(readOnly = true)
  public Page<GetGatheringResponse> readGatheringList(GatheringType range, Pageable pageable) {
    if (range.equals(GatheringType.ALL)) {
      return gatheringRepository.findAllBy(pageable)
          .map(Gathering::toGetGatheringResponse);
    } else {
      return gatheringRepository.findAllByGatheringType(range, pageable)
          .map(Gathering::toGetGatheringResponse);
    }
  }

  @Transactional(readOnly = true)
  public Page<GetGatheringResponse> readGatheringMyList(long currentMemberId, Pageable pageable) {
    return gatheringRepository.findAllByMemberId(currentMemberId, pageable)
        .map(Gathering::toGetGatheringResponse);
  }

  @Transactional
  public SetGatheringResponse updateGathering(long currentMemberId, long gatheringId,
      SetGatheringRequest setGatheringRequest) {
    Gathering gathering = getGathering(currentMemberId, gatheringId);
    gathering.updateGatheringFields(setGatheringRequest);
    return gathering.toSetGatheringResponse();
  }

  @Transactional
  public SetGatheringResponse updateGathering(long gathering, MultipartFile mainImg,
      MultipartFile subImg1,
      MultipartFile subImg2, MultipartFile subImg3) throws IOException {
    Result s3UpdateUrl = updateImages(mainImg, subImg1, subImg2, subImg3);
    Gathering newGathering = findByGatheringId(gathering);
    newGathering.updateGatheringImages(s3UpdateUrl);
    return newGathering.toSetGatheringResponse();
  }

  public GetGatheringResponse readGathering(long gatheringId) {
    Gathering gathering = findByGatheringId(gatheringId);
    return gathering.toGetGatheringResponse();
  }

  public DeleteGatheringResponse deleteGathering(long currentMemberId, long gatheringId) {
    Gathering gathering = getGathering(currentMemberId, gatheringId);
    gatheringRepository.deleteById(gatheringId);
    eventPublisher.publishEvent(new DeleteGatheringEvent(gatheringId));
    return gathering.toDeleteGatheringResponse();
  }

  private Gathering getGathering(long currentMemberId, long gatheringId) {
    Gathering gathering = findByGatheringId(gatheringId);
    if (currentMemberId != gathering.getMember().getId()) {
      throw new CustomException(ExceptionCode.AUTHORIZATION_ISSUE);
    }
    return gathering;
  }

  private Gathering findByGatheringId(long gatheringId) {
    return gatheringRepository.findById(gatheringId)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }

  private Member findByMemberId(long memberId) {
    return memberRepository.findById(memberId)
        .orElseThrow(() -> new CustomException(ENTITY_NOT_FOUND));
  }

  @NotNull
  private Result updateImages(MultipartFile mainImg, MultipartFile subImg1, MultipartFile subImg2,
      MultipartFile subImg3) throws IOException {
    String mainImgUrl = imageCheckNotNull(mainImg);
    String subImgUrl1 = imageCheckNotNull(subImg1);
    String subImgUrl2 = imageCheckNotNull(subImg2);
    String subImgUrl3 = imageCheckNotNull(subImg3);
    return new Result(mainImgUrl, subImgUrl1, subImgUrl2, subImgUrl3);
  }

  private String imageCheckNotNull(MultipartFile image) throws IOException {
    if (image == null) {
      return "";
    }
    return imgService.updateImageToS3(image);
  }

  public record Result(String mainImgUrl, String subImgUrl1, String subImgUrl2,
                       String subImgUrl3) {

  }

  @Scheduled(cron = "${spring.gathering.reminder.cron}")
  public void gatheringReminder() {

    LocalDate tomorrow = LocalDate.now().plusDays(1);
    List<Gathering> gatherings =
        gatheringRepository.findAllByDayAndStatusEquals(tomorrow, Status.PROGRESS);

    for (Gathering gathering : gatherings) {
      List<Long> participants = participationRepository.findAllByGathering_Id(gathering.getId())
          .stream()
          .filter(Participation::isValidParticipationStatus)
          .map(Participation::getParticipant)
          .map(Member::getId).collect(Collectors.toList());

      eventPublisher.publishEvent(NotificationEvent.builder()
          .receivers(participants)
          .message(
              String.format(GATHERING_REMINDER_MESSAGE, gathering.getGatheringType().getValue(),
                  gathering.getTitle()))
          .notificationType(NotificationType.GATHERING)
          .build()
      );
    }
  }
}
