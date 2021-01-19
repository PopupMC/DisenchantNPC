package com.popupmc.disenchantnpc.merchants;

// Disenchanter NPC
public class Disenchanter extends AbstractMerchant {
    public Disenchanter() {
        // NPC Trait Name & it's Menu class that will render the menu
        super(traitName, new DisenchantMenu());
    }

    public final static String traitName = "disenchanter";
}
