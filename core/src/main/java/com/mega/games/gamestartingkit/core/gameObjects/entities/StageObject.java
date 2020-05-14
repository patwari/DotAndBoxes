package com.mega.games.gamestartingkit.core.gameObjects.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.GameObject;

/**
 * This is not a visible object. It is used to listen to input on entire screen.
 */
public class StageObject extends GameObject {

    // set Singleton
    public static final StageObject _myInstance = new StageObject();

    private StageObject() {
    }

    public static StageObject getInstance() {
        return _myInstance;
    }

    public void reset() {
        interactive = true;
    }

    @Override
    public void onTouchDown(float x, float y) {

    }

    @Override
    public void onTouchUp(float x, float y) {
        // TODO: check if he game continues.
        DotManager.getInstance().resetDots();
    }

    @Override
    public void onTouchDragged(float x, float y) {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw(Batch batch) {

    }
}
