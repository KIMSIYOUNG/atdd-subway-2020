package wooteco.subway.maps.line.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;

@Embeddable
public class Fare {
    private int fare;

    public Fare() {
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public Fare plus(final int value) {
        return new Fare(this.fare + value);
    }

    public Fare minus(final int value) {
        return new Fare(this.fare - value);
    }

    public Fare multiply(final double discountRate) {
        return new Fare((int)(this.fare * discountRate));
    }

    public int get() {
        return fare;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        final Fare fare1 = (Fare)o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
