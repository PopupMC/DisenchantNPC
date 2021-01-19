package com.popupmc.disenchantnpc.merchants;

// Enchant De-Leveler
public class EnchantDeleveler extends AbstractMerchant {
    public EnchantDeleveler() {
        super(traitName, new EnchantDelevelerMenu());
    }

    public final static String traitName = "enchant-deleveler";
}
