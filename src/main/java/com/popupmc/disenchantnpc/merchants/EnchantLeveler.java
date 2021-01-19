package com.popupmc.disenchantnpc.merchants;

// Enchant Leveler
public class EnchantLeveler extends AbstractMerchant {
    public EnchantLeveler() {
        super(traitName, new EnchantLevelerMenu());
    }

    public final static String traitName = "enchant-leveler";
}
