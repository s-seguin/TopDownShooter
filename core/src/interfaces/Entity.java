package interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public interface Entity {

    void update(float deltaTime);

    void draw(SpriteBatch batch);

    Vector2 getPosition();

    Rectangle getBoundingRectangle();

    String getId();
}
