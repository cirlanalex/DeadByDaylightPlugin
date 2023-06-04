package org.rednero.deadbydaylight.utils.structs;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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

    public SpawnpointObject(Player player) {
        super(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        float yaw = player.getLocation().getYaw();
        if (yaw < 0) {
            yaw += 360;
        }
        if (yaw >= 315 || yaw < 45) {
            this.direction = Direction.SOUTH;
        } else if (yaw >= 45 && yaw < 135) {
            this.direction = Direction.WEST;
        } else if (yaw >= 135 && yaw < 225) {
            this.direction = Direction.NORTH;
        } else if (yaw >= 225 && yaw < 315) {
            this.direction = Direction.EAST;
        }
    }

    public SpawnpointObject(Player player, Block block) {
        super(block.getX(), block.getY(), block.getZ());
        float yaw = player.getLocation().getYaw();
        if (yaw < 0) {
            yaw += 360;
        }
        if (yaw >= 315 || yaw < 45) {
            this.direction = Direction.SOUTH;
        } else if (yaw >= 45 && yaw < 135) {
            this.direction = Direction.WEST;
        } else if (yaw >= 135 && yaw < 225) {
            this.direction = Direction.NORTH;
        } else if (yaw >= 225 && yaw < 315) {
            this.direction = Direction.EAST;
        }
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
