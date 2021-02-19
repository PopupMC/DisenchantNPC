package com.popupmc.disenchantnpc;

import com.popupmc.disenchantnpc.boxes.OnDisposableBoxPlace;
import com.popupmc.disenchantnpc.boxes.OnLockedBoxPlace;
import com.popupmc.disenchantnpc.merchants.*;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.trait.TraitInfo;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class DisenchantNPC extends JavaPlugin {
    @Override
    public void onEnable() {

        // Save this plugin instance
        plugin = this;

        // Load Config
        DisenchantNPCConfig.load();

        // Register Box Place Events to make them work as expected
        Bukkit.getPluginManager().registerEvents(new OnDisposableBoxPlace(), this);
        Bukkit.getPluginManager().registerEvents(new OnLockedBoxPlace(), this);
        Objects.requireNonNull(this.getCommand("disenchanter")).setExecutor(new OnCommand());

        // Setup Citizens
        // This can only work on server start unfortunately meaning updates can't be applied while server is running
        if(!setupCitizens()) {
            getLogger().severe(String.format("[%s] - Disabled due to Citizens 2.0 not found or not enabled!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Announce enabled
        getLogger().info("DisenchantNPC is enabled.");
    }

    // Announce disabled
    @Override
    public void onDisable() {
        getLogger().info("DisenchantNPC is disabled");
    }

    // Connect to Citizens and register NPC traits with it
    private boolean setupCitizens() {
        // This code is provided directly by the Citizens team
        // as the recccomended way to hook into citizens and check for errors
        if(getServer().getPluginManager().getPlugin("Citizens") == null ||
                !Objects.requireNonNull(getServer().getPluginManager().getPlugin("Citizens")).isEnabled()) {
            return false;
        }

        // Register traits with Citizens
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(Disenchanter.class).withName(Disenchanter.traitName));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(Enchanter.class).withName(Enchanter.traitName));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(EnchantLeveler.class).withName(EnchantLeveler.traitName));
        CitizensAPI.getTraitFactory().registerTrait(TraitInfo.create(EnchantDeleveler.class).withName(EnchantDeleveler.traitName));

        return true;
    }

    // Plugin
    static JavaPlugin plugin;
}
