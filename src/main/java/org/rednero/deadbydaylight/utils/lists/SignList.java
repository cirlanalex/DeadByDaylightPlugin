package org.rednero.deadbydaylight.utils.lists;

import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.rednero.deadbydaylight.utils.enums.SignType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignList {
    private final Map<Sign, SignType> signs;
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public SignList(JavaPlugin plugin) {
        this.signs = new HashMap<>();
        this.plugin = plugin;
        createConfig();
        loadConfig();
    }

    public void addSign(Sign sign, SignType type) {
        if (type == null) {
            return;
        }
        this.signs.put(sign, type);
        if (type == SignType.JOIN) {
            sign.setMetadata("dbd_join", new FixedMetadataValue(this.plugin, true));
        } else if (type == SignType.LEAVE) {
            sign.setMetadata("dbd_leave", new FixedMetadataValue(this.plugin, true));
        } else if (type == SignType.STATS) {
            sign.setMetadata("dbd_stats", new FixedMetadataValue(this.plugin, true));
        } else if (type == SignType.SPECTATE) {
            sign.setMetadata("dbd_spectate", new FixedMetadataValue(this.plugin, true));
        }
    }

    public void removeSign(Sign sign) {
        if (this.signs.get(sign) == SignType.JOIN) {
            sign.removeMetadata("dbd_join", this.plugin);
        } else if (this.signs.get(sign) == SignType.LEAVE) {
            sign.removeMetadata("dbd_leave", this.plugin);
        } else if (this.signs.get(sign) == SignType.STATS) {
            sign.removeMetadata("dbd_stats", this.plugin);
        } else if (this.signs.get(sign) == SignType.SPECTATE) {
            sign.removeMetadata("dbd_spectate", this.plugin);
        }
        this.signs.remove(sign);
    }

    public boolean isSign(Sign sign) {
        return this.signs.containsKey(sign);
    }

    private void createConfig() {
        this.configFile = new File(this.plugin.getDataFolder(), "signs.yml");
        if (!this.configFile.exists()) {
            this.configFile.getParentFile().mkdirs();
            this.plugin.saveResource("signs.yml", false);
        }
    }

    private void loadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        List<String> join_signs = this.config.getStringList("joinSigns");
        List<String> leave_signs = this.config.getStringList("leaveSigns");
        List<String> stats_signs = this.config.getStringList("statsSigns");
        List<String> spectate_signs = this.config.getStringList("spectateSigns");
        for (String join_sign : join_signs) {
            String[] split = join_sign.split("::");
            Sign sign = (Sign) this.plugin.getServer().getWorld(split[0]).getBlockAt(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])).getState();
            this.addSign(sign, SignType.JOIN);
        }
        for (String leave_sign : leave_signs) {
            String[] split = leave_sign.split("::");
            Sign sign = (Sign) this.plugin.getServer().getWorld(split[0]).getBlockAt(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])).getState();
            this.addSign(sign, SignType.LEAVE);
        }
        for (String stats_sign : stats_signs) {
            String[] split = stats_sign.split("::");
            Sign sign = (Sign) this.plugin.getServer().getWorld(split[0]).getBlockAt(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])).getState();
            this.addSign(sign, SignType.STATS);
        }
        for (String spectate_sign : spectate_signs) {
            String[] split = spectate_sign.split("::");
            Sign sign = (Sign) this.plugin.getServer().getWorld(split[0]).getBlockAt(Integer.parseInt(split[1]), Integer.parseInt(split[2]), Integer.parseInt(split[3])).getState();
            this.addSign(sign, SignType.SPECTATE);
        }
    }

    public void saveConfig() {
        try {
            List<String> join_signs = new ArrayList<>();
            List<String> leave_signs = new ArrayList<>();
            List<String> stats_signs = new ArrayList<>();
            List<String> spectate_signs = new ArrayList<>();
            for (Map.Entry<Sign, SignType> entry : this.signs.entrySet()) {
                if (entry.getValue() == SignType.JOIN) {
                    join_signs.add(entry.getKey().getLocation().getWorld().getName() + "::" + entry.getKey().getLocation().getBlockX() + "::" + entry.getKey().getLocation().getBlockY() + "::" + entry.getKey().getLocation().getBlockZ());
                } else if (entry.getValue() == SignType.LEAVE) {
                    leave_signs.add(entry.getKey().getLocation().getWorld().getName() + "::" + entry.getKey().getLocation().getBlockX() + "::" + entry.getKey().getLocation().getBlockY() + "::" + entry.getKey().getLocation().getBlockZ());
                } else if (entry.getValue() == SignType.STATS) {
                    stats_signs.add(entry.getKey().getLocation().getWorld().getName() + "::" + entry.getKey().getLocation().getBlockX() + "::" + entry.getKey().getLocation().getBlockY() + "::" + entry.getKey().getLocation().getBlockZ());
                } else if (entry.getValue() == SignType.SPECTATE) {
                    spectate_signs.add(entry.getKey().getLocation().getWorld().getName() + "::" + entry.getKey().getLocation().getBlockX() + "::" + entry.getKey().getLocation().getBlockY() + "::" + entry.getKey().getLocation().getBlockZ());
                }
            }
            this.config.set("joinSigns", join_signs);
            this.config.set("leaveSigns", leave_signs);
            this.config.set("statsSigns", stats_signs);
            this.config.set("spectateSigns", spectate_signs);
            this.config.save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
