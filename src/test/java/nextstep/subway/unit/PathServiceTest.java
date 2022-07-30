package nextstep.subway.unit;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import nextstep.subway.applicaion.PathService;
import nextstep.subway.applicaion.dto.PathResponse;
import nextstep.subway.applicaion.dto.PathStationResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class PathServiceTest {

  private static final String GYODEASTATION = "교대역";
  private static final String GANGNAMSTATION = "강남역";
  private static final String YOUNGJEASTATION = "양재역";
  private static final String NAMBOOSTATION = "남부터미널역";

  private static final String SECONDLINENAME = "2호선";
  private static final String THIRDLINENAME = "3호선";
  private static final String SINBUNDANGLINENAME = "신분당선";

  private static final String RED = "red";
  private static final String GREEN = "green";
  private static final String ORANGE = "orange";

  private Station 교대역;
  private Station 강남역;
  private Station 양재역;
  private Station 남부터미널역;

  private Line 이호선;
  private Line 신분당선;
  private Line 삼호선;

  @Autowired
  private PathService pathService;

  @Autowired
  private StationRepository stationRepository;

  @Autowired
  private LineRepository lineRepository;

  /**
   * 교대역    --- *2호선* ---   강남역
   * |                        |
   * *3호선*                   *신분당선*
   * |                        |
   * 남부터미널역  --- *3호선* ---   양재
   */

  @BeforeEach
  public void setUp() {
    교대역 = stationRepository.save(new Station(GYODEASTATION));
    강남역 = stationRepository.save(new Station(GANGNAMSTATION));
    양재역 = stationRepository.save(new Station(YOUNGJEASTATION));
    남부터미널역 = stationRepository.save(new Station(NAMBOOSTATION));

    이호선 = lineRepository.save(new Line(SECONDLINENAME, GREEN));
    신분당선 = lineRepository.save(new Line(SINBUNDANGLINENAME, RED));
    삼호선 = lineRepository.save(new Line(THIRDLINENAME, ORANGE));

    이호선.addSection(new Section(이호선, 교대역, 강남역, 10));
    신분당선.addSection(new Section(신분당선, 강남역, 양재역, 10));
    삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 10));
    삼호선.addSection(new Section(삼호선, 남부터미널역, 양재역, 5));
  }

  @Test
  void 최단_경로_조회() {
    PathResponse pathResponse = pathService.getPath(교대역.getId(), 양재역.getId());

    List<String> names = pathResponse.getStations().stream()
        .map(PathStationResponse::getName)
        .collect(toList());

    assertAll(
        () -> assertEquals(pathResponse.getDistance(), 15),
        () -> assertEquals(pathResponse.getStations().size(), 3),
        () -> assertThat(names).containsExactly(교대역.getName(), 남부터미널역.getName(), 양재역.getName())
    );
  }
}
