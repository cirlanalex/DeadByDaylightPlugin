package org.rednero.deadbydaylight.game.objects;

import org.rednero.deadbydaylight.utils.enums.ObjectType;
import org.rednero.deadbydaylight.utils.structs.SpawnpointObject;

public interface GameObject {
    SpawnpointObject getSpawnpointObject();
    ObjectType getObjectType();
}
