package com.popupmc.disenchantnpc.merchants;

import com.popupmc.disenchantnpc.DisenchantNPCConfig;
import com.popupmc.disenchantnpc.util.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.*;

// Base class for a custom trade menu
abstract class AbstractMerchantMenu {
    // Called automatically by AbstractMerchant to do the work
    // in creating and opening the menu
    void openMenu(Player p, String name) {
        // Create fictional merchant
        Merchant merchant = Bukkit.createMerchant(name);

        // Prepare a trade list
        List<MerchantRecipe> trades = new ArrayList<>();

        // Generate trades
        generateTradesPre(p, trades);

        // apply trades to merchant:
        merchant.setRecipes(trades);

        // open trading window to player from fictional merchant we setup
        p.openMerchant(merchant, true);
    }

    // This handles all the usual stuff common to all menus
    void generateTradesPre(Player p, List<MerchantRecipe> trades) {

        // Get the player and object player is holding, if a stack only get a single one
        // Don't offer trades for the whole stack
        PlayerInventory inv = p.getInventory();
        ItemStack item = inv.getItemInMainHand().asOne();

        // Stop if hand is empty, apparently nobody knows how to check that though
        // but this seems to work
        if(item.getAmount() == 0 || item.getType() == Material.AIR)
            return;

        // Get all enchants on item
        // Not all sub-classes need this but it's there
        Map<Enchantment, Integer> enchants = getEnchants(item);

        // Pass control to the sub-class
        generateTrades(p, inv, item, trades, enchants);
    }

    // Sub-classes implement this
    abstract void generateTrades(Player p, PlayerInventory inv, ItemStack item, List<MerchantRecipe> trades, Map<Enchantment, Integer> enchants);

    // Adds a trade option to the list of trades
    void addTrade(ItemStack tradeItem1, ItemStack tradeItem2, ItemStack result, List<MerchantRecipe> trades) {

        // A single trade with absurdly high use count
        // We do this in attempt to make a trade unlimited if the player wishes
        // and has inventory to do so
        MerchantRecipe trade = new MerchantRecipe(result, 10000);

        // Add 1 or 2 ingridients if they are present
        if(tradeItem1 != null)
            trade.addIngredient(tradeItem1);

        if(tradeItem2 != null)
            trade.addIngredient(tradeItem2);

        // Add trade to list of trades
        trades.add(trade);
    }

    // Creates a disposable box containing items
    ItemStack createDisposableBox(String name, List<ItemStack> items) {
        // Creates a black shulker box and gets it's contents which is a tad complicated
        ItemStack boxItem = new ItemStack(Material.BLACK_SHULKER_BOX);
        BlockStateMeta boxMeta = (BlockStateMeta) boxItem.getItemMeta();
        ShulkerBox box = (ShulkerBox) boxMeta.getBlockState();
        Inventory boxInv = box.getInventory();

        // Add Items to it
        for(ItemStack item : items) {
            boxInv.addItem(item);
        }

        // Set it's name
        box.setCustomName(name);
        boxMeta.setDisplayName(name);

        // Set the lore to Disposable which makes it act disposable
        List<String> lore = Collections.singletonList(DisenchantNPCConfig.boxDisposableLore);

        // Apply changes
        boxMeta.setBlockState(box);
        boxItem.setItemMeta(boxMeta);
        boxItem.setLore(lore);

        // Return item
        return boxItem;
    }

    // Creates a locked box containing items
    ItemStack createLockedBox(String name, List<ItemStack> items, int price) {
        // Create shulker box and gets it's contents which is a tad complicated
        ItemStack boxItem = new ItemStack(Material.BLACK_SHULKER_BOX);
        BlockStateMeta boxMeta = (BlockStateMeta) boxItem.getItemMeta();
        ShulkerBox box = (ShulkerBox) boxMeta.getBlockState();
        Inventory boxInv = box.getInventory();

        // Add Items
        for(ItemStack item : items) {
            boxInv.addItem(item);
        }

        // Set name
        box.setCustomName(name);
        boxMeta.setDisplayName(name);

        // Add lore that makes it "Locked" and cost money
        List<String> lore = new ArrayList<>();
        lore.add(DisenchantNPCConfig.boxLockedLore);
        lore.add("Cost: " + price);

        // Apply changes
        boxMeta.setBlockState(box);
        boxItem.setItemMeta(boxMeta);
        boxItem.setLore(lore);

        // Return item
        return boxItem;
    }

    // Generate an enchant name
    // This converts something like fire_aspect, 5
    // to "Fire Aspect 5"
    // At this point I'm not going to worry about roman numerals
    String enchantToTitle(Enchantment enchant, int level) {
        String name = enchant.getKey().getKey();
        name = name.replace('_', ' ');
        return TextUtils.convertToTitleCaseSplitting(name) + " " + level;
    }

    // This returns however many physical coins you want
    ItemStack getCostStack(int amt) {
        ItemStack costStack = DisenchantNPCConfig.currency.asOne();
        costStack.setAmount(amt);
        return costStack;
    }

    // Also returns physical coins, just lower currency
    ItemStack getLowerCostStack(int amt) {
        ItemStack costStack = DisenchantNPCConfig.lowCurrency.asOne();
        costStack.setAmount(amt);
        return costStack;
    }

    // This returns coins but auto calculated based on level
    ItemStack getCostStackFromLevel(int level) {
        // Generate cost as a stack of currency
        int cost = level * DisenchantNPCConfig.pricePerLevel;
        return getCostStack(cost);
    }

    /*
     * Enchanted books enchant very differently than normal items
     * Normal items you simply add the enchant to list of enchants all items have
     * For enchanted books you have to write/read the enchants inside of it
     */

    // This gets all the enchants from a book
    Map<Enchantment,Integer> getEnchantsFromBook(ItemStack book) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
        return meta.getStoredEnchants();
    }

    // Adds enchants to book
    void addEnchantToBook(ItemStack book, Enchantment enchant, int level) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
        meta.addStoredEnchant(enchant, level, true);
        book.setItemMeta(meta);
    }

    // Remove enchant from inside book
    void remEnchantFromBook(ItemStack book, Enchantment enchant) {
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta)book.getItemMeta();
        meta.removeStoredEnchant(enchant);
        book.setItemMeta(meta);
    }

    /*
     * The same way for working with enchants but to everything else besides enchanted books
     */

    // Enchants from non-books
    Map<Enchantment,Integer> getEnchantsFromNonBook(ItemStack item) {
        return item.getEnchantments();
    }

    // Enchant to non-book
    void addEnchantToNonBook(ItemStack item, Enchantment enchant, int level) {
        item.addUnsafeEnchantment(enchant, level);
    }

    // Remove enchant from non-book
    void remEnchantFromNonBook(ItemStack item, Enchantment enchant) {
        item.removeEnchantment(enchant);
    }

    /*
     * Since we have 2 different ways of working with enchants, this handles that automatically
     * for you.
     */

    // Get enchant no matter item
    Map<Enchantment,Integer> getEnchants(ItemStack item) {
        if(item.getType() == Material.ENCHANTED_BOOK)
            return getEnchantsFromBook(item);

        return getEnchantsFromNonBook(item);
    }

    // Add enchant no matter item
    void addEnchant(ItemStack item, Enchantment enchant, int level) {
        if(item.getType() == Material.ENCHANTED_BOOK) {
            addEnchantToBook(item, enchant, level);
            return;
        }

        addEnchantToNonBook(item, enchant, level);
    }

    // Remove enchant no matter item
    void remEnchant(ItemStack item, Enchantment enchant) {
        if(item.getType() == Material.ENCHANTED_BOOK) {
            remEnchantFromBook(item, enchant);
            return;
        }

        remEnchantFromNonBook(item, enchant);
    }

    // This calculates an enchant book price based on configuration
    // by multiplying the price per level by all the enchants in the book
    int getPriceFromBook(ItemStack book) {
        Map<Enchantment, Integer> enchants = getEnchantsFromBook(book);

        int ret = 0;
        for (Map.Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
            ret += DisenchantNPCConfig.pricePerLevel * enchant.getValue();
        }

        return ret;
    }

    // Applies all enchants from a book to an item
    void applyAllEnchantsFromBook(ItemStack book, ItemStack item) {
        Map<Enchantment, Integer> enchants = getEnchantsFromBook(book);

        for (Map.Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
            addEnchant(item, enchant.getKey(), enchant.getValue());
        }
    }
}
