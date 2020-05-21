package com.mega.games.gamestartingkit.core.gameObjects.entities;

import com.mega.games.gamestartingkit.core.dataLoaders.Constants;
import com.mega.games.gamestartingkit.core.dataLoaders.GameData;
import com.mega.games.gamestartingkit.core.dataLoaders.GameSoundManager;
import com.mega.games.gamestartingkit.core.dataLoaders.Values;
import com.mega.games.gamestartingkit.core.gameObjects.GameObjectManager;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.Box;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * This class controls all Dots on the grid.
 */
public class DotManager {

    // set Singleton
    public static final DotManager _myInstance = new DotManager();
    public ArrayList<ArrayList<Dot>> dots;
    private Box outerBorder;
    private Box innerBorder;
    private HashSet<String> edges;
    private DotIndex beginDot;
    private DotIndex endDot;
    private DotIndex snapDot;

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

    public void checkBeginDot(DotIndex idx) {
        // check if any any edges is not connected.
        boolean isBeginDot = false;
        for (DotIndex adjDots : this.getAllAdjacentDots(idx)) {
            if (!isConnected(idx.row, idx.col, adjDots.row, adjDots.col)) {
                this.dots.get(adjDots.row).get(adjDots.col).highlight();
                isBeginDot = true;
            }
        }
        if (isBeginDot) {
            this.beginDot = idx;
            Dot targetDot = this.dots.get(idx.row).get(idx.col);
            targetDot.markAsBeginDot();
            StageObject.getInstance().onSetBeginDot(targetDot.getPos());
        }
    }

    public void setSnapDot(DotIndex idx) {
        if (beginDot != null) {
            if (snapDot != idx) {
                GameSoundManager.getInstance().playTap();
                snapDot = idx;
            }
            Dot targetDot = this.dots.get(idx.row).get(idx.col);
            StageObject.getInstance().onSetSnapDot(targetDot.getPos());
        }
    }

    public void resetDots() {
        // TODO: check for boxComplete. If yes, mark the box.
        if (beginDot != null) {
            for (DotIndex adjDots : this.getAllAdjacentDots(beginDot)) {
                this.dots.get(adjDots.row).get(adjDots.col).reset();
            }
            this.dots.get(beginDot.row).get(beginDot.col).reset();
            beginDot = null;
        }
    }

    public ArrayList<DotIndex> getAllAdjacentDots(DotIndex idx) {
        ArrayList<DotIndex> adjDots = new ArrayList<>();
        for (DotIndex offset : this.adjOffset) {
            DotIndex targetIndex = new DotIndex(idx.row + offset.row, idx.col + offset.col);
            if (isValid(targetIndex)) {
                adjDots.add(targetIndex);
            }
        }
        return adjDots;
    }

    public Box getInnerBorder() {
        return innerBorder;
    }

    public boolean isValid(DotIndex idx) {
        return idx.row >= 0 && idx.row <= Constants.NUM_ROW && idx.col >= 0 && idx.col <= Constants.NUM_COL;
    }

    public String edgeString(int a, int b, int c, int d) {
        return a + "," + b + "," + c + "," + d;
    }

}
