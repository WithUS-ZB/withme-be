package util.objectprovider;

import com.withus.withmebe.festival.entity.Festival;

public class FestivalProvider {

  private FestivalProvider() {
  }

  public static Festival getStubbedFestival(long festivalId) {
    return Festival.builder()
        .title("title" + festivalId)
        .img("img" + festivalId)
        .link("link" + festivalId)
        .build();
  }
}
