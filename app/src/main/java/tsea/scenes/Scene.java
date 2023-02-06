package tsea.scenes;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import imgui.ImGui;
import renderer.Renderer;
import tsea.components.Component;
import tsea.components.ComponentDeserializer;
import tsea.components.GameObjectDeserializer;
import tsea.core.Camera;
import tsea.core.GameObject;

public abstract class Scene {

    protected Renderer renderer;
    protected Camera camera; 
    protected boolean isRunning =false;
    protected List<GameObject> gameObjects;
    protected GameObject activeGameObject = null;
    protected boolean levelLoaded = false;

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

    public void saveExit() {
        try {
            FileWriter writer = new FileWriter("level.txt", false);
            writer.write(new GsonBuilder()
                            .setPrettyPrinting()
                            .registerTypeAdapter(Component.class, new ComponentDeserializer())
                            .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                            .create()
                            .toJson(this.gameObjects));   
            writer.close();   
        } catch (IOException e) {
            e.printStackTrace();
        }       
           
    }

    public void load() {
        Gson gson = new GsonBuilder()
                        .setPrettyPrinting()
                        .registerTypeAdapter(Component.class, new ComponentDeserializer())
                        .registerTypeAdapter(GameObject.class, new GameObjectDeserializer())
                        .create();

        String inFile = "";
        try {
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        } catch (NoSuchFileException e) {
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (inFile != "") {
            int maxGameObjectId = -1;
            int maxComponentId = -1;
            for (GameObject gameObject : gson.fromJson(inFile, GameObject[].class)) {
                addGameObjectToScene(gameObject);
                for (Component component : gameObject.getallComponents()) {
                    if(component.getUniqueId() > maxComponentId) {
                        maxComponentId = component.getUniqueId(); 
                    }
                    if(gameObject.getUniqueId() > maxGameObjectId) {
                        maxGameObjectId = gameObject.getUniqueId(); 
                    }
                }
            }
            maxGameObjectId++;
            maxComponentId++;
            GameObject.init(maxGameObjectId);
            Component.init(maxComponentId);
            this.levelLoaded = true;    
        }
    }

    public Camera getCamera() {
        return this.camera;
    }
}
