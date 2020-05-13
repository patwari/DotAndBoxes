package com.mega.games.gamestartingkit.core.gameObjects.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mega.games.gamestartingkit.core.dataLoaders.GameAssetManager;
import com.mega.games.gamestartingkit.core.dataLoaders.GameData;

public class BGController {
    private Texture game_bg;
    private float x = 0f;
    private float y = 0f;
    private float width = 0f;
    private float height = 0f;

    public static final BGController _myInstance = new BGController();

    public static BGController getInstance() {
        return _myInstance;
    }

    BGController() {
        game_bg = GameAssetManager.getInstance().game_bg;
        resize();
    }

    /** call resize after every resize. */
    public void resize(){
        float targetScale = Math.max((float)GameData._virtualWidth / game_bg.getWidth(), (float)GameData._virtualHeight / game_bg.getHeight());
        this.width = targetScale * game_bg.getWidth();
        this.height = targetScale * game_bg.getHeight();

        this.x = (GameData._virtualWidth - this.width) / 2f;
        this.y = (GameData._virtualHeight - this.height) / 2f;
    }

    public void draw(Batch batch) {
        batch.draw(game_bg, this.x, this.y, this.width,this.height);
    }

}
