package wooteco.subway.maps.map.application.fare;

import java.util.Arrays;
import java.util.function.Function;

import wooteco.subway.maps.line.domain.Fare;
import wooteco.subway.members.member.domain.LoginMember;

public enum AgeGroup {
    CHILD((age) -> (age >= 6 && age < 13), (fare) -> new Fare((fare - 350) * 50 / 100)),
    STUDENT((age) -> (age >= 13 && age < 19), (fare) -> new Fare((fare - 350) * 80 / 100)),
    ADULT((age) -> (age == 0 || age >= 19), Fare::new);

    private final Function<Integer, Boolean> expression;
    private final Function<Integer, Fare> calculator;

    AgeGroup(final Function<Integer, Boolean> expression,
        final Function<Integer, Fare> calculator) {
        this.expression = expression;
        this.calculator = calculator;
    }

    public static AgeGroup of(final LoginMember member) {
        if (member == null) {
            return ADULT;
        }
        return Arrays.stream(values())
            .filter(instance -> instance.apply(member.getAge()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public Boolean apply(int age) {
        return this.expression.apply(age);
    }

    public Fare sum(Integer fare) {
        return this.calculator.apply(fare);
    }

    public Fare discountByAge(Fare fare) {
        return this.sum(fare.get());
    }
}
