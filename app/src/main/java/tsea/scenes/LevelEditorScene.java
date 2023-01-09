package tsea.scenes;

import org.joml.Vector2f;
import org.joml.Vector4f;

import tsea.GameObject;
import tsea.components.SpriteRenderer;

public class LevelEditorScene extends Scene {
    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());

        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (float) (600 - xOffset * 2);
        float totalHeight = (float) (300 - yOffset * 2);

        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for(int x = 0; x < 100; x++){
            for(int y = 0; y < 100; y++){
                float xPosition = xOffset + (x * sizeX);
                float yPosition = yOffset + (y * sizeY);

                GameObject gameObject = new GameObject("BR" + xPosition + "" + yPosition, new Transform(new Vector2f(xPosition, yPosition), new Vector2f(sizeX, sizeY)));
                gameObject.addComponent(new SpriteRenderer(new Vector4f(xPosition / totalWidth, yPosition / totalHeight, 1, 1)));
                this.addGameObjectToScene(gameObject);
            }   
        }
    }

    public LevelEditorScene() {
        System.out.println("Level Editor Scene");
    }

    @Override
    public void update(double deltaTime) {
        System.out.println("FPS BIOTCH " + (1.0f / deltaTime));
        this.gameObjects.forEach(gameObject -> gameObject.update(deltaTime));
        this.renderer.render();
    }
}
