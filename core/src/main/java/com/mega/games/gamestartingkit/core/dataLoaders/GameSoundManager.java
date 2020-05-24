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
    private boolean mute;

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
        unmute();
    }

    private void loadAssets() {
        manager.load("sounds/music.mp3", Music.class);
        manager.load("sounds/tap.mp3", Sound.class);
    }

    private void getAssets() {
        bg = manager.get("sounds/music.mp3", Music.class);
        tap = manager.get("sounds/tap.mp3", Sound.class);
    }

    public void playBG() {
        bg.setLooping(true);
        bg.play();
    }

    public void mute() {
        bg.setVolume(0);
        mute = true;
    }

    public void unmute() {
        bg.setVolume(0.3f);
        mute = false;
    }

    public boolean isMute() {
        return mute;
    }

    public void playTap() {
        tap.play(mute ? 0 : 1);
    }
}
