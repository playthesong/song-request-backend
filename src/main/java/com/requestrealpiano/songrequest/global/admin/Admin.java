package com.requestrealpiano.songrequest.global.admin;

import org.springframework.stereotype.Component;

@Component
public class Admin {

    private boolean readyToLetter;

    public Admin() {
        this.readyToLetter = true;
    }

    public void changeReadyToLetter(boolean readyToLetter) {
        this.readyToLetter = readyToLetter;
    }

    public boolean isReadyToLetter() {
        return readyToLetter;
    }
}
