package wooteco.subway.maps.map.application.fare;

import java.util.Arrays;
import java.util.function.Function;

public enum DistanceCalculator {
    ZERO((distance) -> distance == 0, (distance) -> 0),
    LESS_EQUAL_THAN_TEN((distance) -> (distance > 0 && distance <= 10), (distance) -> 1250),
    LESS_EQUAL_THAN_FIFTH((distance) -> (distance > 10 && distance <= 50), (distance) -> 1250 + getAdditionalFare(distance, 5)),
    ETC((distance) -> true, (distance) -> 1250 + getAdditionalFare(distance, 8));

    private final Function<Integer, Boolean> supplier;
    private final Function<Integer, Integer> sum;

    DistanceCalculator(final Function<Integer, Boolean> supplier,
        final Function<Integer, Integer> sum) {
        this.supplier = supplier;
        this.sum = sum;
    }

    public static DistanceCalculator of(final int distance) {
        return Arrays.stream(values())
            .filter((instance) -> instance.apply(distance))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    private Boolean apply(int distance) {
        return this.supplier.apply(distance);
    }

    public int calculate(int startMoney, int distance) {
        return startMoney + this.sum.apply(distance);
    }

    private static int getAdditionalFare(int distance, int criteria) {
        return (int)((Math.ceil((distance - 1) / criteria) + 1) * 100);
    }
}
