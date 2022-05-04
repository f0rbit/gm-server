package dev.forbit.networking;

import lombok.Data;

public @Data class Location {
    int x;
    int y;

    public Location(int x, int y) {
        setX(x);
        setY(y);
    }
}
