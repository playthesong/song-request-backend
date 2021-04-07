package com.requestrealpiano.songrequest.global.admin;

import org.springframework.stereotype.Component;

@Component
public class Admin {

    private boolean readyToLetter;

    public Admin() {
        this.readyToLetter = true;
    }

    public boolean changeReadyToLetter(boolean readyToLetter) {
        this.readyToLetter = readyToLetter;
        return this.readyToLetter;
    }

    public boolean isReadyToLetter() {
        return readyToLetter;
    }
}
