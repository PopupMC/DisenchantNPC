package com.popupmc.disenchantnpc.merchants;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Removes an enchant level and gives you gold and emeralds in return
 */
public class EnchantDelevelerMenu extends AbstractMerchantMenu {
    void generateTrades(Player p, PlayerInventory inv, ItemStack item, List<MerchantRecipe> trades, Map<Enchantment, Integer> enchants) {

        // Block anything that's not an enchant book or has no enchants
        if(item.getType() != Material.ENCHANTED_BOOK || enchants.size() <= 0)
            return;

        // Create resulting book with decremented levels
        ItemStack resultingBook = item.clone();

        // Payout
        int payoutLower = 0;
        int payoutNormal = 0;

        // Does the new book contain at least 1 enchant after de-leveling
        // If not the player doesn't get a book back
        boolean atLeast1Enchant = false;

        // Loop through enchants
        for (Map.Entry<Enchantment, Integer> enchant : enchants.entrySet()) {

            // Remove enchant from book
            remEnchant(resultingBook, enchant.getKey());

            // Calculate lower level
            int newLevel = enchant.getValue() - 1;

            // Emeralds?
            // Only if original enchant level was 3 or higher
            boolean useEmeralds = enchant.getValue() >= 3;

            // Re-add back only if level is above 0
            if(newLevel > 0) {
                addEnchant(resultingBook, enchant.getKey(), newLevel);
                atLeast1Enchant = true;
            }

            // Increment price accordingly
            if(useEmeralds)
                payoutNormal += 1;
            else
                payoutLower += 1;
        }

        // Get stack of payout money
        ItemStack priceStack = getCostStack(payoutNormal);
        ItemStack lowerPriceStack = getLowerCostStack(payoutLower);

        // Add items to box
        ArrayList<ItemStack> boxItems = new ArrayList<>();

        // Add resulting book if it contains at least 1 enchant
        if(atLeast1Enchant)
            boxItems.add(resultingBook);

        // Add normal money if there's any
        if(payoutNormal > 0)
            boxItems.add(priceStack);

        // Add lower money if there's any
        if(payoutLower > 0)
            boxItems.add(lowerPriceStack);

        // Create box to contain the 1-3 items
        ItemStack resultingBox = createDisposableBox("Deleveled Result", boxItems);

        // Add Recipe
        addTrade(item, null, resultingBox, trades);
    }
}
