package tsea.scenes;

import org.joml.Vector2f;

import imgui.ImGui;
import imgui.ImVec2;
import tsea.components.MouseControls;
import tsea.components.RigidBody;
import tsea.components.Sprite;
import tsea.components.SpriteRenderer;
import tsea.components.Spritesheet;
import tsea.core.Camera;
import tsea.core.GameObject;
import tsea.core.Prefabs;
import tsea.core.Transform;
import util.AssetPool;
import util.AssetReferences;

public class LevelEditorScene extends Scene {

    private GameObject gameObject;
    private Spritesheet sprites;

    MouseControls mouseControls = new MouseControls();

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-250,0));

        sprites = AssetPool.getSpritesheet(AssetReferences.DEFAULT_SPRITESHEET_FILE);

        if(this.levelLoaded){
            this.activeGameObject = gameObjects.get(0);
            return;
        }

        gameObject = new GameObject("Object", new Transform(new Vector2f(200,100),new Vector2f(256,256)), 2);
        gameObject.addComponent(new SpriteRenderer());
        gameObject.addComponent(new RigidBody());
        this.addGameObjectToScene(gameObject);

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
        mouseControls.update(deltaTime);

        this.gameObjects.forEach(gameObject -> gameObject.update(deltaTime));
        this.renderer.render();
    }

    @Override
    public void imgui(){
        ImGui.begin("tsea");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for (int i=0; i < sprites.size(); i++) {
            Sprite sprite = sprites.getSprite(i);
            float spriteWidth = sprite.getWidth() * 2;
            float spriteHeight = sprite.getHeight() * 2;
            int id = sprite.getTexId();
            Vector2f[] texCoords = sprite.getTextureCoordinates();

            ImGui.pushID(i);
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[0].x, texCoords[0].y, texCoords[2].x, texCoords[2].y)) {
                GameObject object = Prefabs.generateSpriteObject(sprite, spriteWidth, spriteHeight);
                mouseControls.pickupObject(object);
            }
            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if (i + 1 < sprites.size() && nextButtonX2 < windowX2) {
                ImGui.sameLine();
            }
        }


        ImGui.end();
    }
}
