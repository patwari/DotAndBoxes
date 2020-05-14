package com.mega.games.gamestartingkit.core.gameObjects.entities;

/**
 * This class indicates the position of the a dot in the dotGrid.
 * Bottom-left dot = [0,0]
 * Top-left dot = [ROW_LIM,0]
 * Bottom-Right dot = [0, COL_LIM]
 */
public class DotIndex {
    int row;
    int col;

    public DotIndex(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public DotIndex() {
        new DotIndex(0, 0);
    }

}
