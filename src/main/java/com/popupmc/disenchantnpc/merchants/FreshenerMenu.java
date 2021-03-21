package com.popupmc.disenchantnpc.merchants;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantRecipe;
import org.bukkit.inventory.PlayerInventory;

import java.util.List;
import java.util.Map;

public class FreshenerMenu  extends AbstractMerchantMenu {
    @Override
    void generateTrades(Player p, PlayerInventory inv, ItemStack item, List<MerchantRecipe> trades, Map<Enchantment, Integer> enchants) {
        // Block anything that's not an enchant book or has no enchants
        if(item.getType() != Material.ENCHANTED_BOOK || enchants.size() <= 0)
            return;

        // Create resulting book
        ItemStack resultingBook = new ItemStack(Material.ENCHANTED_BOOK);

        for (Map.Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
            addEnchant(resultingBook, enchant.getKey(), enchant.getValue());
        }

        // Add trade
        addTrade(item, null, resultingBook, trades);
    }
}
