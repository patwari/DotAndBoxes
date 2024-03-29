package com.mega.games.gamestartingkit.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.mega.games.gamestartingkit.core.dataLoaders.Constants;
import com.mega.games.gamestartingkit.core.dataLoaders.GameAssetManager;
import com.mega.games.gamestartingkit.core.dataLoaders.GameData;
import com.mega.games.gamestartingkit.core.dataLoaders.GameDataController;
import com.mega.games.gamestartingkit.core.dataLoaders.GameInfra;
import com.mega.games.gamestartingkit.core.dataLoaders.GameSoundManager;
import com.mega.games.gamestartingkit.core.dataLoaders.WorldData;
import com.mega.games.gamestartingkit.core.gameObjects.GameObjectManager;
import com.mega.games.gamestartingkit.core.gameObjects.baseObjects.GameObject;
import com.mega.games.gamestartingkit.core.widgets.DebugRenderer;
import com.mega.games.gamestartingkit.core.widgets.HUD;

public class PlayScreen implements Screen {
    public boolean valuesInitialized;
    private boolean configSelected;
    /*
    first frame takes time to load due to async asset loading. This causes
    a large update delta, so skip it.
    */
    private boolean skipFrame = true;
    private float timeSinceGameEnd;
    private DebugRenderer debugRenderer;
    private HUD hud;
    private Stage stage;
    private ImageButton soundButton;


    public PlayScreen() {
        valuesInitialized = false;
        configSelected = false;
        debugRenderer = new DebugRenderer();
        hud = new HUD();

        stage = new Stage(GameInfra.getInstance().viewport, GameInfra.getInstance().batch);
        Gdx.input.setInputProcessor(stage);
        addUIListeners();
        GameDataController.getInstance().startGame();

        initConfigScreen();
        initSoundButton();
    }

    private void initSoundButton() {
        Drawable soundOnDrawable = new TextureRegionDrawable(GameAssetManager.getInstance().soundOn);
        Drawable soundOffDrawable = new TextureRegionDrawable(GameAssetManager.getInstance().soundOff);
        soundButton = new ImageButton(soundOnDrawable, soundOnDrawable, soundOffDrawable);
        soundButton.setPosition(GameData._virtualWidth, GameData._virtualHeight, Align.topRight);
        stage.addActor(soundButton);
    }

    /**
     * Manually handle the sound button pressup event. There is some issues when InputHandler is attached directly to the soundButton.
     */
    public void checkSoundButtonClick(float x, float y) {
        if (x >= soundButton.getX() && x <= soundButton.getX() + soundButton.getWidth() && y >= soundButton.getY() && y <= soundButton.getY() + soundButton.getHeight()) {
            if (soundButton.isChecked()) {
                GameSoundManager.getInstance().mute();
            } else {
                GameSoundManager.getInstance().unmute();
            }
        }
    }

    public void initConfigScreen() {
        Skin skin = GameAssetManager.getInstance().glassySkin;
        Dialog dialog = new Dialog("Settings", skin) {
            @Override
            public void result(Object obj) {
                Array<Cell> cells = getContentTable().getCells();
                int tableRows = getContentTable().getRows();
                int tableCols = getContentTable().getColumns();

                for (int i = 0; i < tableRows; i++) {
                    Cell cell = cells.get(i * tableCols + 1);
                    int val = ((SelectBox<Integer>) cell.getActor()).getSelected().intValue();
                    switch (cell.getActor().getName()) {
                        case "players":
                            Constants.PLAYERS_COUNT = val;
                            break;
                        case "rows":
                            Constants.NUM_ROW = val;
                            break;
                        case "cols":
                            Constants.NUM_COL = val;
                            break;
                    }
                }
                configSelected = true;
            }
        };
        float w = 300;
        float h = 500;
        dialog.setSize(w, h);
        dialog.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 100);
        dialog.setScale(200f / w);

        final SelectBox<Integer> playersCountBox = new SelectBox<>(skin);
        playersCountBox.setName("players");
        playersCountBox.setSelected(2);
        playersCountBox.setItems(2, 3, 4);

        final SelectBox<Integer> numRowsBox = new SelectBox<>(skin);
        numRowsBox.setName("rows");
        numRowsBox.setSelected(3);
        numRowsBox.setItems(3, 4, 5, 6);

        final SelectBox<Integer> numColsBox = new SelectBox<>(skin);
        numColsBox.setName("cols");
        numColsBox.setSelected(3);
        numColsBox.setItems(3, 4, 5, 6);

        dialog.getContentTable().defaults().pad(10);
        dialog.getContentTable().add("Players", "black");
        dialog.getContentTable().add(playersCountBox);
        dialog.getContentTable().row();
        dialog.getContentTable().add("Rows", "black");
        dialog.getContentTable().add(numRowsBox);
        dialog.getContentTable().row();
        dialog.getContentTable().add("Columns", "black");
        dialog.getContentTable().add(numColsBox);

        dialog.button("Ok", "OK");

        stage.addActor(dialog);
    }

    private void addUIListeners() {
        stage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //Handle touch down input
                if (GameObjectManager.getInstance().getObjs() != null) {
                    for (GameObject obj : GameObjectManager.getInstance().getObjs()) {
                        if (obj.interactive) {
                            obj.onTouchDown(x, y);
                        }
                    }
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                //Handle touch up input
                if (GameObjectManager.getInstance().getObjs() != null) {
                    for (GameObject obj : GameObjectManager.getInstance().getObjs()) {
                        if (obj.interactive) {
                            obj.onTouchUp(x, y);
                        }
                    }
                }

                checkSoundButtonClick(x, y);
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                //Handle touch dragged input
                if (GameObjectManager.getInstance().getObjs() != null) {
                    for (GameObject obj : GameObjectManager.getInstance().getObjs()) {
                        if (obj.interactive) {
                            obj.onTouchDragged(x, y);
                        }
                    }
                }
                super.touchDragged(event, x, y, pointer);
            }
        });
    }

    @Override
    public void show() {

    }

    private void initIfNeeded() {
        if (configSelected && !valuesInitialized) {
            //reset data and object managers
            //Todo : Add all reset and init functions here
            GameDataController.getInstance().setDefault();
            WorldData.getInstance().setDefault();
            GameObjectManager.getInstance().reset();
            valuesInitialized = true;
        }
    }

    private void update(float dt) {
        initIfNeeded();

        GameDataController.getInstance().update(dt);

        //Add Game End Lag
        if (GameDataController.getInstance().getIsGameEnded() && !GameDataController.getInstance().getIsGameOver()) {
            timeSinceGameEnd += dt;
            if (timeSinceGameEnd > GameData.getInstance().gameEndLag) {
                GameDataController.getInstance().setGameOver();
                return;
            }
        }

        GameObjectManager.getInstance().update(dt);

        hud.update(dt);
        debugRenderer.update(dt);
        stage.act(dt);
    }

    @Override
    public void render(float delta) {
        //skip frame if needed
        if (skipFrame) {
            skipFrame = false;
            return;
        }

        //stop render loop if game is over
        if (!GameDataController.getInstance().getIsGameOver()) {
            //update Objects
            update(delta);
        }


        //Clear Screen and Buffers
        Gdx.gl.glClearColor(1, 1, 1, 0.1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Draw Objects
        Batch batch = GameInfra.getInstance().batch;

        batch.begin();
//        hud.draw(batch);
        GameObjectManager.getInstance().draw(batch);

        if (GameData._debugMode) {
            debugRenderer.draw(batch);
        }
        batch.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        GameInfra.getInstance().resizeScreen(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
