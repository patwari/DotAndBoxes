package com.mega.games.gamestartingkit.core.gameObjects.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.mega.games.gamestartingkit.core.dataLoaders.Constants;
import com.mega.games.gamestartingkit.core.dataLoaders.GameAssetManager;
import com.mega.games.gamestartingkit.core.dataLoaders.GameData;
import com.mega.games.support.MegaServices;

import java.util.ArrayList;

public class PlayerScoreManager {
    // set Singleton
    public static final PlayerScoreManager _myInstance = new PlayerScoreManager();
    private ArrayList<Image> iconsBG;
    private ArrayList<Image> playerIcons;
    private ArrayList<Label> scoreLabels;

    private PlayerScoreManager() {
        iconsBG = new ArrayList<>();
        playerIcons = new ArrayList<>();
        scoreLabels = new ArrayList<>();
    }

    public static PlayerScoreManager GetInstance() {
        return _myInstance;
    }

    public void reset() {
        createElements();
    }

    protected void createElements() {
        iconsBG.clear();
        playerIcons.clear();
        scoreLabels.clear();
        iconsBG = new ArrayList<>();
        playerIcons = new ArrayList<>();
        scoreLabels = new ArrayList<>();

        Texture iconBGTexture = GameAssetManager.getInstance().getTexture("playerBG.png");
        Texture iconTexture = GameAssetManager.getInstance().getTexture("playerIcon.png");
        Label.LabelStyle labelStyle = GameAssetManager.getInstance().getLabelStyle(MegaServices.FONT_GOTHAMROUNDED_MEDIUM, Color.WHITE);

        float iconBGW = Constants.PLAYER_ICON_BG_H / iconBGTexture.getHeight() * iconBGTexture.getWidth();
        float iconW = iconTexture.getWidth() <= iconTexture.getHeight() ? Constants.PLAYER_ICON_H : Constants.PLAYER_ICON_H / iconTexture.getHeight() * iconTexture.getWidth();
        float iconH = iconTexture.getWidth() <= iconTexture.getHeight() ? Constants.PLAYER_ICON_H / iconTexture.getWidth() * iconTexture.getHeight() : Constants.PLAYER_ICON_H;
        float labelW = iconW * 1.2f;

        float topLimitY = DotManager.getInstance().getOuterBorder().getPos().y - Constants.PLAYER_ICON_BG_H;
        if ((Constants.PLAYERS_COUNT - 1) / 2 > 0)
            // for total 3 or 4 players, shift up.
            topLimitY += Constants.PLAYER_ICON_BG_H / 2f;

        for (int i = 0; i < Constants.PLAYERS_COUNT; i++) {
            Image iconBG = new Image(iconBGTexture);
            Image icon = new Image(iconTexture);
            Label label = new Label("5", labelStyle);

            iconBG.setOriginY(Constants.PLAYER_ICON_BG_H / 2);
            iconBG.setColor(Constants.PLAYER_COLORS[i]);
            iconBG.setSize(iconBGW, Constants.PLAYER_ICON_BG_H);
            icon.setSize(iconW, iconH);
            label.setSize(labelW, iconH);

            iconBG.setPosition(i % 2 == 0 ? 0 : GameData._virtualWidth, 0);
            if (i % 2 == 0) {
                // left side
                float midY = topLimitY - (i / 2 * Constants.PLAYER_ICON_BG_H);
                iconBG.setPosition(0, midY - Constants.PLAYER_ICON_BG_H / 2f);
                icon.setPosition(0, midY, Align.left);
                label.setAlignment(Align.left, Align.left);
                label.setPosition(labelW, midY, Align.left);
            } else {
                float midY = topLimitY - ((i - 1) / 2 * Constants.PLAYER_ICON_BG_H);
                iconBG.setRotation(180);
                iconBG.setPosition(GameData._virtualWidth, midY - Constants.PLAYER_ICON_BG_H / 2f);
                icon.setPosition(GameData._virtualWidth, midY, Align.right);
                label.setAlignment(Align.right, Align.right);
                label.setPosition(GameData._virtualWidth - labelW, midY, Align.right);
            }

            iconsBG.add(iconBG);
            playerIcons.add(icon);
            scoreLabels.add(label);
        }
    }


    public void draw(Batch batch) {
        for (Image img : iconsBG) {
            img.draw(batch, 1);
        }
        for (Image img : playerIcons) {
            img.draw(batch, 1);
        }
        for (Label lbl : scoreLabels) {
            lbl.draw(batch, 1);
        }
    }

}
