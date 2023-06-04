package org.rednero.deadbydaylight.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.rednero.deadbydaylight.utils.lists.SignList;

public class SupportSignBreak implements Listener {
    private final SignList signs;

    public SupportSignBreak(SignList signs) {
        this.signs = signs;
    }

    @EventHandler
    public void onSupportSignBreak(BlockPhysicsEvent event) {
        if (event.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) event.getBlock().getState();
            if (this.signs.isSign(sign)) {
                Block supportingBlock = event.getBlock().getRelative(((org.bukkit.material.Sign) sign.getData()).getAttachedFace());
                if (supportingBlock.getType() == Material.AIR) {
                    this.signs.removeSign(sign);
                }
            }
        }
    }
}
