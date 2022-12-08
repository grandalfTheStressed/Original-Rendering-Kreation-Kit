package com.orkDevEngine.launcher;

import com.orkDevEngine.core.engine.games.TestGame;
import com.orkDevEngine.core.engine.utils.Constants;
import com.orkDevEngine.core.engine.EngineManager;
import com.orkDevEngine.core.engine.screen.WindowManager;
import org.lwjgl.Version;

public class Main {

    private static WindowManager window;
    private static TestGame game;

    public static void main(String[] args){
        System.out.println(Version.getVersion());
        window = new WindowManager(Constants.TITLE, 1600, 900, false);
        game = new TestGame();
        EngineManager engine = new EngineManager();
        try {
            engine.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WindowManager getWindow() {
        return window;
    }

    public static TestGame getGame() {
        return game;
    }
}
