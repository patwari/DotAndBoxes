package com.mega.games.gamestartingkit.core.dataLoaders;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.mega.games.support.MegaServices;

public class GameAssetManager {

    //set Singleton
    public static final GameAssetManager _myInstance = new GameAssetManager();
    //Score Assets
    public int scoreFontSize;
    public Label.LabelStyle scoreLabelStyle;
    //Image
    public AtlasRegion circle;
    public AtlasRegion circleOutline;
    public AtlasRegion square;
    public AtlasRegion squareOutline;
    public Texture game_bg;
    public Skin glassySkin;
    private AssetManager manager;
    private TextureAtlas atlas;
    private TextureAtlas dinoAtlas;

    private GameAssetManager() {
    }

    public static GameAssetManager getInstance() {
        return _myInstance;
    }

    public void init(AssetManager manager) {
        this.manager = manager;

        //que assets for loading
        loadAssets();

        //force load all assets right now
        manager.finishLoading();

        //get all assets
        getAssets();
    }

    private void loadAssets() {
        //load texture atlas
        manager.load("texAtlas.txt", TextureAtlas.class);
        manager.load("Animations/dino.txt", TextureAtlas.class);
        manager.load("game_bg.jpg", Texture.class);
        manager.load("lineGuide.png", Texture.class);
        manager.load("edge_ring.png", Texture.class);
        manager.load("playerIcon.png", Texture.class);
        manager.load("playerBG.png", Texture.class);
        manager.load("Skin/glassy-ui.json", Skin.class);

        //load font data
        FreetypeFontLoader.FreeTypeFontLoaderParameter scoreFontLoader = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        scoreFontLoader.fontFileName = MegaServices.FONT_GOTHAMROUNDED_MEDIUM;
        scoreFontLoader.fontParameters.minFilter = Texture.TextureFilter.Linear;
        scoreFontLoader.fontParameters.magFilter = Texture.TextureFilter.Linear;

        scoreFontLoader.fontParameters.color = new Color(255 / 255f, 255 / 255f, 255 / 255f, 1);
        scoreFontSize = 16;
        scoreFontLoader.fontParameters.size = scoreFontSize;
        manager.load(MegaServices.FONT_GOTHAMROUNDED_MEDIUM, BitmapFont.class, scoreFontLoader);
    }

    private void getAssets() {
        //Font
        scoreLabelStyle = new Label.LabelStyle();
        scoreLabelStyle.font = manager.get(MegaServices.FONT_GOTHAMROUNDED_MEDIUM, BitmapFont.class);
        scoreLabelStyle.fontColor = Color.BLACK;

        //Textures
        atlas = manager.get("texAtlas.txt", TextureAtlas.class);
        dinoAtlas = manager.get("Animations/dino.txt", TextureAtlas.class);
        game_bg = manager.get("game_bg.jpg", Texture.class);

        //particles
        circle = atlas.findRegion("circle_fill");
        circleOutline = atlas.findRegion("circle_line");
        square = atlas.findRegion("square_fill");
        squareOutline = atlas.findRegion("square_line");
        glassySkin = manager.get("Skin/glassy-ui.json", Skin.class);

        Array<TextureAtlas.AtlasRegion> reg = dinoAtlas.getRegions();
    }

    public Texture getTexture(String name) {
        return manager.get(name, Texture.class);
    }

    public Label.LabelStyle getLabelStyle(String font, Color color) {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = manager.get(font, BitmapFont.class);
        style.fontColor = color;
        return style;
    }

    public Array<AtlasRegion> getDinoTextures() {
        String[] parts = new String[]{"Idle", "Walk", "Run", "Jump"};
        Array<AtlasRegion> allTextures = new Array<>();
        for (String action : parts) {
            allTextures.addAll(dinoAtlas.findRegions(action));
        }
        return allTextures;
    }
}
