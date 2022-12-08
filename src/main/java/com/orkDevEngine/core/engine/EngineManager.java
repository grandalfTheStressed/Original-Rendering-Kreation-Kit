package com.orkDevEngine.core.engine;

import com.orkDevEngine.core.engine.games.IGameLogic;
import com.orkDevEngine.core.engine.screen.input.KeyboardManager;
import com.orkDevEngine.core.engine.screen.input.MouseManager;
import com.orkDevEngine.core.engine.screen.WindowManager;
import com.orkDevEngine.core.engine.utils.Clock;
import com.orkDevEngine.core.engine.utils.exceptions.ClockException;
import com.orkDevEngine.launcher.Main;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.orkDevEngine.core.engine.utils.Constants.FRAME_RATE;
import static com.orkDevEngine.core.engine.utils.Constants.NANO_SECOND;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwTerminate;

public class EngineManager {

    private static final Logger logger = LoggerFactory.getLogger(EngineManager.class);
    private static String fps;
    private static long frameTime = NANO_SECOND / FRAME_RATE;

    private boolean isRunning;

    private WindowManager window;
    private KeyboardManager keyboardManager;
    private MouseManager mouseManager;
    private IGameLogic gameLogic;

    private Clock clock;
    private GLFWErrorCallback errorCallback;

    private void init() throws Exception {
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
        clock = new Clock();
        window = Main.getWindow();
        gameLogic = Main.getGame();
        mouseManager = new MouseManager();
        keyboardManager = new KeyboardManager();
    }

    public void start() throws Exception {
        if(isRunning) return;

        init();
        window.init();
        mouseManager.init();
        keyboardManager.init();
        gameLogic.init(mouseManager, keyboardManager);
        run();
    }

    public void run() {

        isRunning = true;
        long renderTime;

        try {
            clock.setTimedEvent("renderTime", Clock.TIME_UNIT.NANOS, frameTime);
        } catch (ClockException e) {
            e.printStackTrace();
        }

        while (isRunning) {
            if (window.windowShouldClose()) {
                stop();
            }

            try {
                renderTime = clock.checkTimedEvent("renderTime") + frameTime;
                if(renderTime >= frameTime) {
                    update(renderTime / 1000000000.0f);
                    input();
                    int fps = (int) Math.ceil((1.0 / ((double) renderTime / NANO_SECOND)));
                    if(fps < 60)
                        logger.debug("fps dropped below target value: {}", fps);

                    setFps(fps + "");
                    render();
                    clock.updateTimedEvent("renderTime", Clock.TIME_UNIT.NANOS, frameTime);
                }
            } catch (ClockException e) {
                e.printStackTrace();
            }
        }
        cleanUp();
    }

    public void stop() {
        if(!isRunning) return;

        isRunning = false;
    }

    public void cleanUp() {
        gameLogic.cleanUp();
        window.cleanUp();
        errorCallback.free();
        glfwTerminate();
    }

    public void update(float interval) {
        gameLogic.update(interval);
    }

    public void render() {
        gameLogic.render();
        window.update();
    }

    public void input() {
        gameLogic.input();
    }

    public static void setFps(String fps) {
        EngineManager.fps = fps;
    }

    public static String getFps() {
        return fps;
    }
}
