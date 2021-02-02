package org.seariver.screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import org.seariver.BaseActor;
import org.seariver.BaseGame;
import org.seariver.BaseScreen;
import org.seariver.actor.Hero;

public class LevelScreen extends BaseScreen {

    Maze maze;
    Hero hero;

    public void initialize() {

        BaseActor background = new BaseActor(0, 0, mainStage);
        background.loadTexture("white.png");
        background.setColor(Color.GRAY);
        background.setSize(768, 700);

        maze = new Maze(mainStage);

        hero = new Hero(0, 0, mainStage);
        hero.centerAtActor(maze.getRoom(0, 0));
    }

    // Game Loop
    public void update(float deltaTime) {
        for (BaseActor wall : BaseActor.getList(mainStage, "org.seariver.actor.Wall")) {
            hero.preventOverlap(wall);
        }
    }

    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.R) {
            BaseGame.setActiveScreen(new LevelScreen());
        }

        return false;
    }
}
