package com.withus.withmebe.gathering.service;

import static com.withus.withmebe.common.exception.ExceptionCode.ENTITY_NOT_FOUND;

import com.withus.withmebe.common.exception.CustomException;
import com.withus.withmebe.common.exception.ExceptionCode;
import com.withus.withmebe.gathering.dto.request.AddGatheringRequest;
import com.withus.withmebe.gathering.dto.request.SetGatheringRequest;
import com.withus.withmebe.gathering.dto.response.AddGatheringResponse;
import com.withus.withmebe.gathering.dto.response.DeleteGatheringResponse;
import com.withus.withmebe.gathering.dto.response.GetGatheringResponse;
import com.withus.withmebe.gathering.dto.response.SetGatheringResponse;
import com.withus.withmebe.gathering.entity.Gathering;
import com.withus.withmebe.gathering.repository.GatheringRepository;
import com.withus.withmebe.member.entity.Member;
import com.withus.withmebe.member.repository.MemberRepository;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
@RequiredArgsConstructor
public class GatheringService {

  private final GatheringRepository gatheringRepository;
  private final MemberRepository memberRepository;
  private final ImgService imgService;

  @Transactional
  public AddGatheringResponse createGathering(long currentMemberId,
      AddGatheringRequest addGatheringRequest){
    Member newMember = findByMemberId(currentMemberId);
    Gathering gathering = gatheringRepository.save(
        addGatheringRequest.toEntity(newMember));
    return gathering.toAddGatheringResponse();
  }

  @Transactional
  public SetGatheringResponse createGathering(long gathering, MultipartFile mainImg, MultipartFile subImg1,
      MultipartFile subImg2, MultipartFile subImg3) throws IOException {
    Result s3UpdateUrl = updateImage(mainImg, subImg1, subImg2, subImg3);
    Gathering newGathering = findByGatheringId(gathering);
    newGathering.updateGatheringImage(s3UpdateUrl);
    return newGathering.toSetGatheringResponse();
  }

  @Transactional(readOnly = true)
  public List<GetGatheringResponse> readGatheringList() {
    List<Gathering> gatherings = gatheringRepository.findAllByOrderByCreatedDttmDesc();
    return gatherings.stream()
        .map(gathering -> gathering.toGetGatheringResponse(gathering.getMember()))
        .collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public List<GetGatheringResponse> readGatheringMyList(long currentMemberId) {
    List<Gathering> myGatherings = gatheringRepository.findAllByMemberId(currentMemberId);
    return myGatherings.stream().map(gathering -> gathering.toGetGatheringResponse(gathering.getMember())).collect(
        Collectors.toList());
  }

  @Transactional
  public SetGatheringResponse updateGathering(long currentMemberId, long gatheringId,
      SetGatheringRequest setGatheringRequest) {
    Gathering gathering = getGathering(currentMemberId, gatheringId);
    gathering.updateGatheringFields(setGatheringRequest);
    return gathering.toSetGatheringResponse();
  }

  @Transactional
  public SetGatheringResponse updateGathering(long gathering, MultipartFile mainImg, MultipartFile subImg1,
      MultipartFile subImg2, MultipartFile subImg3) throws IOException {
    Result s3UpdateUrl = updateImage(mainImg, subImg1, subImg2, subImg3);
    Gathering newGathering = findByGatheringId(gathering);
    newGathering.updateGatheringImage(s3UpdateUrl);
    return newGathering.toSetGatheringResponse();
  }

  public GetGatheringResponse readGathering(long gatheringId) {
    Gathering gathering = findByGatheringId(gatheringId);
    return gathering.toGetGatheringResponse(gathering.getMember());
  }

  public DeleteGatheringResponse deleteGathering(long currentMemberId, long gatheringId) {
    Gathering gathering = getGathering(currentMemberId, gatheringId);
    gatheringRepository.deleteById(gatheringId);
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
  private Result updateImage(MultipartFile mainImg, MultipartFile subImg1, MultipartFile subImg2,
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
}
