package org.seariver.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import org.seariver.BaseActor;

import static com.badlogic.gdx.Gdx.input;

public class Hero extends BaseActor {

    Animation north;
    Animation south;
    Animation east;
    Animation west;

    public Hero(float x, float y, Stage s) {

        super(x, y, s);

        String fileName = "hero.png";
        Texture texture = new Texture(Gdx.files.internal(fileName), true);

        int rows = 4;
        int cols = 3;
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);

        Array<TextureRegion> textureArray = new Array<>();

        float frameDuration = 0.2f;

        for (int c = 0; c < cols; c++)
            textureArray.add(temp[0][c]);
        south = new Animation(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[1][c]);
        west = new Animation(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[2][c]);
        east = new Animation(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[3][c]);
        north = new Animation(frameDuration, textureArray, Animation.PlayMode.LOOP_PINGPONG);

        setAnimation(south);

        // set after animation established
        setBoundaryPolygon(8);
        setAcceleration(800);
        setMaxSpeed(110);
        setDeceleration(800);
    }

    public void act(float deltaTime) {

        super.act(deltaTime);

        // hero movement controls
        if (input.isKeyPressed(Keys.LEFT)) accelerateAtAngle(180);
        if (input.isKeyPressed(Keys.RIGHT)) accelerateAtAngle(0);
        if (input.isKeyPressed(Keys.UP)) accelerateAtAngle(90);
        if (input.isKeyPressed(Keys.DOWN)) accelerateAtAngle(270);

        // pause animation when character not moving
        if (getSpeed() == 0) {
            setAnimationPaused(true);
        } else {
            setAnimationPaused(false);

            // set direction animation
            float angle = getMotionAngle();

            if (angle >= 45 && angle <= 135) {
                setAnimation(north);
            } else if (angle > 135 && angle < 225) {
                setAnimation(west);
            } else if (angle >= 225 && angle <= 315) {
                setAnimation(south);
            } else {
                setAnimation(east);
            }
        }

        applyPhysics(deltaTime);
    }
}
