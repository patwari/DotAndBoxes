package com.mega.games.gamestartingkit.core.gameObjects.baseObjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.mega.games.gamestartingkit.core.dataLoaders.GameAssetManager;

public class Box extends GameObject {
    //Member variables
    private TextureAtlas.AtlasRegion currReg;

    public Box(float width, float height, Color color, boolean filled) {
        currReg = filled ? GameAssetManager.getInstance().square : GameAssetManager.getInstance().squareOutline;
        setSize(width, height);
        setColor(color);
    }

    public Box(float width, float height, Color color) {
        this(width, height, color, true);
    }

    public Vector2 getCenterPos() {
        return new Vector2(this.getPos().x + this.getSize().x / 2f, this.getPos().y + this.getSize().y / 2f);
    }

    public void setCenterPos(float x, float y) {
        this.setPos(x - this.getSize().x / 2f, y - this.getSize().y / 2f);
    }

    @Override
    public void onTouchDown(float x, float y) {

    }

    @Override
    public void onTouchUp(float x, float y) {

    }

    @Override
    public void onTouchDragged(float x, float y) {

    }

    @Override
    public void update(float dt) {
    }

    @Override
    public void draw(Batch batch) {
        batch.setColor(getColor());
        batch.draw(currReg, getPos().x, getPos().y, getSize().x, getSize().y);
        batch.setColor(new Color(1, 1, 1, 1));
    }
}
