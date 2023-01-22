package tsea.scenes;

import org.joml.Vector2f;
import org.joml.Vector4f;

import imgui.ImGui;
import tsea.GameObject;
import tsea.components.SpriteRenderer;
import tsea.components.Spritesheet;
import util.AssetPool;
import util.AssetReferences;

public class LevelEditorScene extends Scene {

    private GameObject gameObject;
    private Spritesheet sprites;
    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-250,-250));

        sprites = AssetPool.getSpritesheet(AssetReferences.DEFAULT_SPRITESHEET_FILE);

        gameObject = new GameObject("Object", new Transform(new Vector2f(100,100),new Vector2f(256,256)), 2);
        gameObject.addComponent(new SpriteRenderer(sprites.getSprite(2)));
        this.addGameObjectToScene(gameObject);

       

        GameObject gameObject2 = new GameObject("Object2", new Transform(new Vector2f(400,100),new Vector2f(256,256)), 0);
        gameObject2.addComponent(new SpriteRenderer(new Vector4f(1,0,0,1)));
        this.addGameObjectToScene(gameObject2);

        this.activeGameObject = gameObject2;
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
        spriteFlipTimeLeft -= deltaTime;
        if (spriteFlipTimeLeft <= 0){
            spriteFlipTimeLeft = spriteFlipTime;
            spriteIndex++;
            gameObject.getComponent(SpriteRenderer.class).setSprite(sprites.getSprite(spriteIndex%26));
        }
        gameObject.transform.position.x += 10 * deltaTime;
        this.gameObjects.forEach(gameObject -> gameObject.update(deltaTime));
        this.renderer.render();
    }

    @Override
    public void imgui(){
    }
}
