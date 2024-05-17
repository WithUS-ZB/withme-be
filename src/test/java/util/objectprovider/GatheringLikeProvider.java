package util.objectprovider;

import com.withus.withmebe.gathering.entity.GatheringLike;
import org.springframework.test.util.ReflectionTestUtils;

public class GatheringLikeProvider {

  private GatheringLikeProvider() {
  }
  public static GatheringLike getStubbedGatheringLike(boolean isLiked) {
    GatheringLike gatheringLike = new GatheringLike();
    ReflectionTestUtils.setField(gatheringLike, "isLiked", isLiked);
    return gatheringLike;
  }
}
