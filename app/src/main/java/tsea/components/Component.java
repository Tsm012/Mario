package tsea.components;

import tsea.GameObject;

public abstract class Component {
    public GameObject gameObject;

    public void start() {

    }
    public void update(double deltatime){}
    public void imgui() {}
}
