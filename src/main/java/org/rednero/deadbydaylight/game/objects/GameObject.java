package org.rednero.deadbydaylight.game.objects;

import org.rednero.deadbydaylight.utils.enums.ObjectType;
import org.rednero.deadbydaylight.utils.structs.SpawnpointObject;

import java.util.List;

public interface GameObject {
    SpawnpointObject getSpawnpointObject();
    ObjectType getObjectType();
}
