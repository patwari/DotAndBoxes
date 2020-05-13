package com.mega.games.gamestartingkit.core.gameObjects.entities;

import com.mega.games.gamestartingkit.core.dataLoaders.Constants;
import com.mega.games.gamestartingkit.core.dataLoaders.GameData;
import com.mega.games.gamestartingkit.core.dataLoaders.Values;
import com.mega.games.gamestartingkit.core.gameObjects.GameObjectManager;

import java.util.ArrayList;

public class DotManager {

    // set Singleton
    public static final DotManager _myInstance = new DotManager();
    public ArrayList<ArrayList<Dot>> dots;

    private DotManager() {
        dots = new ArrayList<>();
    }

    public static DotManager getInstance() {
        return _myInstance;
    }

    public void reset() {
        dots.clear();
        dots = new ArrayList<ArrayList<Dot>>();

        float limitDim = Math.min(GameData._virtualWidth, GameData._virtualHeight);
        Values.BOX_SIZE = (limitDim - 2 * Constants.DOT_SIZE) / Math.max(Constants.NUM_COL, Constants.NUM_ROW);
        float offsetX = (GameData._virtualWidth - Constants.NUM_COL * Values.BOX_SIZE - Constants.DOT_SIZE * 2) / 2;
        float offsetY = (GameData._virtualHeight - Constants.NUM_ROW * Values.BOX_SIZE - Constants.DOT_SIZE * 2) / 2;

        for (int i = 0; i <= Constants.NUM_ROW; i++) {
            ArrayList<Dot> rowDots = new ArrayList<Dot>();
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
