package org.rednero.deadbydaylight.utils.structs;

import org.rednero.deadbydaylight.utils.enums.Direction;

public class SpawnpointObject extends Coordinates {
    Direction direction;

    public SpawnpointObject() {
        super();
    }

    public SpawnpointObject(double x, double y, double z, Direction direction) {
        super(x, y, z);
        this.direction = direction;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String saveToString() {
        return super.saveToString() + "::" + this.direction.name();
    }

    @Override
    public void loadFromString(String string) {
        String[] split = string.split("::");
        super.loadFromString(split[0] + "::" + split[1] + "::" + split[2]);
        this.direction = Direction.valueOf(split[3]);
    }
}
