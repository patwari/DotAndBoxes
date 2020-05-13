package com.mega.games.gamestartingkit.core.gameObjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mega.games.gamestartingkit.core.dataLoaders.GameAssetManager;
import com.mega.games.gamestartingkit.core.dataLoaders.GameData;
import com.mega.games.gamestartingkit.core.gameObjects.entities.BGController;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.GameObject;

import java.util.ArrayList;

public class GameObjectManager {
    //set singleton
    private static final GameObjectManager _myInstance = new GameObjectManager();
    private ArrayList<GameObject> objs;

    private GameObjectManager() {
        objs = new ArrayList<>();
    }

    public static GameObjectManager getInstance() {
        return _myInstance;
    }

    public void reset() {
        //on reset, clear the object list and just add a ball
        objs.clear();
    }

    public ArrayList<GameObject> getObjs() {
        return objs;
    }

    public void update(float dt) {
        //Automatically called every frame before draw function
        for (GameObject obj : objs) {
            obj.update(dt);
        }
    }

    public void draw(Batch batch) {
        BGController.getInstance().draw(batch);
        //automatically called every frame after update function
        for (GameObject obj : objs) {
            obj.draw(batch);
        }
    }
}
