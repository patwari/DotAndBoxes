package com.mega.games.gamestartingkit.core.gameObjects;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.GameObject;
import com.mega.games.gamestartingkit.core.gameObjects.entities.BGController;
import com.mega.games.gamestartingkit.core.gameObjects.entities.DotManager;
import com.mega.games.gamestartingkit.core.gameObjects.entities.PlayerScoreManager;
import com.mega.games.gamestartingkit.core.gameObjects.entities.StageObject;

import java.util.ArrayList;

public class GameObjectManager {
    //set singleton
    private static final GameObjectManager _myInstance = new GameObjectManager();
    private ArrayList<GameObject> objs;

    private GameObjectManager() {
        objs = new ArrayList<>();
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    public static GameObjectManager getInstance() {
        return _myInstance;
    }

    public void reset() {
        //on reset, clear the object list and just add a ball
        objs.clear();

        StageObject.getInstance().reset();
        DotManager.getInstance().reset();
        PlayerScoreManager.getInstance().reset();
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
            if (obj.visible) {
                obj.draw(batch);
            }
        }
        PlayerScoreManager.getInstance().draw(batch);
    }
}
