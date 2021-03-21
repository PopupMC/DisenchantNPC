package com.popupmc.disenchantnpc.merchants;

public class Freshener extends AbstractMerchant {
    public Freshener() {
        super(traitName, new FreshenerMenu());
    }

    public final static String traitName = "freshener";
}
