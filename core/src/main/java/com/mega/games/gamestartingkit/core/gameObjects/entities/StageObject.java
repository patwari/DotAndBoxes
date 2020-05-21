package com.mega.games.gamestartingkit.core.gameObjects.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.mega.games.gamestartingkit.core.dataLoaders.Constants;
import com.mega.games.gamestartingkit.core.dataLoaders.GameAssetManager;
import com.mega.games.gamestartingkit.core.gameObjects.GameObjectManager;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.Box;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.GameObject;

/**
 * This is not a visible object. It is used to listen to input on entire screen.
 */
public class StageObject extends GameObject {

    // set Singleton
    public static final StageObject _myInstance = new StageObject();
    private Image lineGuide;
    private Vector2 beginDotPos;
    /**
     * lineGuide should be larger than the begin dot, by a factor of x1.5
     */
    private float lineGuideScaleF = 1.5f;

    private StageObject() {
    }

    public static StageObject getInstance() {
        return _myInstance;
    }

    public void reset() {
        interactive = true;
        if (lineGuide != null) {
            lineGuide.clear();
        }
        Texture circle = GameAssetManager.getInstance().getTexture("lineGuide.png");
        int offset = 5;
        NinePatch roundRect = new NinePatch(circle, circle.getWidth() / 2 - offset, circle.getHeight() / 2 - offset, circle.getWidth() / 2 - offset, circle.getHeight() / 2 - offset);
        roundRect.scale(Constants.BEGIN_DOT_SIZE * 2 * lineGuideScaleF / roundRect.getTotalWidth(), Constants.BEGIN_DOT_SIZE * 2 * lineGuideScaleF / roundRect.getTotalHeight());
        lineGuide = new Image(roundRect);
        lineGuide.setVisible(false);
        GameObjectManager.getInstance().getObjs().add(StageObject.getInstance());
    }

    @Override
    public void onTouchDown(float x, float y) {

    }

    @Override
    public void onTouchUp(float x, float y) {
        if (beginDotPos != null) {
            onInvalidEdge();
        }
    }

    @Override
    public void onTouchDragged(float x, float y) {
        if (beginDotPos != null) {
            float lineGuideBaseRad = Constants.BEGIN_DOT_SIZE * lineGuideScaleF;
            Vector2 newTouch = limitTouchToBorder(x, y, lineGuideBaseRad);

            if (lineGuide.isVisible() && beginDotPos != null) {
                lineGuide.setWidth(beginDotPos.dst(newTouch) + lineGuideBaseRad * 2);
                lineGuide.setRotation(MathUtils.radiansToDegrees * MathUtils.atan2(newTouch.y - beginDotPos.y, newTouch.x - beginDotPos.x));
            }
        }
    }

    public void onSetBeginDot(Vector2 pos) {
        beginDotPos = pos;

        lineGuide.setVisible(true);
        lineGuide.setSize(Constants.BEGIN_DOT_SIZE * 2 * lineGuideScaleF, Constants.BEGIN_DOT_SIZE * 2 * lineGuideScaleF);
        lineGuide.setPosition(beginDotPos.x, beginDotPos.y, Align.center);
        lineGuide.setOrigin(Align.center);
    }

    public void onSetSnapDot(Vector2 pos) {
        lineGuide.setWidth(beginDotPos.dst(pos.x, pos.y) + Constants.BEGIN_DOT_SIZE * 2 * lineGuideScaleF);
        lineGuide.setRotation(MathUtils.radiansToDegrees * MathUtils.atan2(pos.y - beginDotPos.y, pos.x - beginDotPos.x));
    }

    public void onInvalidEdge() {
        DotManager.getInstance().resetDots();
        lineGuide.setVisible(false);
        beginDotPos = null;
    }

    /**
     * Limits touch such that the lineGuide never goes out of grid.
     */
    private Vector2 limitTouchToBorder(float x, float y, float tol) {
        Vector2 newTouchPos = new Vector2();
        Box innerBorder = DotManager.getInstance().getInnerBorder();
        if (x < innerBorder.getPos().x + tol) {
            newTouchPos.x = tol + innerBorder.getPos().x;
        } else if (x > innerBorder.getPos().x + innerBorder.getSize().x - tol) {
            newTouchPos.x = -tol + innerBorder.getPos().x + innerBorder.getSize().x;
        } else {
            newTouchPos.x = x;
        }
        if (y < innerBorder.getPos().y + tol) {
            newTouchPos.y = tol + innerBorder.getPos().y;
        } else if (y > innerBorder.getPos().y + innerBorder.getSize().y - tol) {
            newTouchPos.y = -tol + innerBorder.getPos().y + innerBorder.getSize().y;
        } else {
            newTouchPos.y = y;
        }
        return newTouchPos;
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void draw(Batch batch) {
        if (lineGuide.isVisible()) {
            lineGuide.draw(batch, 1);
        }
    }
}
