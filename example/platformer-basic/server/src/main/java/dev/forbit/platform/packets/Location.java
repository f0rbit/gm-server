package dev.forbit.platform.packets;

import lombok.Getter;
import lombok.Setter;

public class Location {

    @Getter @Setter double x;
    @Getter @Setter double y;

    public Location(double x, double y) {
        setX(x);
        setY(y);
    }

}
