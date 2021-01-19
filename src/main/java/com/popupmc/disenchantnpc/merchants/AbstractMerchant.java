package com.popupmc.disenchantnpc.merchants;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.trait.Trait;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

// Base class for NPC Merchants that have a right-clickable trade menu
public abstract class AbstractMerchant extends Trait {

    public AbstractMerchant(String traitName, AbstractMerchantMenu menu) {
        // Send Citizens the name of the trait
        // Save the associated menu
        super(traitName);
        this.menu = menu;
    }

    // Handle NPC Right Clicks, literally the same for all merchants
    @EventHandler
    public void click(NPCRightClickEvent event){
        // Make sure it's this NPC
        // The Citizens Team emphasizes the importance of this
        if(event.getNPC() != this.getNPC())
            return;

        // Get Player
        Player p = event.getClicker();

        // Open Menu
        menu.openMenu(p, this.getNPC().getName());
    }

    // These are unused but Citizens seems to imply their needed
    // Called every tick
    @Override
    public void run() {
    }

    @Override
    public void onAttach() {

    }

    @Override
    public void onDespawn() {
    }

    @Override
    public void onSpawn() {

    }

    @Override
    public void onRemove() {
    }

    // The right click menu to open
    AbstractMerchantMenu menu;
}
