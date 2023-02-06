package tsea.components;


import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

import tsea.Window;
import tsea.core.GameObject;
import tsea.input.MouseListener;

public class MouseControls extends Component {
    GameObject holdingObject = null;

    public void pickupObject(GameObject go) {
        this.holdingObject = go;
        Window.get().getScene().addGameObjectToScene(go);
    }

    public void place() {
        this.holdingObject = null;
    }

    @Override
    public void update(double deltaTime) {
        if (holdingObject != null) {
            holdingObject.transform.position.x = MouseListener.getOrthoX() - 16;
            holdingObject.transform.position.y = MouseListener.getOrthoY() - 16;

            if (MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
                place();
            }
        }
    }
}