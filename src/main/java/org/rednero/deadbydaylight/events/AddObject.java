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
import org.rednero.deadbydaylight.utils.structs.SpawnpointObject;

public class AddObject implements Listener {
    private final FileConfiguration config;
    private final Game game;

    public AddObject(FileConfiguration config, Game game) {
        this.config = config;
        this.game = game;
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
                            this.game.getSpawnpoints().getGenerators().add(new Generator(new SpawnpointObject(event.getPlayer(), event.getClickedBlock())));
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.generatorAdded"));
                        }
                    } else if (meta.getDisplayName().equals(ChatColor.GOLD + "Add Exit Gate")) {
                        if (meta.getLore().get(0).equals("dbd.exitgate")) {
                            this.game.getSpawnpoints().getExitGates().add(new ExitGate(new SpawnpointObject(event.getPlayer(), event.getClickedBlock())));
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.exitGateAdded"));
                        }
                    } else if (meta.getDisplayName().equals(ChatColor.GOLD + "Add Hatch")) {
                        if (meta.getLore().get(0).equals("dbd.hatch")) {
                            this.game.getSpawnpoints().getHatches().add(new Hatch(new SpawnpointObject(event.getPlayer(), event.getClickedBlock())));
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.hatchAdded"));
                        }
                    } else if (meta.getDisplayName().equals(ChatColor.GOLD + "Add Chest")) {
                        if (meta.getLore().get(0).equals("dbd.chest")) {
                            this.game.getSpawnpoints().getChests().add(new Chest(new SpawnpointObject(event.getPlayer(), event.getClickedBlock())));
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.chestAdded"));
                        }
                    } else if (meta.getDisplayName().equals(ChatColor.GOLD + "Add Totem")) {
                        if (meta.getLore().get(0).equals("dbd.totem")) {
                            this.game.getSpawnpoints().getTotems().add(new Totem(new SpawnpointObject(event.getPlayer(), event.getClickedBlock())));
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.totemAdded"));
                        }
                    } else if (meta.getDisplayName().equals(ChatColor.GOLD + "Add Hook")) {
                        if (meta.getLore().get(0).equals("dbd.hook")) {
                            this.game.getSpawnpoints().getHooks().add(new Hook(new SpawnpointObject(event.getPlayer(), event.getClickedBlock())));
                            player.sendMessage(this.config.getString("messages.prefix") + this.config.getString("messages.hookAdded"));
                        }
                    }
                }
            }
        }
    }
}
