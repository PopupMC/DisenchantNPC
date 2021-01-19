package com.popupmc.disenchantnpc.boxes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.util.List;

/*
 * To create a box of any purpose
 */
abstract public class OnAbstractBoxPlace implements Listener {

    // Takes an int to check for correct lore lines on behalf of the boxes
    public OnAbstractBoxPlace(int loreLines) {
        this.loreLines = loreLines;
    }

    // The block place event starts here which does some of the initial work and filtering
    @EventHandler
    public void onBlockPlacePre(BlockPlaceEvent e) {

        // Get item in hand (Why not just tell me the block that's being placed)
        ItemStack item = e.getItemInHand();

        // Make sure it's the correct material
        if(item.getType() != material)
            return;

        // Get lore
        List<String> lore = item.getLore();

        // Make sure there is lore
        if(lore == null)
            return;

        // Make sure it's of the right size exactly
        if(lore.size() != loreLines)
            return;

        // Call the sub-class and ask sub-class to do a manual verification
        if(!verifyLore(lore))
            return;

        // Get various information on it
        BlockStateMeta boxMeta = (BlockStateMeta) e.getItemInHand().getItemMeta();
        ShulkerBox box = (ShulkerBox) boxMeta.getBlockState();
        Inventory boxInv = box.getInventory();

        // Pass control to sub-class
        onBlockPlace(e, e.getPlayer(), item, lore, boxMeta, box, boxInv);

        // Set event to be canceled
        // We never want the block to be placed under any cirumstances
        e.setCancelled(true);
    }

    // Let this sub-class handle block place event, we just did some of the initial work
    abstract public void onBlockPlace(BlockPlaceEvent e, Player p, ItemStack item, List<String> lore, BlockStateMeta boxMeta, ShulkerBox box, Inventory boxInv);

    // Send quick colored messages to the player
    void sendWarning(Player player, String msg) {
        player.sendMessage(ChatColor.GOLD + msg);
    }
    void sendSuccess(Player player, String msg) {
        player.sendMessage(ChatColor.GREEN + msg);
    }
    void sendError(Player player, String msg) {player.sendMessage(ChatColor.RED + msg);}

    // Remove box from players hand
    void removeBox(BlockPlaceEvent e) {
        e.getPlayer().getInventory().remove(e.getItemInHand());
    }

    // A way for the sub-class to manually investogate the lore for accuracy
    abstract public boolean verifyLore(List<String> lore);

    // Lore lines for us to check for automatically & material it should be
    int loreLines;
    final static Material material = Material.BLACK_SHULKER_BOX;
}
