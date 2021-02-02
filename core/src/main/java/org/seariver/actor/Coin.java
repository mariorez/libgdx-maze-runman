package org.seariver.actor;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.seariver.BaseActor;

public class Coin extends BaseActor {

    public Coin(float x, float y, Stage stage) {
        super(x, y, stage);
        loadTexture("coin.png");
        setBoundaryPolygon(6);
    }
}
