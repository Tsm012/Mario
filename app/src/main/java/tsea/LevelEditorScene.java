package tsea;

import tsea.Window.SCENE;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {

    private boolean isLoading = false;
    private float loadingTime = 2.0f;

    public LevelEditorScene() {
        System.out.println("Level EditorScene");
    }

    @Override
    public void update(double deltaTime) {

        System.out.println("" + (1.0f / deltaTime) + "FPS");
        
        if (!isLoading && KeyListener.isKeyPressed(GLFW_KEY_SPACE)){
            isLoading = true;
        } 

        if(isLoading && loadingTime > 0){
            loadingTime -= deltaTime;
            Window.get().red -= deltaTime * 5.0f;
            Window.get().blue -= deltaTime * 5.0f;
            Window.get().green -= deltaTime * 5.0f;
        } else if(isLoading) {
            Window.changeScene(SCENE.LEVEL);
        }
        
    }
}
