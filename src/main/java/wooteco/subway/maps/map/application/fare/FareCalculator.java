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
    protected Fare calculateFareByDistance(Fare fare, int distance) {
        if (distance == 0) {
            return fare.plus(0);
        } else if (distance <= 10) {
            return fare.plus(1250);
        } else if (distance <= 50) {
            return fare.plus(1250).plus(getAdditionalFare(distance, 5));
        } else {
            return fare.plus(1250).plus(getAdditionalFare(distance, 8));
        }
    }

    private int getAdditionalFare(int distance, int criteria) {
        return (int)((Math.ceil((distance - 1) / criteria) + 1) * 100);
    }

    @Override
    protected Fare calculateAdditionalLineFare(Fare fare, Map<Long, Station> stations, List<Line> lines) {
        List<Long> collect = new ArrayList<>(stations.keySet());
        final int lineFare = lines.stream()
            .filter(line -> collect.contains(line.getId()))
            .mapToInt(Line::getFare)
            .max().getAsInt();
        return fare.plus(lineFare);
    }

    @Override
    protected Fare calculateDiscountByAge(Fare fare, LoginMember member) {
        return member.discountFare(fare.get());
    }
}
