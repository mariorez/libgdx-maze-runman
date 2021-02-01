package org.seariver.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import org.seariver.BaseActor;
import org.seariver.BaseGame;
import org.seariver.BaseScreen;

public class LevelScreen extends BaseScreen {

    Maze maze;

    public void initialize() {

        BaseActor background = new BaseActor(0, 0, mainStage);
        background.loadTexture("white.png");
        background.setColor(Color.GRAY);
        background.setSize(768, 700);

        maze = new Maze(mainStage);
    }

    public void update(float deltaTime) {
    }

    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.R) {
            BaseGame.setActiveScreen(new LevelScreen());
        }

        return false;
    }
}
