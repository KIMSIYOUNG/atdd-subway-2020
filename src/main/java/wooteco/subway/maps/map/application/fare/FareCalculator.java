package wooteco.subway.maps.map.application.fare;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import wooteco.subway.maps.line.domain.Fare;
import wooteco.subway.maps.line.domain.Line;
import wooteco.subway.maps.station.domain.Station;
import wooteco.subway.members.member.domain.LoginMember;

@Component
public class FareCalculator extends FareService {
    @Override
    public Fare calculateFare(final LoginMember member, final int distance, final Map<Long, Station> stations,
        final List<Line> lines) {
        return super.calculateFare(member, distance, stations, lines);
    }

    @Override
    protected Fare calculateFareByDistance(Fare fare, int distance) {
        DistanceCalculator distanceCalculator = DistanceCalculator.of(distance);
        final int calculate = distanceCalculator.calculate(fare.get(), distance);

        return new Fare(calculate);
    }

    @Override
    protected Fare calculateAdditionalLineFare(Fare fare, Map<Long, Station> stations, List<Line> wholeLines) {
        List<Long> registeredLineIds = new ArrayList<>(stations.keySet());
        final int lineFare = wholeLines.stream()
            .filter(line -> registeredLineIds.contains(line.getId()))
            .mapToInt(Line::getFare)
            .max().getAsInt();
        return fare.plus(lineFare);
    }

    @Override
    protected Fare calculateDiscountByAge(Fare fare, LoginMember member) {
        return member.discountFare(fare.get());
    }
}
