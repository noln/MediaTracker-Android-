package com.example.mediatracker20.adapters;

public class Saver {
    static boolean isChanged;
    private static Saver saver;

    private Saver() {
        isChanged = false;
    }

    public void appChanged() {
        isChanged = true;
    }

    public void saved() {
        isChanged = false;
    }

    public static Saver getInstance() {
        if(saver == null) {
            saver = new Saver();
        }
        return saver;
    }

    public boolean isChanged() {
        return isChanged;
    }
}
