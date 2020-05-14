package com.mega.games.gamestartingkit.core.gameObjects.entities;

import com.mega.games.gamestartingkit.core.dataLoaders.Constants;
import com.mega.games.gamestartingkit.core.dataLoaders.GameData;
import com.mega.games.gamestartingkit.core.dataLoaders.Values;
import com.mega.games.gamestartingkit.core.gameObjects.GameObjectManager;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.Box;

import java.util.ArrayList;
import java.util.HashSet;

public class DotManager {

    // set Singleton
    public static final DotManager _myInstance = new DotManager();
    public ArrayList<ArrayList<Dot>> dots;
    private Box outerBorder;
    private Box innerBorder;
    private HashSet<String> edges;
    private DotIndex[] adjOffset = new DotIndex[]{
            new DotIndex(1, 0),     // top
            new DotIndex(0, 1),     // right
            new DotIndex(-1, 0),    // down
            new DotIndex(0, -1)     // left
    };

    private DotManager() {
    }

    public static DotManager getInstance() {
        return _myInstance;
    }

    public void reset() {
        this.createBorders();
        this.populateDots();
        if (this.edges != null) {
            this.edges.clear();
        }
        this.edges = new HashSet<>();
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
        if (dots != null) {
            dots.clear();
        }
        dots = new ArrayList<>();

        Values.BOX_SIZE = (innerBorder.getSize().x - 2 * Constants.DOT_SIZE) / Math.max(Constants.NUM_COL, Constants.NUM_ROW);
        float offsetX = (GameData._virtualWidth - Constants.NUM_COL * Values.BOX_SIZE - Constants.DOT_SIZE * 2) * 0.5f;
        float offsetY = (GameData._virtualHeight - Constants.NUM_ROW * Values.BOX_SIZE - Constants.DOT_SIZE * 2) * 0.5f;

        for (int i = 0; i <= Constants.NUM_ROW; i++) {
            ArrayList<Dot> rowDots = new ArrayList<>();
            for (int j = 0; j <= Constants.NUM_COL; j++) {
                Dot tempDot = new Dot();
                tempDot.setIndex(i, j);
                rowDots.add(tempDot);
                tempDot.setPos(j * Values.BOX_SIZE + Constants.DOT_SIZE + offsetX, i * Values.BOX_SIZE + Constants.DOT_SIZE + offsetY);
                GameObjectManager.getInstance().getObjs().add(tempDot);
            }
            dots.add(rowDots);
        }
    }

    public boolean isConnected(int row1, int col1, int row2, int col2) {
        return this.edges.contains(edgeString(row1, col1, row2, col2)) || this.edges.contains(edgeString(row2, col2, row1, col1));
    }

    public void connect(int row1, int col1, int row2, int col2) {
        this.edges.add(edgeString(row1, col1, row2, col2));
    }

    public void highlightAdjascentDots(DotIndex selectedIndex) {
        for (DotIndex offset : this.adjOffset) {
            DotIndex targetIndex = new DotIndex(selectedIndex.row + offset.row, selectedIndex.col + offset.col);
            if (targetIndex.row >= 0 && targetIndex.row <= Constants.NUM_ROW && targetIndex.col >= 0 && targetIndex.col <= Constants.NUM_COL) {
                if (!isConnected(selectedIndex.row, selectedIndex.col, targetIndex.row, targetIndex.col)) {
                    this.dots.get(targetIndex.row).get(targetIndex.col).highlight();
                }
            }
        }
    }

    public void unhighlightAdjascentDots(DotIndex selectedIndex) {
        for (DotIndex offset : this.adjOffset) {
            DotIndex targetIndex = new DotIndex(selectedIndex.row + offset.row, selectedIndex.col + offset.col);
            if (targetIndex.row >= 0 && targetIndex.row <= Constants.NUM_ROW && targetIndex.col >= 0 && targetIndex.col <= Constants.NUM_COL) {
                this.dots.get(targetIndex.row).get(targetIndex.col).unhighlight();
            }
        }
    }

    public String edgeString(int a, int b, int c, int d) {
        return a + "," + b + "," + c + "," + d;
    }

}
