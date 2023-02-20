package tsea.scenes;

import org.joml.Vector2f;
import org.joml.Vector3f;

import imgui.ImGui;
import imgui.ImVec2;
import renderer.DebugDraw;
import tsea.components.GridLines;
import tsea.components.MouseControls;
import tsea.components.Sprite;
import tsea.components.SpriteRenderer;
import tsea.components.Spritesheet;
import tsea.core.Camera;
import tsea.core.GameObject;
import tsea.core.Prefabs;
import tsea.core.Transform;
import util.AssetPool;
import util.Settings;

public class LevelEditorScene extends Scene {

    // private GameObject gameObject;
    private Spritesheet sprites;

    MouseControls mouseControls = new MouseControls();
    GameObject levelEditorStuff = new GameObject("LevelEditor", new Transform(new Vector2f()), 0);

    @Override
    public void init() {
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());
        
        loadResources();
        this.camera = new Camera(new Vector2f(-250,0));

        sprites = AssetPool.getSpritesheet(Settings.DEFAULT_SPRITESHEET_FILE);

        if(this.levelLoaded){
            if(gameObjects.size() > 0) {
                this.activeGameObject = gameObjects.get(0);
            } 
            return;
        }

        // gameObject = new GameObject("Object", new Transform(new Vector2f(200,100),new Vector2f(256,256)), 2);
        // gameObject.addComponent(new SpriteRenderer());
        // gameObject.addComponent(new RigidBody());
        // this.addGameObjectToScene(gameObject);

        // this.activeGameObject = gameObject;
    }

    private void loadResources() {
        AssetPool.getShader(Settings.DEFAULT_VERTEX_FILE, Settings.DEFAULT_SHADER_FILE);
        AssetPool.addSpritesheet(Settings.DEFAULT_SPRITESHEET_FILE, 
            new Spritesheet(AssetPool.getTexture(Settings.DEFAULT_SPRITESHEET_FILE),
            16, 16, 26,0));
        
        for(GameObject gameObject: this.gameObjects){
            if(gameObject.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spriteRender = gameObject.getComponent(SpriteRenderer.class);
                if(spriteRender.getTexture() != null) {
                    spriteRender.setTexture(AssetPool.getTexture(spriteRender.getTexture().getFilePath()));
                }
            }
        }
    }

    public LevelEditorScene() {
        System.out.println("Level Editor Scene");
    }

    float x = 200.0f;
    float y = 200.0f;
    float angle = 0.0f;

    @Override
    public void update(double deltaTime) {
        levelEditorStuff.update(deltaTime);
        DebugDraw.addCircle(new Vector2f(x,y), 64, new Vector3f(0,1,0), 1);
        DebugDraw.addBox2D(new Vector2f(200, 200), new Vector2f(64,32), angle, new Vector3f(1,0,0), 1);
        angle += 15.0f * (float)deltaTime;
        x += 5.0f * (float)deltaTime;
        y += 5.0f * (float)deltaTime;

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
            if (ImGui.imageButton(id, spriteWidth, spriteHeight, texCoords[2].x, texCoords[0].y, texCoords[0].x, texCoords[2].y)) {
                GameObject object = Prefabs.generateSpriteObject(sprite, 32, 32);
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
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
