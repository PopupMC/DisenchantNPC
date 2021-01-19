package com.popupmc.disenchantnpc.merchants;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Levels up enchants by 1 given 2 identical books
 */
public class EnchantLevelerMenu extends AbstractMerchantMenu {
    void generateTrades(Player p, PlayerInventory inv, ItemStack item, List<MerchantRecipe> trades, Map<Enchantment, Integer> enchants) {
        // Block anything that's not an enchant book or has no enchants
        if(item.getType() != Material.ENCHANTED_BOOK || enchants.size() <= 0)
            return;

        // If it's a book that contains a fortune 4 or above enchant then block
        if(enchants.containsKey(Enchantment.LOOT_BONUS_BLOCKS) &&
        enchants.get(Enchantment.LOOT_BONUS_BLOCKS) >= 4)
            return;

        // Create resulting book with incremented levels
        ItemStack resultingBook = item.clone();

        for (Map.Entry<Enchantment, Integer> enchant : enchants.entrySet()) {

            remEnchant(resultingBook, enchant.getKey());
            addEnchant(resultingBook, enchant.getKey(), enchant.getValue() + 1);
        }

        // Calculate price
        int price = getPriceFromBook(resultingBook);

        // Locked Box
        ItemStack resultingItem = createLockedBox("Enchanted Item", Collections.singletonList(resultingBook), price);

        // Add trade
        addTrade(item, item, resultingItem, trades);
    }
}
