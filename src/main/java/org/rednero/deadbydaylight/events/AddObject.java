package org.rednero.deadbydaylight.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.rednero.deadbydaylight.game.Game;
import org.rednero.deadbydaylight.game.objects.*;
import org.rednero.deadbydaylight.utils.enums.Direction;
import org.rednero.deadbydaylight.utils.structs.SpawnpointObject;

public class AddObject implements Listener {
    private final FileConfiguration config;
    private final Game game;

    public AddObject(FileConfiguration config, Game game) {
        this.config = config;
        this.game = game;
    }

    private boolean checkBlock(double x, double y, double z) {
        if (Bukkit.getWorld(this.config.getString("game.world")).getBlockAt((int) x, (int) y, (int) z).getType() != Material.AIR) {
            return false;
        }
        return true;
    }

    private boolean checkExitGate(SpawnpointObject spawnpointObject) {
        int x = (int) spawnpointObject.getX();
        int y = (int) spawnpointObject.getY();
        int z = (int) spawnpointObject.getZ();

        if (spawnpointObject.getDirection() == Direction.NORTH) {
            z++;
        } else if (spawnpointObject.getDirection() == Direction.EAST) {
            x--;
        } else if (spawnpointObject.getDirection() == Direction.SOUTH) {
            z--;
        } else if (spawnpointObject.getDirection() == Direction.WEST) {
            x++;
        }

        if (!this.checkBlock(x, y, z)) {
            return false;
        }
        return true;
    }

    private boolean checkOneBlockObject(SpawnpointObject spawnpointObject, int yOffset, boolean checkGround) {
        int x = (int)  spawnpointObject.getX();
        int y = (int) spawnpointObject.getY() + yOffset;
        int z = (int) spawnpointObject.getZ();

        if (!this.checkBlock(x, y, z)) {
            return false;
        }

        if (checkGround) {
            for (int i = x - 1; i < x + 2; i++) {
                for (int j = z - 1; j < z + 2; j++) {
                    if (this.checkBlock(i, y - 1, j)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean checkObject(SpawnpointObject spawnpointObject, int width, int height, int length, int yOffset, boolean checkGround) {
        int startX, startY, startZ;
        int endX, endY, endZ;

        startY = (int) spawnpointObject.getY() + yOffset;
        endY = startY + height;

        if (spawnpointObject.getDirection() == Direction.NORTH) {
            startX = (int) spawnpointObject.getX();
            endX = startX + length;
            startZ = (int) spawnpointObject.getZ() - width + 1;
            endZ = startZ + width;
        } else if (spawnpointObject.getDirection() == Direction.EAST) {
            startX = (int) spawnpointObject.getX();
            endX = startX + width;
            startZ = (int) spawnpointObject.getZ();
            endZ = startZ + length;
        } else if (spawnpointObject.getDirection() == Direction.SOUTH) {
            startX = (int) spawnpointObject.getX() - length + 1;
            endX = startX + length;
            startZ = (int) spawnpointObject.getZ();
            endZ = startZ + width;
        } else {
            startX = (int) spawnpointObject.getX() - width + 1;
            endX = startX + width;
            startZ = (int) spawnpointObject.getZ() - length + 1;
            endZ = startZ + length;
        }
        for (int i = startX; i < endX; i++) {
            for (int j = startY; j < endY; j++) {
                for (int k = startZ; k < endZ; k++) {
                    if (!this.checkBlock(i, j, k)) {
                        return false;
                    }
                }
            }
        }
        if (checkGround) {
            for (int i = startX - 1; i < endX + 1; i++) {
                for (int j = startZ - 1; j < endZ + 1; j++) {
                    if (this.checkBlock(i, startY - 1, j)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            ItemStack item = player.getItemInHand();
            if (item.getType() == Material.STICK) {
                if (item.hasItemMeta()) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta.getDisplayName().equals(ChatColor.GOLD + "Add Generator")) {
                        if (meta.getLore().get(0).equals("dbd.generator")) {
                            if (this.game.getPlayers().getTotalCount() > 0) {
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantEditGame"));
                                player.setItemInHand(new ItemStack(Material.AIR));
                                return;
                            }
                            SpawnpointObject spawnpoint = new SpawnpointObject(event.getPlayer(), event.getClickedBlock());
                            if (!checkObject(spawnpoint, 3, 3, 2, 1, false)) {
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantAddGenerator"));
                                player.setItemInHand(new ItemStack(Material.AIR));
                                return;
                            }
                            this.game.getSpawnpoints().getGenerators().add(new Generator(spawnpoint));
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.generatorAdded"));
                            player.setItemInHand(new ItemStack(Material.AIR));
                        }
                    } else if (meta.getDisplayName().equals(ChatColor.GOLD + "Add Exit Gate")) {
                        if (meta.getLore().get(0).equals("dbd.exitgate")) {
                            if (this.game.getPlayers().getTotalCount() > 0) {
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantEditGame"));
                                player.setItemInHand(new ItemStack(Material.AIR));
                                return;
                            }
                            SpawnpointObject spawnpoint = new SpawnpointObject(event.getPlayer(), event.getClickedBlock());
                            if (!checkExitGate(spawnpoint)) {
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantAddExitGate"));
                                player.setItemInHand(new ItemStack(Material.AIR));
                                return;
                            }
                            this.game.getSpawnpoints().getExitGates().add(new ExitGate(spawnpoint));
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.exitGateAdded"));
                            player.setItemInHand(new ItemStack(Material.AIR));
                        }
                    } else if (meta.getDisplayName().equals(ChatColor.GOLD + "Add Hatch")) {
                        if (meta.getLore().get(0).equals("dbd.hatch")) {
                            if (this.game.getPlayers().getTotalCount() > 0) {
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantEditGame"));
                                player.setItemInHand(new ItemStack(Material.AIR));
                                return;
                            }
                            SpawnpointObject spawnpoint = new SpawnpointObject(event.getPlayer(), event.getClickedBlock());
                            if (!checkObject(spawnpoint, 3, 3, 3, 1, false)) {
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantAddHatch"));
                                player.setItemInHand(new ItemStack(Material.AIR));
                                return;
                            }
                            this.game.getSpawnpoints().getHatches().add(new Hatch(spawnpoint));
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.hatchAdded"));
                            player.setItemInHand(new ItemStack(Material.AIR));
                        }
                    } else if (meta.getDisplayName().equals(ChatColor.GOLD + "Add Chest")) {
                        if (meta.getLore().get(0).equals("dbd.chest")) {
                            if (this.game.getPlayers().getTotalCount() > 0) {
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantEditGame"));
                                player.setItemInHand(new ItemStack(Material.AIR));
                                return;
                            }
                            SpawnpointObject spawnpoint = new SpawnpointObject(event.getPlayer(), event.getClickedBlock());
                            if (!checkOneBlockObject(spawnpoint, 1, false)) {
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantAddChest"));
                                player.setItemInHand(new ItemStack(Material.AIR));
                                return;
                            }
                            this.game.getSpawnpoints().getChests().add(new Chest(spawnpoint));
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.chestAdded"));
                            player.setItemInHand(new ItemStack(Material.AIR));
                        }
                    } else if (meta.getDisplayName().equals(ChatColor.GOLD + "Add Totem")) {
                        if (meta.getLore().get(0).equals("dbd.totem")) {
                            if (this.game.getPlayers().getTotalCount() > 0) {
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantEditGame"));
                                player.setItemInHand(new ItemStack(Material.AIR));
                                return;
                            }
                            SpawnpointObject spawnpoint = new SpawnpointObject(event.getPlayer(), event.getClickedBlock());
                            if (!checkOneBlockObject(spawnpoint, 1, false)) {
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantAddTotem"));
                                player.setItemInHand(new ItemStack(Material.AIR));
                                return;
                            }
                            this.game.getSpawnpoints().getTotems().add(new Totem(spawnpoint));
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.totemAdded"));
                            player.setItemInHand(new ItemStack(Material.AIR));
                        }
                    } else if (meta.getDisplayName().equals(ChatColor.GOLD + "Add Hook")) {
                        if (meta.getLore().get(0).equals("dbd.hook")) {
                            if (this.game.getPlayers().getTotalCount() > 0) {
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantEditGame"));
                                player.setItemInHand(new ItemStack(Material.AIR));
                                return;
                            }
                            SpawnpointObject spawnpoint = new SpawnpointObject(event.getPlayer(), event.getClickedBlock());
                            if (!checkObject(spawnpoint, 3, 3, 1, 1, true)) {
                                player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.cantAddHook"));
                                player.setItemInHand(new ItemStack(Material.AIR));
                                return;
                            }
                            this.game.getSpawnpoints().getHooks().add(new Hook(new SpawnpointObject(event.getPlayer(), event.getClickedBlock())));
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.hookAdded"));
                            player.setItemInHand(new ItemStack(Material.AIR));
                        }
                    }
                }
            }
        }
    }
}
