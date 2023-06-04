package org.rednero.deadbydaylight.utils.structs;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class SpawnpointEntity extends Coordinates {
    private float yaw;
    private float pitch;

    public SpawnpointEntity() {
        super();
    }
    public SpawnpointEntity(double x, double y, double z, float yaw, float pitch) {
        super(x, y, z);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public SpawnpointEntity(Player player) {
        super(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
        this.yaw = player.getLocation().getYaw();
        this.pitch = player.getLocation().getPitch();
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    @Override
    public String saveToString() {
        return super.saveToString() + "::" + this.yaw + "::" + this.pitch;
    }

    @Override
    public void loadFromString(String string) {
        String[] split = string.split("::");
        super.loadFromString(split[0] + "::" + split[1] + "::" + split[2]);
        this.yaw = Float.parseFloat(split[3]);
        this.pitch = Float.parseFloat(split[4]);
    }

    public Location toLocation(World world) {
        return new Location(world, this.getX(), this.getY(), this.getZ(), this.yaw, this.pitch);
    }
}
