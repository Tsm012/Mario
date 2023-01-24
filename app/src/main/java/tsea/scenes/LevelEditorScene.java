package tsea.scenes;

import org.joml.Vector2f;
import tsea.GameObject;
import tsea.components.SpriteRenderer;
import tsea.components.Spritesheet;
import util.AssetPool;
import util.AssetReferences;

public class LevelEditorScene extends Scene {

    private GameObject gameObject;
    private Spritesheet sprites;

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-250,-250));

        sprites = AssetPool.getSpritesheet(AssetReferences.DEFAULT_SPRITESHEET_FILE);

        gameObject = new GameObject("Object", new Transform(new Vector2f(100,100),new Vector2f(256,256)), 2);
        gameObject.addComponent(new SpriteRenderer());

        this.activeGameObject = gameObject;
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
        this.gameObjects.forEach(gameObject -> gameObject.update(deltaTime));
        this.renderer.render();
    }

    @Override
    public void imgui(){
    }
}
