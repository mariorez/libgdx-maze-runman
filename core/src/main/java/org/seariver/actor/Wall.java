package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.seariver.BaseActor;

public class Wall extends BaseActor {

    public Wall(float x, float y, float width, float height, Stage stage) {
        super(x, y, stage);
        loadTexture("square.jpg");
        setSize(width, height);
        setBoundaryRectangle();
    }
}
