package org.rednero.deadbydaylight.game.objects;

import org.rednero.deadbydaylight.utils.enums.Direction;
import org.rednero.deadbydaylight.utils.enums.ObjectType;
import org.rednero.deadbydaylight.utils.structs.SpawnpointObject;

public class Generator implements GameObject {
    private SpawnpointObject spawnpointObject;

    public Generator(double x, double y, double z, Direction direction) {
        this.spawnpointObject = new SpawnpointObject(x, y, z, direction);
    }

    public Generator(SpawnpointObject spawnpointObject) {
        this.spawnpointObject = spawnpointObject;
    }

    public SpawnpointObject getSpawnpointObject() {
        return this.spawnpointObject;
    }

    @Override
    public ObjectType getObjectType() {
        return ObjectType.GENERATOR;
    }
}
