package com.popupmc.disenchantnpc.merchants;

// Enchanter
public class Enchanter extends AbstractMerchant {
    public Enchanter() {
        super(traitName, new EnchanterMenu());
    }

    public final static String traitName = "enchanter";
}
