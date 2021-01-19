package com.popupmc.disenchantnpc.merchants;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.*;

/**
 * Give an item and enchantment book, get box requires money to open
 */

public class EnchanterMenu extends AbstractMerchantMenu {
    void generateTrades(Player p, PlayerInventory inv, ItemStack item, List<MerchantRecipe> trades, Map<Enchantment, Integer> enchants) {
        // Prepare to get a list of all books in the players inventory except the one the player is holding
        ArrayList<ItemStack> books = new ArrayList<>();

        // Keeps track of whether or not we removed the book in hand already
        // If there's no book in hand then there's nothing to worry about
        boolean removedBookInHand = false;

        // Add all enchanted books with enchantments
        for(int i = 0; i < inv.getSize(); i++) {
            ItemStack invSlot = inv.getItem(i);

            // Must be a valid item
            if (invSlot == null)
                continue;

            // Must be an enchanted item
            if (invSlot.getType() != Material.ENCHANTED_BOOK)
                continue;

            // Must have 1 enchant
            Map<Enchantment, Integer> invSlotEnchants = getEnchants(invSlot);
            if(invSlotEnchants.size() <= 0)
                continue;

            // Can't already be in list
            if(books.contains(invSlot.asOne()))
                continue;

            // Can't the include the item in hand
            if(invSlot.isSimilar(item) &&
            !removedBookInHand) {
                removedBookInHand = true;
                continue;
            }

            // Add book found in inventory
            books.add(invSlot.asOne());
        }

        // Create recipes from the different books
        for (ItemStack book : books) {

            // Calculate price of this book
            int price = getPriceFromBook(book);

            // Create result item
            ItemStack resultingItem = item.clone();
            applyAllEnchantsFromBook(book, resultingItem);

            // Add it to a locked box
            ItemStack resultingBox = createLockedBox("Enchanted Item", Collections.singletonList(resultingItem), price);

            // Add trade
            addTrade(item, book, resultingBox, trades);
        }
    }
}
