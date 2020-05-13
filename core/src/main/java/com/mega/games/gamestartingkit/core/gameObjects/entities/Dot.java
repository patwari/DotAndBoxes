package com.mega.games.gamestartingkit.core.gameObjects.entities;

import com.mega.games.gamestartingkit.core.dataLoaders.Constants;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.Circle;

public class Dot extends Circle {
    public Dot() {
        super(Constants.DOT_SIZE, Constants.DOT_COLOR);
    }
}
