package wooteco.subway.maps.map.application.fare;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.maps.line.domain.Fare;
import wooteco.subway.maps.line.domain.Line;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.members.member.domain.LoginMember;

class FareServiceTest {
    private FareCalculator fareCalculator;

    @BeforeEach
    void setUp() {
        fareCalculator = new FareCalculator();
    }

    @DisplayName("어린이 + 최대 라인 Fare(800) + 거리 30")
    @Test
    void childAndFare800AndDistance30() {
        LoginMember loginMember = getLoginMember(7);
        int distance = 30;
        Map<Long, Station> stations = getLongStationMap(null);
        List<Line> lines = getLines(500, 700, 800);

        final Fare fare = fareCalculator.calculateFare(loginMember, distance, stations, lines);
        assertThat(fare.isSame(1150)).isTrue();
    }

    @DisplayName("학생 + 최대 라인 Fare(500) + 거리 20")
    @Test
    void studentAndFare500AndDistance20() {
        LoginMember loginMember = getLoginMember(14);
        int distance = 20;
        Map<Long, Station> stations = getLongStationMap(null);
        List<Line> lines = getLines(500, 100, 200);
        final Fare fare = fareCalculator.calculateFare(loginMember, distance, stations, lines);

        assertThat(fare.isSame(1440)).isTrue();
    }

    @DisplayName("일반인 + 최대 라인 Fare(0) + 거리 70")
    @Test
    void normalAndFare0AndDistance70() {
        LoginMember loginMember = getLoginMember(21);
        int distance = 70;
        Map<Long, Station> stations = getLongStationMap(2L);
        List<Line> lines = getLines(0, 700, 0);

        final Fare fare = fareCalculator.calculateFare(loginMember, distance, stations, lines);
        assertThat(fare.isSame(2150)).isTrue();
    }

    @DisplayName("일반인 + 최대 라인 Fare(800) + 거리 5")
    @Test
    void normalAndFare800AndDistance5() {
        LoginMember loginMember = getLoginMember(60);
        int distance = 5;
        Map<Long, Station> stations = getLongStationMap(null);
        List<Line> lines = getLines(500, 700, 800);

        final Fare fare = fareCalculator.calculateFare(loginMember, distance, stations, lines);
        assertThat(fare.isSame(2050)).isTrue();
    }

    private LoginMember getLoginMember(final int age) {
        return new LoginMember(1L, "TEST", "TEST", age);
    }

    private Map<Long, Station> getLongStationMap(Long excludeId) {
        Map<Long, Station> stations = new HashMap<>();
        stations.put(1L, new Station("판교"));
        stations.put(2L, new Station("잠실"));
        stations.put(3L, new Station("분당"));

        if (Objects.nonNull(excludeId)) {
            stations.remove(excludeId);
        }
        return stations;
    }

    private List<Line> getLines(int lineFare, int lineFare2, int lineFare3) {
        return Arrays.asList(
            new Line(1L, "1호선", "레드", LocalTime.now(), LocalTime.now().plusHours(2), 10, lineFare),
            new Line(2L, "2호선", "노랑", LocalTime.now(), LocalTime.now().plusHours(2), 10, lineFare2),
            new Line(3L, "3호선", "블루", LocalTime.now(), LocalTime.now().plusHours(2), 10, lineFare3)
        );
    }
}