package com.mega.games.gamestartingkit.core.gameObjects.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.mega.games.gamestartingkit.core.dataLoaders.Constants;
import com.mega.games.gamestartingkit.core.dataLoaders.GameAssetManager;
import com.mega.games.gamestartingkit.core.dataLoaders.GameDataController;
import com.mega.games.gamestartingkit.core.dataLoaders.GameSoundManager;
import com.mega.games.gamestartingkit.core.gameObjects.GameObjectManager;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.Box;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.GameObject;

import java.util.ArrayList;

/**
 * This is an object that holds game generic elements, which aren't part of any specific group.
 */
public class StageObject extends GameObject {

    // set Singleton
    public static final StageObject _myInstance = new StageObject();
    private Image lineGuide;
    private Vector2 beginDotPos;
    private ArrayList<Image> edgeImages;
    private ArrayList<float[]> boxColors;
    private TextureAtlas.AtlasRegion boxTexture;

    /**
     * lineGuide should be larger than the begin dot, by a factor of x1.5
     */
    private float lineGuideScaleF = 1.5f;

    private StageObject() {
        edgeImages = new ArrayList<>();
        boxColors = new ArrayList<>();
    }

    public static StageObject getInstance() {
        return _myInstance;
    }

    public void reset() {
        interactive = true;
        edgeImages.clear();
        edgeImages = new ArrayList<>();
        boxColors.clear();
        boxColors = new ArrayList<>();
        boxTexture = GameAssetManager.getInstance().square;

        initLineGuide();
        GameSoundManager.getInstance().playBG();
        GameObjectManager.getInstance().getObjs().add(StageObject.getInstance());
    }

    public void initLineGuide() {
        if (lineGuide != null)
            lineGuide.clear();

        Texture circle = GameAssetManager.getInstance().getTexture("lineGuide.png");
        int offset = 5;
        NinePatch roundRect = new NinePatch(circle, circle.getWidth() / 2 - offset, circle.getHeight() / 2 - offset, circle.getWidth() / 2 - offset, circle.getHeight() / 2 - offset);
        roundRect.scale(Constants.BEGIN_DOT_SIZE * 2 * lineGuideScaleF / roundRect.getTotalWidth(), Constants.BEGIN_DOT_SIZE * 2 * lineGuideScaleF / roundRect.getTotalHeight());
        lineGuide = new Image(roundRect);
        lineGuide.setVisible(false);
    }

    @Override
    public void onTouchDown(float x, float y) {

    }

    @Override
    public void onTouchUp(float x, float y) {
        if (beginDotPos != null) {
            DotManager.getInstance().checkEndDot(x, y);
            resetDots();
        }
    }

    @Override
    public void onTouchDragged(float x, float y) {
        if (beginDotPos != null) {
            float lineGuideBaseRad = Constants.BEGIN_DOT_SIZE * lineGuideScaleF;
            Vector2 newTouch = limitTouchToBorder(x, y, lineGuideBaseRad);

            if (lineGuide.isVisible() && beginDotPos != null) {
                if (DotManager.getInstance().getSnapDot() != null) {
                    if (!Dot.Contains(DotManager.getInstance().getSnapDot(), x, y, Constants.DOT_SNAP_TOL)) {
                        // if the pointer has moved far enough since last snap, then reset the snapDot, and re-position lineGuide using touchPos.
                        DotManager.getInstance().resetSnapDot();
                        lineGuide.setWidth(beginDotPos.dst(newTouch) + lineGuideBaseRad * 2);
                        lineGuide.setRotation(MathUtils.radiansToDegrees * MathUtils.atan2(newTouch.y - beginDotPos.y, newTouch.x - beginDotPos.x));
                    }
                } else {
                    // not snapped, so re-position lineGuide using touchPos.
                    lineGuide.setWidth(beginDotPos.dst(newTouch) + lineGuideBaseRad * 2);
                    lineGuide.setRotation(MathUtils.radiansToDegrees * MathUtils.atan2(newTouch.y - beginDotPos.y, newTouch.x - beginDotPos.x));
                }
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

    public void resetDots() {
        DotManager.getInstance().resetDots();
        lineGuide.setVisible(false);
        beginDotPos = null;
    }

    public void drawEdges(float x1, float y1, float x2, float y2) {
        Texture edge = GameAssetManager.getInstance().getTexture("edge_ring.png");
        int offset = 5;
        NinePatch edgePatch = new NinePatch(edge, edge.getWidth() / 2 - offset, edge.getHeight() / 2 - offset, edge.getWidth() / 2 - offset, edge.getHeight() / 2 - offset);
        edgePatch.scale(Constants.DOT_SIZE * 2 * Constants.EDGE_SIZE_F / edgePatch.getTotalWidth(), Constants.DOT_SIZE * 2 / edgePatch.getTotalHeight());
        Image tempEdge = new Image(edgePatch);
        tempEdge.setPosition(x1, y1, Align.center);
        tempEdge.setOrigin(Align.center);
        tempEdge.setWidth(Vector2.dst(x1, y1, x2, y2) + Constants.DOT_SIZE * 2 * Constants.EDGE_SIZE_F);
        tempEdge.setRotation(MathUtils.radiansToDegrees * MathUtils.atan2(y2 - y1, x2 - x1));
        tempEdge.setColor(Constants.PLAYER_COLORS[GameDataController.getInstance().getCurrPlayerIndex()]);
        edgeImages.add(tempEdge);
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

    public void colorBox(float x, float y, float w, float h, int playerIndex) {
        boxColors.add(new float[]{x, y, w, h, playerIndex});
    }


    @Override
    public void update(float dt) {

    }

    @Override
    public void draw(Batch batch) {
        for (float[] params : boxColors) {
            batch.setColor(Constants.PLAYER_COLORS[(int) params[4]]);
            batch.draw(boxTexture, params[0], params[1], params[2], params[3]);
        }
        batch.setColor(new Color(1, 1, 1, 1));
        for (Image img : edgeImages) {
            img.draw(batch, Constants.EDGES_ALPHA);
        }
        if (lineGuide.isVisible()) {
            lineGuide.draw(batch, 1);
        }
    }
}
