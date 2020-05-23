package com.mega.games.gamestartingkit.core.dataLoaders;

import com.badlogic.gdx.Gdx;
import com.mega.games.gamestartingkit.core.gameObjects.entities.PlayerScoreManager;
import com.mega.games.support.MegaServices;

import java.util.HashMap;
import java.util.Map;

public class GameDataController {
    //set singleton
    private static final GameDataController _myInstance = new GameDataController();
    private boolean isConfigValid;
    private String configError;
    private MegaServices megaServices;
    private GameData data = GameData.getInstance();
    private int totalScore;

    private GameDataController() {
    }

    public static GameDataController getInstance() {
        return _myInstance;
    }

    void setMegaServices(MegaServices megaServ) {
        megaServices = megaServ;

        isConfigValid = validateConfig();
        if (!isConfigValid) {
            HashMap<String, Object> rv = new HashMap<>();
            rv.put("error", configError);
            megaServ.analytics().logEvent("Invalid_Config_Found", rv);
        }
    }

    public void startGame() {
        data.isGameEnded = false;
        data.isGameOver = false;
        GameInfra.getInstance().megaServices.callbacks().playStarted();
    }

    private boolean validateConfig() {
        try {
            //Todo: Add Checks here
            //Example:
            float maxZomSp = Float.parseFloat((String) megaServices.config().get("maxZombieSpeed"));
            float minZomSp = Float.parseFloat((String) megaServices.config().get("minZombieSpeed"));
            if (maxZomSp < minZomSp || maxZomSp < 0 || minZomSp < 0) {
                throw new Exception("malformed zombie speed bounds");
            }
        } catch (Exception e) {
            configError = e.getMessage();
            return false;
        }

        return true;
    }

    private String extractValFromConfig(String key, String defVal) {
        if (isConfigValid) {
            return (String) megaServices.config().get(key);
        } else {
            return defVal;
        }
    }

    public void setDefault() {
        data.elapsed = 0;
        data.gameEndLag = 5;
        data.scores = new int[Constants.PLAYERS_COUNT];
        for (int i = 0; i < Constants.PLAYERS_COUNT; i++) {
            data.scores[i] = 0;
        }
        resetPlayerIndex();
        totalScore = 0;
    }

    public void addScoreToCurrPlayer(int val) {
        data.scores[data.currPlayerIdx] += val;
        PlayerScoreManager.getInstance().onScoreUpdate();
        totalScore += val;
        if (totalScore >= Constants.NUM_COL * Constants.NUM_ROW) {
            setGameOver();
            setGameEnded();
        }
    }

    public void addScoreToCurrPlayer() {
        addScoreToCurrPlayer(1);
    }

    public void setToNextPlayer() {
        data.currPlayerIdx++;
        if (data.currPlayerIdx >= Constants.PLAYERS_COUNT) {
            data.currPlayerIdx = 0;
        }
        PlayerScoreManager.getInstance().onPlayerChange();
    }

    public int getCurrPlayerIndex() {
        return data.currPlayerIdx;
    }

    public void setGameEnded() {
        if (!data.isGameEnded) {
            data.isGameEnded = true;
            Gdx.input.setInputProcessor(null);
            megaServices.analytics().logEvent("Death", getGameState());
        }
    }

    public void resetPlayerIndex() {
        data.currPlayerIdx = 0;
    }

    public boolean getIsGameEnded() {
        return data.isGameEnded;
    }

    private Map<String, Object> getGameState() {
        Map<String, Object> rv = new HashMap<>();

        //Todo: Fill the keys
        //keys shouldn't have spaces

        return rv;
    }

    public void setGameOver() {
        if (!data.isGameOver) {
            data.isGameEnded = true;
            data.isGameOver = true;
            int maxScore = 0;
            for (int i = 0; i < Constants.PLAYERS_COUNT; i++) {
                maxScore += data.scores[i];
            }
            megaServices.callbacks().gameOver(maxScore);
        }
    }

    public boolean getIsGameOver() {
        return data.isGameOver;
    }

    public void update(float dt) {
        data.elapsed += dt;
    }
}
