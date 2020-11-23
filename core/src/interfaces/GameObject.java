package interfaces;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public interface GameObject {
    void update (float deltaTime);
    void draw (SpriteBatch batch);
}
