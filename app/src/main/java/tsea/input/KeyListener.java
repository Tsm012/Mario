package tsea.input;

import java.util.HashMap;

import org.lwjgl.glfw.GLFW;

public class KeyListener {
    private static KeyListener instance;
    private HashMap<Integer, Boolean> keyPressed = new HashMap<Integer,Boolean>(); 

    private KeyListener() {

    }

    private static KeyListener get() {
        if(KeyListener.instance == null){
            KeyListener.instance = new KeyListener();
        }
        return KeyListener.instance;
    }

    public static void keyCallback(long windows, int key, int scancode, int action, int mods) {
        switch (action) {
            case GLFW.GLFW_PRESS:
                get().keyPressed.put(key, true);
                break;
            case GLFW.GLFW_RELEASE:
                get().keyPressed.put(key, false);
                break;
            default:
                break;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed.getOrDefault(keyCode, false);
    }
} 
