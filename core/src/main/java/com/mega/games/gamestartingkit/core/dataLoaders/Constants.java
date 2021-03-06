package com.mega.games.gamestartingkit.core.dataLoaders;

import com.badlogic.gdx.graphics.Color;

public class Constants {
    // always keep NUM_ROW <= NUM_COL
    public static int NUM_ROW = 3;
    public static int NUM_COL = 4;

    public static float DOT_SIZE = 15f;
    public static float BEGIN_DOT_SIZE = 22.5f;
    public static float ADJ_DOT_SIZE = 20.5f;
    public static Color DOT_COLOR = Color.WHITE;
    public static float DOT_SNAP_TOL = 15f;

    public static Color DOT_BORDER_COLOR = Color.RED;
    public static Color BOARD_BORDER_COLOR = Color.DARK_GRAY;
    public static Color DOT_BORDER_HIGHLIGHT_COLOR = new Color(0xCC00CC);
    public static float[] DOT_BORDER_HIGHLIGHT_SIZE_F = new float[]{0.9f, 1.1f};
    public static float DOT_BORDER_SCALE_SPEED_F = 0.6f;

    public static float EDGES_ALPHA = 0.6f;
    public static float EDGE_SIZE_F = 1;

    // max 4 players supported.
    public static int PLAYERS_COUNT = 4;
    public static Color[] PLAYER_COLORS = new Color[]{Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW};
    public static float PLAYER_ICON_H = 40;
    public static float PLAYER_ICON_BG_H = 60;
}
