package com.mega.games.gamestartingkit.core.gameObjects.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.mega.games.gamestartingkit.core.dataLoaders.Constants;
import com.mega.games.gamestartingkit.core.dataLoaders.GameAssetManager;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.Circle;

public class Dot extends Circle {
    private TextureAtlas.AtlasRegion border;
    /**
     * the index position of this dot in the dotManager's grid.
     */
    private DotIndex index;
    private boolean isHighlighted = false;
    private boolean pendingUnhighlight = false;

    public Dot() {
        super(Constants.DOT_SIZE, Constants.DOT_COLOR);
        border = GameAssetManager.getInstance().circleOutline;
    }

    @Override
    public void onTouchDown(float x, float y) {
        super.onTouchDown(x, y);
        if (this.isTouchWithin(x, y)) {
            this.setRadius(Constants.DOT_SIZE * 1.5f);
            DotManager.getInstance().highlightAdjascentDots(this.getIndex());
            pendingUnhighlight = true;
        }
    }

    protected boolean isTouchWithin(float x, float y) {
        return this.getPos().dst2(x, y) < Math.pow(this.getRadius(), 2);
    }

    @Override
    public void onTouchUp(float x, float y) {
        super.onTouchUp(x, y);
        this.setRadius(Constants.DOT_SIZE);
        if (pendingUnhighlight) {
            DotManager.getInstance().unhighlightAdjascentDots(this.getIndex());
            this.pendingUnhighlight = false;
        }
    }

    @Override
    public void onTouchDragged(float x, float y) {
        super.onTouchDragged(x, y);
    }

    public void highlight() {
        this.setRadius(Constants.DOT_SIZE * 1.25f);
        this.isHighlighted = true;
    }

    public void unhighlight() {
        this.setRadius(Constants.DOT_SIZE);
        this.isHighlighted = false;
    }

    public DotIndex getIndex() {
        return this.index;
    }

    public void setIndex(int row, int col) {
        this.index = new DotIndex(row, col);
    }

    @Override
    public void draw(Batch batch) {
        super.draw(batch);

        batch.setColor(this.isHighlighted ? Constants.DOT_BORDER_HIGHLIGHT_COLOR : Constants.DOT_BORDER_COLOR);
        if (this.visible) {
            batch.draw(border, getPos().x - getRadius(), getPos().y - getRadius(), 2 * getRadius(), 2 * getRadius());
        }
        batch.setColor(new Color(1, 1, 1, 1));
    }

}
