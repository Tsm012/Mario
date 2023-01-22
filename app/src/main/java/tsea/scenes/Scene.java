package tsea.scenes;

import java.util.ArrayList;
import java.util.List;

import imgui.ImGui;
import renderer.Renderer;
import tsea.GameObject;

public abstract class Scene {

    protected Renderer renderer;
    protected Camera camera; 
    private boolean isRunning =false;
    protected List<GameObject> gameObjects;
    protected GameObject activeGameObject = null;

    public Scene() {
        this.gameObjects = new ArrayList<GameObject>();
        this.renderer = new Renderer();
    }

    public void init() {
        
    }

    public void start() {
        gameObjects.forEach(gameObject -> { 
            gameObject.start();
            this.renderer.add(gameObject);
        });
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject gameObject) {
        gameObjects.add(gameObject);

        if (isRunning) {
            gameObject.start();
            this.renderer.add(gameObject);
        }
    }

    public abstract void update(double deltaTime);

    public void sceneImgui() {
        if(activeGameObject != null) {
            ImGui.begin("Inspector");
            activeGameObject.imgui();
            ImGui.end();
        }
        imgui();
    }

    public void imgui() {

    }

    public Camera getCamera() {
        
        return this.camera;
    }
}
