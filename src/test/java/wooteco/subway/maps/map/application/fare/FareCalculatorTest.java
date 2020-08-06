package wooteco.subway.maps.map.application.fare;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import wooteco.subway.maps.line.domain.Fare;
import wooteco.subway.members.member.domain.LoginMember;

class FareCalculatorTest {
    private static final int BASIC_FARE = 1250;
    private static final int ZERO = 0;

    private final FareCalculator fareCalculator = new FareCalculator();

    @DisplayName("같은 역 혹은 움직이지 않은 경우 이용 요금은 0원입니다.")
    @Test
    void distanceTest() {
        final Fare fare = fareCalculator.calculateFareByDistance(new Fare(0), 0);

        assertThat(fare.get()).isEqualTo(ZERO);
    }

    @DisplayName("기본 요금을 내는 구간에는 1250원이 요금입니다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 3, 5, 10})
    void basicFare(int distance) {
        final Fare fare = fareCalculator.calculateFareByDistance(new Fare(0), distance);

        assertThat(fare.get()).isEqualTo(BASIC_FARE);
    }

    @DisplayName("10km~50km 까지는 5km마다 추가요금이 붙는다.")
    @ParameterizedTest
    @CsvSource(value = {"13, 1550", "10,1250", "49,2250"})
    void lessThan50(int distance, int money) {
        final Fare fare = fareCalculator.calculateFareByDistance(new Fare(0), distance);

        assertThat(fare.get()).isEqualTo(money);
    }

    @DisplayName("50km를 초과하는 경우 8km마다 추가요금이 붙는다.")
    @ParameterizedTest
    @CsvSource(value = {"58, 2050", "62,2050", "50,2250"})
    void over50(int distance, int money) {
        final Fare fare = fareCalculator.calculateFareByDistance(new Fare(0), distance);

        assertThat(fare.get()).isEqualTo(money);
    }

    @DisplayName("나이별로 할인이 추가 적용된다.")
    @ParameterizedTest
    @CsvSource(value = {"15, 720", "0,1250", "21,1250", "7, 450"})
    void calculateByAge(int age, int money) {
        final Fare fare = fareCalculator.calculateDiscountByAge(new Fare(1250), new LoginMember(1L, "TEST", "TEST", age));

        assertThat(fare.get()).isEqualTo(money);
    }
}