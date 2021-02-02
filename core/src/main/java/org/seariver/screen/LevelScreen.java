package org.seariver.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import org.seariver.BaseActor;
import org.seariver.BaseGame;
import org.seariver.BaseScreen;
import org.seariver.actor.Coin;
import org.seariver.actor.Ghost;
import org.seariver.actor.Hero;

public class LevelScreen extends BaseScreen {

    Maze maze;
    Hero hero;

    Label coinsLabel;
    Label messageLabel;

    Sound coinSound;
    Music windMusic;

    public void initialize() {

        BaseActor background = new BaseActor(0, 0, mainStage);
        background.loadTexture("white.png");
        background.setColor(Color.GRAY);
        background.setSize(768, 700);

        maze = new Maze(mainStage);

        hero = new Hero(0, 0, mainStage);
        hero.centerAtActor(maze.getRoom(0, 0));

        for (int i = 0; i <= 3; i++) {
            int startAt = 8 - i;
            Ghost ghost = new Ghost(0, 0, mainStage);
            ghost.centerAtActor(maze.getRoom(startAt, startAt));
            ghost.speed = ghost.speed + (i*3);
            ghost.toFront();
        }

        for (BaseActor room : BaseActor.getList(mainStage, "org.seariver.actor.Room")) {
            Coin coin = new Coin(0, 0, mainStage);
            coin.centerAtActor(room);
        }

        coinsLabel = new Label("Coins left:", BaseGame.labelStyle);
        coinsLabel.setColor(Color.GOLD);
        messageLabel = new Label("...", BaseGame.labelStyle);
        messageLabel.setFontScale(2);
        messageLabel.setVisible(false);

        uiTable.pad(10);
        uiTable.add(coinsLabel);
        uiTable.row();
        uiTable.add(messageLabel).expandY();

        coinSound = Gdx.audio.newSound(Gdx.files.internal("coin.wav"));
        windMusic = Gdx.audio.newMusic(Gdx.files.internal("wind.mp3"));
        windMusic.setLooping(true);
        windMusic.setVolume(0.1f);
        windMusic.play();
    }

    // Game Loop
    public void update(float deltaTime) {

        for (BaseActor wall : BaseActor.getList(mainStage, "org.seariver.actor.Wall")) {
            hero.preventOverlap(wall);
        }

        for (BaseActor coin : BaseActor.getList(mainStage, "org.seariver.actor.Coin")) {
            if (hero.overlaps(coin)) {
                coinSound.play(0.10f);
                coin.remove();
            }
        }

        int coins = BaseActor.count(mainStage, "org.seariver.actor.Coin");
        coinsLabel.setText("Coins left: " + coins);

        if (coins == 0) {
            for (BaseActor ghost : BaseActor.getList(mainStage, "org.seariver.actor.Ghost")) {
                ghost.remove();
                ghost.setPosition(-1000, -1000);
                ghost.clearActions();
                ghost.addAction(Actions.forever(Actions.delay(1)));
            }
            messageLabel.setText("You win!");
            messageLabel.setColor(Color.GREEN);
            messageLabel.setVisible(true);
        }

        for (BaseActor actor : BaseActor.getList(mainStage, "org.seariver.actor.Ghost")) {

            Ghost ghost = (Ghost) actor;

            if (hero.overlaps(ghost)) {
                hero.remove();
                hero.setPosition(-1000, -1000);
                ghost.clearActions();
                ghost.addAction(Actions.forever(Actions.delay(1)));
                messageLabel.setText("Game Over");
                messageLabel.setColor(Color.RED);
                messageLabel.setVisible(true);
            }

            if (ghost.getActions().size == 0) {
                maze.resetRooms();
                ghost.findPath(maze.getRoom(ghost), maze.getRoom(hero));
            }

            if (!messageLabel.isVisible()) {
                float distance = new Vector2(hero.getX() - ghost.getX(), hero.getY() - ghost.getY()).len();
                float volume = -(distance - 64) / (300 - 64) + 1;
                volume = MathUtils.clamp(volume, 0.10f, 1.00f);
                windMusic.setVolume(volume);
            }
        }
    }

    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.R) {
            BaseGame.setActiveScreen(new LevelScreen());
        }

        return false;
    }
}
