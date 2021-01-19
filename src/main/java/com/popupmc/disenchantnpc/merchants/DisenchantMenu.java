package com.popupmc.disenchantnpc.merchants;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.*;

// Right click with enchanted item or book
// This offers to trade enchants off into separate books
public class DisenchantMenu extends AbstractMerchantMenu {
    @Override
    void generateTrades(Player p, PlayerInventory inv, ItemStack item, List<MerchantRecipe> trades, Map<Enchantment, Integer> enchants) {

        // Block anything with no enchants
        if(enchants.size() <= 0)
            return;

        // No duplication of enchanted books with 1 enchant
        // Basically if you disenchant a book with one enchant you get a book duplication
        // With the second having no enchants which is unesesary
        if(item.getType() == Material.ENCHANTED_BOOK &&
        enchants.size() <= 1)
            return;

        // Go through all the enchants on the items
        for (Map.Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
            // Create enchanted book to hold enchant
            ItemStack resultingBook = new ItemStack(Material.ENCHANTED_BOOK);
            addEnchantToBook(resultingBook, enchant.getKey(), enchant.getValue());

            // Create enchanted item to hold all but this one enchant
            ItemStack resultingItem = item.clone();
            remEnchant(resultingItem, enchant.getKey());

            // Generate cost as a stack of currency
            ItemStack costStack = getCostStackFromLevel(enchant.getValue());

            // Create title from enchant we're moving off in the trade offer
            String title = enchantToTitle(enchant.getKey(), enchant.getValue());

            // Create disposable box containing the resulting book and item
            ItemStack disposableBox = createDisposableBox(title, Arrays.asList(
                    resultingBook,
                    resultingItem
            ));

            // Add trade
            addTrade(item, costStack, disposableBox, trades);
        }
    }
}
