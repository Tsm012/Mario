package tsea.scenes;

import java.util.ArrayList;
import java.util.List;

import tsea.GameObject;

public abstract class Scene {

    protected Camera camera; 
    private boolean isRunning =false;
    protected List<GameObject> gameObjects = new ArrayList<GameObject>();

    public Scene() {
        
    }

    public void init() {

    }

    public void start() {
        gameObjects.forEach(gameObject -> gameObject.start());
        isRunning = true;
    }

    public void addGameObjectsToScene(GameObject gameObject) {
        gameObjects.add(gameObject);

        if (isRunning) {
            gameObject.start();
        }
    }

    public abstract void update(double deltaTime);
}
