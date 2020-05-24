package com.mega.games.gamestartingkit.core.dataLoaders;

import com.badlogic.gdx.math.Vector2;

public class GameData {
    //set singleton
    private static final GameData _myInstance = new GameData();
    public static boolean _debugMode = true;
    //Basic Game Data
    private static int _screenFactor = 40;
    public static int _virtualWidth = 9 * _screenFactor;
    public static int _virtualHeight = 16 * _screenFactor;
    public boolean isGameEnded;
    public boolean isGameOver;
    public float gameEndLag;
    public float elapsed;
    public int[] scores;
    //Game Specific Data (Mostly Config Driven)
    public Vector2 screenCenter = new Vector2(_virtualWidth / 2f, _virtualHeight / 2f);
    public int currPlayerIdx;

    private GameData() {
    }

    public static GameData getInstance() {
        return _myInstance;
    }
}
