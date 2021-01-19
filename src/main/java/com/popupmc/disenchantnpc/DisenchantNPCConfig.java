package com.popupmc.disenchantnpc;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

public class DisenchantNPCConfig {
    static void load() {
        // Create if doesnt exist
        DisenchantNPC.plugin.saveDefaultConfig();

        // Load config file
        FileConfiguration config = DisenchantNPC.plugin.getConfig();

        // Read config.yml in resources for better explanations

        // Price per enchant level to calculate pricing or payouts
        pricePerLevel = config.getInt("price-per-level", 3);

        // Currency
        currency = new ItemStack(Material.valueOf(config.getString("currency", "EMERALD")));

        // Low currency for smaller payouts
        lowCurrency = new ItemStack(Material.valueOf(config.getString("low-currency", "GOLD_INGOT")));

        // Lores for different boxes
        boxDisposableLore = config.getString("boxes.disposable-lore", "Disposable Box");
        boxLockedLore = config.getString("boxes.locked-lore", "Locked! Needs money to unlock!");
    }

    public static int pricePerLevel;
    public static ItemStack currency;
    public static ItemStack lowCurrency;
    public static String boxDisposableLore;
    public static String boxLockedLore;
}
