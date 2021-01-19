package com.popupmc.disenchantnpc.boxes;

import com.popupmc.disenchantnpc.DisenchantNPCConfig;
import com.popupmc.disenchantnpc.util.InventoryUtils;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.List;

// A disposable box
public class OnDisposableBoxPlace extends OnAbstractBoxPlace {

    // Tell parent class should have 1 line of lore
    public OnDisposableBoxPlace() {
        super(1);
    }

    // Fairly simple, just unpack box and destroy
    public void onBlockPlace(BlockPlaceEvent e, Player p, ItemStack item, List<String> lore, BlockStateMeta boxMeta, ShulkerBox box, Inventory boxInv) {

        // Unpack Box
        boolean allUnpacked = unpackItems(e, boxMeta, box, boxInv);

        // If everything didnt get unpacked stop here with a warning
        if(!allUnpacked) {
            sendWarning(e.getPlayer(), "Warning: Not all items could be moved to your inventory. Please free up some space and try again.");
            return;
        }

        // Remove box
        removeBox(e);

        // Announce success
        sendSuccess(e.getPlayer(), "All items have been placed in your inventory!");
    }

    // Just make sure the first line is equal to what it is in the config
    public boolean verifyLore(List<String> lore) {
        return lore.get(0).equals(DisenchantNPCConfig.boxDisposableLore);
    }

    // Code to unpack the box
    public boolean unpackItems(BlockPlaceEvent e, BlockStateMeta boxMeta, ShulkerBox box, Inventory boxInv) {

        // Get size
        int boxInvSize = boxInv.getSize();

        // Pare to begin unpacking
        boolean completedUnpacking = true;

        for(int i = 0; i < boxInvSize; i++) {
            // Get item in box
            ItemStack item = boxInv.getItem(i);
            if(item == null)
                continue;

            // Check for free space and stop if not enough
            int freeSpace = InventoryUtils.invFreeSpace(e.getPlayer().getInventory(), item.getType(), item);
            if(freeSpace < item.getAmount()) {
                completedUnpacking = false;
                break;
            }

            // Add item and remove from box
            e.getPlayer().getInventory().addItem(item);
            boxInv.remove(item);
        }

        // Apply changes to box, this means if the box was partially unpacked
        // it remains partially unpacked
        boxMeta.setBlockState(box);
        e.getItemInHand().setItemMeta(boxMeta);

        // Return state of completion
        return completedUnpacking;
    }
}
