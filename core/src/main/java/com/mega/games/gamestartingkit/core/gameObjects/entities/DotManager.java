package com.mega.games.gamestartingkit.core.gameObjects.entities;

import com.badlogic.gdx.math.Vector2;
import com.mega.games.gamestartingkit.core.dataLoaders.Constants;
import com.mega.games.gamestartingkit.core.dataLoaders.GameData;
import com.mega.games.gamestartingkit.core.dataLoaders.GameDataController;
import com.mega.games.gamestartingkit.core.dataLoaders.GameSoundManager;
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
    protected float boxSize;
    private Box outerBorder;
    private Box innerBorder;
    private HashSet<String> edges;
    private boolean[][] isBoxColored;
    private DotIndex beginDot;
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
        createBorders();
        populateDots();
        if (edges != null) {
            edges.clear();
        }
        edges = new HashSet<>();
        isBoxColored = new boolean[Constants.NUM_ROW][Constants.NUM_COL];
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

        boxSize = (innerBorder.getSize().x - 2 * Constants.DOT_SIZE) / Math.max(Constants.NUM_COL, Constants.NUM_ROW);
        float offsetX = (GameData._virtualWidth - Constants.NUM_COL * boxSize - Constants.DOT_SIZE * 2) * 0.5f;
        float offsetY = (GameData._virtualHeight - Constants.NUM_ROW * boxSize - Constants.DOT_SIZE * 2) * 0.5f;

        for (int i = 0; i <= Constants.NUM_ROW; i++) {
            ArrayList<Dot> rowDots = new ArrayList<>();
            for (int j = 0; j <= Constants.NUM_COL; j++) {
                Dot tempDot = new Dot();
                tempDot.setIndex(i, j);
                rowDots.add(tempDot);
                tempDot.setPos(j * boxSize + Constants.DOT_SIZE + offsetX, i * boxSize + Constants.DOT_SIZE + offsetY);
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

    public void connect(DotIndex begin, DotIndex end) {
        connect(begin.row, begin.col, end.row, end.col);
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

    public void resetDots() {
        if (beginDot != null) {
            for (DotIndex adjDots : this.getAllAdjacentDots(beginDot)) {
                this.dots.get(adjDots.row).get(adjDots.col).reset();
            }
            this.dots.get(beginDot.row).get(beginDot.col).reset();
        }
        beginDot = null;
        snapDot = null;
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

    public void checkEndDot(float pointerX, float pointerY) {
        if (beginDot != null && snapDot != null) {
            if (Dot.Contains(dots.get(snapDot.row).get(snapDot.col), pointerX, pointerY, Constants.DOT_SNAP_TOL)) {
                connect(beginDot, snapDot);
                Vector2 pos1 = dots.get(beginDot.row).get(beginDot.col).getPos();
                Vector2 pos2 = dots.get(snapDot.row).get(snapDot.col).getPos();
                StageObject.getInstance().drawEdges(pos1.x, pos1.y, pos2.x, pos2.y);
                checkForWin(beginDot, snapDot);
            }
        }
        resetDots();
    }

    // TODO: check for win.
    public void checkForWin(DotIndex beginDot, DotIndex endDot) {
        DotIndex firstBoxIdx = new DotIndex((beginDot.row + endDot.row) / 2, (beginDot.col + endDot.col) / 2);
        DotIndex secondBoxIdx = new DotIndex(firstBoxIdx.row, firstBoxIdx.col);
        if ((beginDot.row == endDot.row)) {
            secondBoxIdx.row--;
        } else {
            secondBoxIdx.col--;
        }
        boolean anyBoxComplete = false;
        if (checkBoxComplete(firstBoxIdx)) {
            anyBoxComplete = true;
        }
        if (checkBoxComplete(secondBoxIdx)) {
            anyBoxComplete = true;
        }

        if (!anyBoxComplete) {
            GameDataController.getInstance().setToNextPlayer();
        }
    }

    private boolean checkBoxComplete(DotIndex boxIdx) {
        if (isValidBoxIdx(boxIdx)) {
            if (isBoxComplete(boxIdx) && !isBoxColored[boxIdx.row][boxIdx.col]) {
                Dot leftDownDot = dots.get(boxIdx.row).get(boxIdx.col);
                StageObject.getInstance().colorBox(leftDownDot.getPos().x, leftDownDot.getPos().y, boxSize, boxSize, GameDataController.getInstance().getCurrPlayerIndex());
                isBoxColored[boxIdx.row][boxIdx.col] = true;
                GameDataController.getInstance().addScoreToCurrPlayer();
                return true;
            }
        }
        return false;
    }

    private boolean isBoxComplete(DotIndex firstBoxIdx) {
        int row = firstBoxIdx.row, col = firstBoxIdx.col;
        if (!isConnected(row, col, row + 1, col)) {  // left side.
            return false;
        }
        if (!isConnected(row, col, row, col + 1)) {   // bottom side.
            return false;
        }
        if (!isConnected(row + 1, col, row + 1, col + 1)) {   // top side.
            return false;
        }
        // right side.
        return isConnected(row, col + 1, row + 1, col + 1);
    }

    public Box getInnerBorder() {
        return innerBorder;
    }

    public Box getOuterBorder() {
        return outerBorder;
    }

    public Dot getSnapDot() {
        if (snapDot == null)
            return null;
        return dots.get(snapDot.row).get(snapDot.col);
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

    public void resetSnapDot() {
        snapDot = null;
    }

    public boolean isValid(DotIndex idx) {
        return idx.row >= 0 && idx.row <= Constants.NUM_ROW && idx.col >= 0 && idx.col <= Constants.NUM_COL;
    }

    public boolean isValidBoxIdx(DotIndex idx) {
        return idx.row >= 0 && idx.row < Constants.NUM_ROW && idx.col >= 0 && idx.col < Constants.NUM_COL;
    }

    public String edgeString(int a, int b, int c, int d) {
        return a + "," + b + "," + c + "," + d;
    }

}
