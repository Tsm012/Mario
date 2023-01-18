package tsea.scenes;

import org.joml.Vector2f;

import tsea.GameObject;
import tsea.components.Sprite;
import tsea.components.SpriteRenderer;
import tsea.components.Spritesheet;
import util.AssetPool;
import util.AssetReferences;

public class LevelEditorScene extends Scene {
    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-250,-250));

        Spritesheet sprites = AssetPool.getSpritesheet(AssetReferences.DEFAULT_SPRITESHEET_FILE);

        GameObject gameObject = new GameObject("Object", new Transform(new Vector2f(100,100),new Vector2f(256,256)));
        gameObject.addComponent(new SpriteRenderer(sprites.getSprite(2)));
        this.addGameObjectToScene(gameObject);

        GameObject gameObject2 = new GameObject("Object2", new Transform(new Vector2f(400,100),new Vector2f(256,256)));
        gameObject2.addComponent(new SpriteRenderer(sprites.getSprite(16)));
        this.addGameObjectToScene(gameObject2);

        
    }

    private void loadResources() {
        AssetPool.getShader(AssetReferences.DEFAULT_VERTEX_FILE, AssetReferences.DEFAULT_SHADER_FILE);
        AssetPool.addSpritesheet(AssetReferences.DEFAULT_SPRITESHEET_FILE, 
            new Spritesheet(AssetPool.getTexture(AssetReferences.DEFAULT_SPRITESHEET_FILE),
            16, 16, 26,0));

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
