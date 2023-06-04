package org.rednero.deadbydaylight.utils.lists;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.rednero.deadbydaylight.game.objects.*;
import org.rednero.deadbydaylight.utils.enums.Direction;
import org.rednero.deadbydaylight.utils.enums.ObjectType;
import org.rednero.deadbydaylight.utils.structs.SpawnpointEntity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpawnpointList {
    private List<SpawnpointEntity> spawnpointsKiller;
    private List<SpawnpointEntity> spawnpointsSurvivor;
    private List<GameObject> chests;
    private List<GameObject> generators;
    private List<GameObject> hooks;
    private List<GameObject> exitGates;
    private List<GameObject> totems;
    private List<GameObject> hatches;
    private SpawnpointEntity spawnpointSpawn;
    private SpawnpointEntity spawnpointSpectator;
    private final JavaPlugin plugin;
    private File configFile;
    private FileConfiguration config;

    public SpawnpointList(JavaPlugin plugin) {
        this.spawnpointsKiller = new ArrayList<>();
        this.spawnpointsSurvivor = new ArrayList<>();
        this.chests = new ArrayList<>();
        this.generators = new ArrayList<>();
        this.hooks = new ArrayList<>();
        this.exitGates = new ArrayList<>();
        this.totems = new ArrayList<>();
        this.hatches = new ArrayList<>();
        this.plugin = plugin;
        createConfig();
        loadConfig();
    }

    public void addSpawnpointKiller(SpawnpointEntity spawnpoint) {
        this.spawnpointsKiller.add(spawnpoint);
    }

    public void addSpawnpointSurvivor(SpawnpointEntity spawnpoint) {
        this.spawnpointsSurvivor.add(spawnpoint);
    }

    public void setSpawnpointSpawn(SpawnpointEntity spawnpoint) {
        this.spawnpointSpawn = spawnpoint;
    }

    public void setSpawnpointSpectator(SpawnpointEntity spawnpoint) {
        this.spawnpointSpectator = spawnpoint;
    }

    public void removeSpawnpointKiller(SpawnpointEntity spawnpoint) {
        this.spawnpointsKiller.remove(spawnpoint);
    }

    public void removeSpawnpointSurvivor(SpawnpointEntity spawnpoint) {
        this.spawnpointsSurvivor.remove(spawnpoint);
    }

    public void addChest(GameObject chest) {
        this.chests.add(chest);
    }

    public void addGenerator(GameObject generator) {
        this.generators.add(generator);
    }

    public void addHook(GameObject hook) {
        this.hooks.add(hook);
    }

    public void addExitGate(GameObject exitGate) {
        this.exitGates.add(exitGate);
    }

    public void addTotem(GameObject tothem) {
        this.totems.add(tothem);
    }

    public void addHatch(GameObject hatch) {
        this.hatches.add(hatch);
    }

    public void removeChest(GameObject chest) {
        this.chests.remove(chest);
    }

    public void removeGenerator(GameObject generator) {
        this.generators.remove(generator);
    }

    public void removeHook(GameObject hook) {
        this.hooks.remove(hook);
    }

    public void removeExitGate(GameObject exitGate) {
        this.exitGates.remove(exitGate);
    }

    public void removeTotem(GameObject tothem) {
        this.totems.remove(tothem);
    }

    public void removeHatch(GameObject hatch) {
        this.hatches.remove(hatch);
    }

    public List<SpawnpointEntity> getSpawnpointsKiller() {
        return this.spawnpointsKiller;
    }

    public List<SpawnpointEntity> getSpawnpointsSurvivor() {
        return this.spawnpointsSurvivor;
    }

    public List<GameObject> getChests() {
        return this.chests;
    }

    public List<GameObject> getGenerators() {
        return this.generators;
    }

    public List<GameObject> getHooks() {
        return this.hooks;
    }

    public List<GameObject> getExitGates() {
        return this.exitGates;
    }

    public List<GameObject> getTotems() {
        return this.totems;
    }

    public List<GameObject> getHatches() {
        return this.hatches;
    }

    public SpawnpointEntity getSpawnpointSpawn() {
        return this.spawnpointSpawn;
    }

    public SpawnpointEntity getSpawnpointSpectator() {
        return this.spawnpointSpectator;
    }

    private void createConfig() {
        this.configFile = new File(this.plugin.getDataFolder(), "spawnpoints.yml");
        if (!this.configFile.exists()) {
            this.configFile.getParentFile().mkdirs();
            this.plugin.saveResource("spawnpoints.yml", false);
        }
    }

    private void loadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configFile);
        List<String> killerSpawnpoints = this.config.getStringList("killerSpawnPoints");
        List<String> survivorSpawnpoints = this.config.getStringList("survivorSpawnPoints");
        List<String> hooksSpawnpoints = this.config.getStringList("hooksSpawnPoints");
        List<String> chestsSpawnpoints = this.config.getStringList("chestsSpawnPoints");
        List<String> exitGatesSpawnpoints = this.config.getStringList("exitGatesSpawnPoints");
        List<String> generatorsSpawnpoints = this.config.getStringList("generatorsSpawnPoints");
        List<String> totemsSpawnpoints = this.config.getStringList("totemsSpawnPoints");
        List<String> hatchSpawnpoints = this.config.getStringList("hatchSpawnPoints");
        if (this.config.getString("spawnpointSpawn") != null) {
            String[] split = this.config.getString("spawnpointSpawn").split("::");
            this.spawnpointSpawn = new SpawnpointEntity(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Float.parseFloat(split[3]), Float.parseFloat(split[4]));
        }
        if (this.config.getString("spawnpointSpectator") != null) {
            String[] split = this.config.getString("spawnpointSpectator").split("::");
            this.spawnpointSpectator = new SpawnpointEntity(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Float.parseFloat(split[3]), Float.parseFloat(split[4]));
        }
        for (String killerSpawnpoint : killerSpawnpoints) {
            String[] split = killerSpawnpoint.split("::");
            SpawnpointEntity spawnpoint = new SpawnpointEntity(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Float.parseFloat(split[3]), Float.parseFloat(split[4]));
            this.addSpawnpointKiller(spawnpoint);
        }
        for (String survivorSpawnpoint : survivorSpawnpoints) {
            String[] split = survivorSpawnpoint.split("::");
            SpawnpointEntity spawnpoint = new SpawnpointEntity(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Float.parseFloat(split[3]), Float.parseFloat(split[4]));
            this.addSpawnpointSurvivor(spawnpoint);
        }
        for (String hookSpawnpoint : hooksSpawnpoints) {
            String[] split = hookSpawnpoint.split("::");
            Hook hook = new Hook(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Direction.valueOf(split[3]));
            this.addHook(hook);
        }
        for (String chestSpawnpoint : chestsSpawnpoints) {
            String[] split = chestSpawnpoint.split("::");
            Chest chest = new Chest(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Direction.valueOf(split[3]));
            this.addChest(chest);
        }
        for (String exitGateSpawnpoint : exitGatesSpawnpoints) {
            String[] split = exitGateSpawnpoint.split("::");
            ExitGate exitGate = new ExitGate(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Direction.valueOf(split[3]));
            this.addExitGate(exitGate);
        }
        for (String generatorSpawnpoint : generatorsSpawnpoints) {
            String[] split = generatorSpawnpoint.split("::");
            Generator generator = new Generator(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Direction.valueOf(split[3]));
            this.addGenerator(generator);
        }
        for (String totemSpawnpoint : totemsSpawnpoints) {
            String[] split = totemSpawnpoint.split("::");
            Totem totem = new Totem(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Direction.valueOf(split[3]));
            this.addTotem(totem);
        }
        for (String hatchSpawnpoint : hatchSpawnpoints) {
            String[] split = hatchSpawnpoint.split("::");
            Hatch hatch = new Hatch(Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Direction.valueOf(split[3]));
            this.addHatch(hatch);
        }
    }

    public void saveConfig() {
        try {
            List<String> killerSpawnpoints = new ArrayList<>();
            List<String> survivorSpawnpoints = new ArrayList<>();
            List<String> hooksSpawnpoints = new ArrayList<>();
            List<String> chestsSpawnpoints = new ArrayList<>();
            List<String> exitGatesSpawnpoints = new ArrayList<>();
            List<String> generatorsSpawnpoints = new ArrayList<>();
            List<String> totemsSpawnpoints = new ArrayList<>();
            List<String> hatchSpawnpoints = new ArrayList<>();
            for (SpawnpointEntity spawnpoint : this.spawnpointsKiller) {
                killerSpawnpoints.add(spawnpoint.saveToString());
            }
            for (SpawnpointEntity spawnpoint : this.spawnpointsSurvivor) {
                survivorSpawnpoints.add(spawnpoint.saveToString());
            }
            for (GameObject hook : this.hooks) {
                hooksSpawnpoints.add(hook.getSpawnpointObject().saveToString());
            }
            for (GameObject chest : this.chests) {
                chestsSpawnpoints.add(chest.getSpawnpointObject().saveToString());
            }
            for (GameObject exitGate : this.exitGates) {
                exitGatesSpawnpoints.add(exitGate.getSpawnpointObject().saveToString());
            }
            for (GameObject generator : this.generators) {
                generatorsSpawnpoints.add(generator.getSpawnpointObject().saveToString());
            }
            for (GameObject totem : this.totems) {
                totemsSpawnpoints.add(totem.getSpawnpointObject().saveToString());
            }
            for (GameObject hatch : this.hatches) {
                hatchSpawnpoints.add(hatch.getSpawnpointObject().saveToString());
            }
            this.config.set("killerSpawnPoints", killerSpawnpoints);
            this.config.set("survivorSpawnPoints", survivorSpawnpoints);
            this.config.set("hooksSpawnPoints", hooksSpawnpoints);
            this.config.set("chestsSpawnPoints", chestsSpawnpoints);
            this.config.set("exitGatesSpawnPoints", exitGatesSpawnpoints);
            this.config.set("generatorsSpawnPoints", generatorsSpawnpoints);
            this.config.set("totemsSpawnPoints", totemsSpawnpoints);
            this.config.set("hatchSpawnPoints", hatchSpawnpoints);
            if (this.spawnpointSpawn != null) {
                this.config.set("spawnpointSpawn", this.spawnpointSpawn.saveToString());
            }
            if (this.spawnpointSpectator != null) {
                this.config.set("spawnpointSpectator", this.spawnpointSpectator.saveToString());
            }
            this.config.save(this.configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
