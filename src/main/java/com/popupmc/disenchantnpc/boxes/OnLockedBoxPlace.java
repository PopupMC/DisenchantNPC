package com.popupmc.disenchantnpc.boxes;

import com.popupmc.disenchantnpc.DisenchantNPCConfig;
import com.popupmc.disenchantnpc.util.InventoryUtils;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.Collections;
import java.util.List;

public class OnLockedBoxPlace extends OnAbstractBoxPlace {

    // A locked box has 2 lore lines, have it auto-check for that
    public OnLockedBoxPlace() {
        super(2);
    }

    // Just pay the fee and unlock is all it does
    @Override
    public void onBlockPlace(BlockPlaceEvent e, Player p, ItemStack item, List<String> lore, BlockStateMeta boxMeta, ShulkerBox box, Inventory boxInv) {
        // Get Cost from lore line #2
        Integer cost = getCost(p, lore.get(1));

        // Charge money if cost is parsable & player has enough money
        if(cost != null) {
            if(hasEnoughMoney(p, cost))
                chargeMoney(p, cost);
            else
                return;
        }

        // Unlock and notify
        unlockBox(item);
        sendSuccess(p, "Fee paid & box unlocked! You may have to right-click again to open.");
    }

    // Verifies lines 1 & 2 are how they should appear
    @Override
    public boolean verifyLore(List<String> lore) {
        return lore.get(0).equals(DisenchantNPCConfig.boxLockedLore) &&
                lore.get(1).startsWith("Cost: ");
    }

    // Parses cost from the lore
    // "Cost: 123"
    // Returns null if not parsable
    public Integer getCost(Player p, String line) {
        int cost;

        // Attempt to parse cost
        try {
            cost = Integer.parseInt(line.substring(6));
        }
        catch (NumberFormatException ex) {
            // Not able to parse, return null with error
            sendError(p, "Error: Unable to parse cost from lore, proceeding anyways without charge...");
            return null;
        }

        // Parsed, return cost
        return cost;
    }

    // Calculate if player has enough physical money which is a bit complicated
    public boolean hasEnoughMoney(Player p, int cost) {
        if(!p.getInventory().containsAtLeast(DisenchantNPCConfig.currency.asOne(), cost)) {
            sendWarning(p, "You need " + cost + " emerald(s) to open this box!");
            return false;
        }

        return true;
    }

    // Remove physical money from player
    public void chargeMoney(Player p, int amount) {
        InventoryUtils.invRemoveSpace(p.getInventory(), DisenchantNPCConfig.currency.asOne(), amount);
    }

    // Basically make it disposable instead of locked, has same effect of "Unlocking it"
    public void unlockBox(ItemStack item) {
        item.setLore(Collections.singletonList(DisenchantNPCConfig.boxDisposableLore));
    }
}
