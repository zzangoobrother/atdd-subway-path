package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Station;

public class PathStationResponse {
    private Long id;
    private String name;

    public PathStationResponse() {
    }

    public PathStationResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static PathStationResponse createStationResponse(Station station) {
        return new PathStationResponse(
            station.getId(),
            station.getName()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
