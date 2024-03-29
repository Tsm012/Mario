package tsea.core;

import org.joml.Vector2f;

public class Transform {
    
    public Vector2f position;
    public Vector2f scale;
    

    public Transform() {
        this.position = new Vector2f();
        this.scale = new Vector2f();
    }

    public Transform(Vector2f position) {
        this.position = position;
        this.scale = new Vector2f();
    }

    public Transform(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }

    public void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
    }

    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(Transform transform){
        transform.position.set(this.position);
        transform.scale.set(this.scale);
    }

    public boolean equals(Object object) {
        if(object == null) return false;
        if(!(object instanceof Transform)) return false;

        Transform transform = (Transform) object;   
        return transform.position.equals(this.position) && transform.scale.equals(this.scale);
    }
}
