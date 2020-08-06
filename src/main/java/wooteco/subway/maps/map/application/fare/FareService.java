package wooteco.subway.maps.map.application.fare;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import wooteco.subway.maps.line.domain.Fare;
import wooteco.subway.maps.line.domain.Line;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.members.member.domain.LoginMember;

@Service
public abstract class FareService {

    public Fare getBasicFare() {
        return new Fare(0);
    }

    public Fare calculateFare(LoginMember member, int distance, Map<Long, Station> stations, List<Line> lines) {
        Fare basicFare = getBasicFare();
        Fare distanceFare = calculateFareByDistance(basicFare, distance);
        Fare distanceAndLine = calculateAdditionalLineFare(distanceFare, stations, lines);
        return calculateDiscountByAge(distanceAndLine, member);
    }

    protected abstract Fare calculateFareByDistance(Fare fare, int distance);

    protected abstract Fare calculateAdditionalLineFare(Fare fare, Map<Long, Station> stations, List<Line> lines);

    protected abstract Fare calculateDiscountByAge(Fare fare, LoginMember member);
}
