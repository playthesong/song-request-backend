package com.requestrealpiano.songrequest.domain.product;

public enum ProductType {
    MUSIC_SHEET,
    PIANO
    ;

    public String getKey() {
        return name();
    }
}
