package com.mega.games.gamestartingkit.core.gameObjects.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mega.games.gamestartingkit.core.dataLoaders.Constants;
import com.mega.games.gamestartingkit.core.dataLoaders.GameAssetManager;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.Circle;

public class Dot extends Circle {
    private TextureAtlas.AtlasRegion border;

    public Dot() {
        super(Constants.DOT_SIZE, Constants.DOT_COLOR);
        border = GameAssetManager.getInstance().circleOutline;
    }

    @Override
    public void onTouchDown(float x, float y) {
        super.onTouchDown(x, y);
        if (this.isTouchWithin(x, y)) {
            this.setRadius(Constants.DOT_SIZE * 1.5f);
        }
    }

    protected boolean isTouchWithin(float x, float y) {
        return this.getPos().dst2(x, y) < Math.pow(this.getRadius(), 2);
    }

    @Override
    public void onTouchUp(float x, float y) {
        super.onTouchUp(x, y);
        this.setRadius(Constants.DOT_SIZE);
    }

    @Override
    public void onTouchDragged(float x, float y) {
        super.onTouchDragged(x, y);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

        batch.setColor(Constants.DOT_BORDER_COLOR);
        if (this.visible) {
            batch.draw(border, getPos().x - getRadius(), getPos().y - getRadius(), 2 * getRadius(), 2 * getRadius());
        }
        batch.setColor(new Color(1, 1, 1, 1));
    }

}
