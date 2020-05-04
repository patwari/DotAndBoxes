package com.mega.games.gamestartingkit.core.dataLoaders;

public class WorldData {

    //set singleton
    private static final WorldData _myInstance = new WorldData();

    private WorldData() {
    }

    public static WorldData getInstance() {
        return _myInstance;
    }

    public void setDefault() {
        //Rarely filled
    }
}
