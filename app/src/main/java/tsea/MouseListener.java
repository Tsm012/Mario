package tsea;

import java.util.HashMap;

import org.lwjgl.glfw.GLFW;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPosition, yPosition, lastX, lastY;
    private HashMap<Integer, Boolean> mouseButtonPressed = new HashMap<Integer,Boolean>();
    private boolean isDragging;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPosition = 0.0;
        this.yPosition = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener get() {
        if(MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mousePositionCallback(long window, double xPosition, double yPosition) {
        get().lastX = get().xPosition;
        get().lastY = get().yPosition;
        get().xPosition = xPosition;
        get().yPosition = yPosition;
        get().isDragging = get().mouseButtonPressed.values().stream().reduce(false, (havePressed, arePressing) -> havePressed || arePressing);
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        switch (action) {
            case GLFW.GLFW_PRESS:
                get().mouseButtonPressed.put(button, true);
                break;
            case GLFW.GLFW_RELEASE:
                get().mouseButtonPressed.put(button, false);
                get().isDragging = false;
                break;
            default:
                break;
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastX = get().xPosition;
        get().lastY = get().yPosition;
    }

    public static float getX(){
        return (float) get().xPosition;
    }

    public static float getY(){
        return (float) get().yPosition;
    }

    public static float getDx(){
        return (float) (get().lastX - get().xPosition);
    }

    public static float getDy(){
        return (float) (get().lastY - get().yPosition);
    }

    public static float getScrollX(){
        return (float) get().scrollX;
    }

    public static float getScrollY(){
        return (float) get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;   
    }

    public static boolean mouseButtonDown(int button) {
        return get().mouseButtonPressed.getOrDefault(button, false);
    }
}
