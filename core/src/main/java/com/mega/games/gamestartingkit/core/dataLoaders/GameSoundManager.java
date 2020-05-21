package com.mega.games.gamestartingkit.core.dataLoaders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class GameSoundManager {
    //set singleton
    private static final GameSoundManager _myInstance = new GameSoundManager();
    private AssetManager manager;
    //Music
    private Music bg;
    //Sfx
    private Sound tap;

    private GameSoundManager() {
    }

    public static GameSoundManager getInstance() {
        return _myInstance;
    }

    public void init(AssetManager manager) {
        this.manager = manager;

        loadAssets();

        manager.finishLoading();

        getAssets();
    }

    private void loadAssets() {
        manager.load("sounds/music_POC.mp3", Music.class);

        manager.load("sounds/tap.mp3", Sound.class);
    }

    private void getAssets() {
        bg = manager.get("sounds/music_POC.mp3", Music.class);
        bg.setVolume(0.3f);

        tap = manager.get("sounds/tap.mp3", Sound.class);
    }

    public void playBG() {
        bg.setLooping(true);
        bg.play();
    }

    public void stopBG() {
        bg.stop();
    }

    public void playTap() {
        tap.play();
    }
}
