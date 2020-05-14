package com.mega.games.gamestartingkit.core.gameObjects.entities;

import com.mega.games.gamestartingkit.core.dataLoaders.Constants;
import com.mega.games.gamestartingkit.core.dataLoaders.GameData;
import com.mega.games.gamestartingkit.core.dataLoaders.Values;
import com.mega.games.gamestartingkit.core.gameObjects.GameObjectManager;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.Box;

import java.util.ArrayList;

public class DotManager {

    // set Singleton
    public static final DotManager _myInstance = new DotManager();
    public ArrayList<ArrayList<Dot>> dots;
    private Box outerBorder;
    private Box innerBorder;

    private DotManager() {
        dots = new ArrayList<>();
    }

    public static DotManager getInstance() {
        return _myInstance;
    }

    public void reset() {
        this.createBorders();
        this.populateDots();
    }

    protected void createBorders() {
        float limitDim = Math.min(GameData._virtualWidth, GameData._virtualHeight);
        outerBorder = new Box(limitDim, limitDim, Constants.BOARD_BORDER_COLOR, false);
        innerBorder = new Box(limitDim * 0.9f, limitDim * 0.9f, Constants.BOARD_BORDER_COLOR, false);
        outerBorder.setCenterPos(GameData._virtualWidth * 0.5f, GameData._virtualHeight * 0.5f);
        innerBorder.setCenterPos(GameData._virtualWidth * 0.5f, GameData._virtualHeight * 0.5f);
        outerBorder.interactive = false;
        innerBorder.interactive = false;
        GameObjectManager.getInstance().getObjs().add(outerBorder);
        GameObjectManager.getInstance().getObjs().add(innerBorder);
    }

    protected void populateDots() {
        dots.clear();
        dots = new ArrayList<>();

        Values.BOX_SIZE = (innerBorder.getSize().x - 2 * Constants.DOT_SIZE) / Math.max(Constants.NUM_COL, Constants.NUM_ROW);
        float offsetX = (GameData._virtualWidth - Constants.NUM_COL * Values.BOX_SIZE - Constants.DOT_SIZE * 2) * 0.5f;
        float offsetY = (GameData._virtualHeight - Constants.NUM_ROW * Values.BOX_SIZE - Constants.DOT_SIZE * 2) * 0.5f;

        for (int i = 0; i <= Constants.NUM_ROW; i++) {
            ArrayList<Dot> rowDots = new ArrayList<>();
            for (int j = 0; j <= Constants.NUM_COL; j++) {
                Dot tempDot = new Dot();
                rowDots.add(tempDot);
                tempDot.setPos(j * Values.BOX_SIZE + Constants.DOT_SIZE + offsetX, i * Values.BOX_SIZE + Constants.DOT_SIZE + offsetY);
                GameObjectManager.getInstance().getObjs().add(tempDot);
            }
            dots.add(rowDots);
        }
    }

}
