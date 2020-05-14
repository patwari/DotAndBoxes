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
    private float borderRadF = 1;
    private boolean isBorderIncrease = false;

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

    @Override
    public void update(float dt) {
        super.update(dt);

        if (isHighlighted) {
            this.borderRadF += Constants.DOT_BORDER_SCALE_SPEED_F * dt * (isBorderIncrease ? 1 : -1);
            if (this.borderRadF >= Constants.DOT_BORDER_HIGHLIGHT_SIZE_F[1]) {
                this.isBorderIncrease = false;
            } else if (this.borderRadF <= Constants.DOT_BORDER_HIGHLIGHT_SIZE_F[0]) {
                this.isBorderIncrease = true;
            }
        }
    }

    public void highlight() {
        this.setRadius(Constants.DOT_SIZE * 1.25f);
        borderRadF = 1;
        this.isHighlighted = true;
    }

    public void unhighlight() {
        this.setRadius(Constants.DOT_SIZE);
        borderRadF = 1;
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

        if (this.visible) {
            batch.setColor(this.isHighlighted ? Constants.DOT_BORDER_HIGHLIGHT_COLOR : Constants.DOT_BORDER_COLOR);
            float currBorderRad = getRadius() * borderRadF;
            batch.draw(border, getPos().x - currBorderRad, getPos().y - currBorderRad, 2 * currBorderRad, 2 * currBorderRad);
            batch.setColor(new Color(1, 1, 1, 1));
        }
    }

}
