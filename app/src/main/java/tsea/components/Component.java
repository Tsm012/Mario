package tsea.components;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.joml.Vector3f;
import org.joml.Vector4f;

import imgui.ImGui;
import tsea.core.GameObject;

public abstract class Component {
    private static int ID_COUNTER = 0;
    private int UniqueId = -1;
    public transient GameObject gameObject;

    
    public int getUniqueId() {
        return UniqueId;
    }
    public void generateUniqueId() {
        this.UniqueId = UniqueId == -1 ? ID_COUNTER++ : UniqueId;
    }
   
    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }

    public void start() {}
    public void update(double deltatime){}
    public void imgui() {
        Field[] fields = this.getClass().getDeclaredFields();
        for(Field field : fields) {
            boolean isTransient = Modifier.isTransient(field.getModifiers());
            if(isTransient){
                continue;
            }
            boolean isPrivate = Modifier.isPrivate(field.getModifiers());
            if(isPrivate){
                field.setAccessible(true);
            }
            Class type = field.getType();
            try {
                Object value = field.get(this);
                String name = field.getName();
                if(type == int.class) {
                    int[] imInt = {(int)value};
                    if(ImGui.dragInt(name + ": ", imInt)){
                        field.set(this, imInt[0]);
                    }
                    continue;
                }
                if(type == float.class) {
                    float[] imFloat = {(float)value};
                    if(ImGui.dragFloat(name + ": ", imFloat)){
                        field.set(this, imFloat[0]);
                    }
                    continue;
                }
                if(type == boolean.class) {
                    if(ImGui.checkbox(name + ": ", (boolean)value)){
                        field.set(this, !(boolean)value);
                    }
                    continue;
                }
                if(type == Vector3f.class) {
                    Vector3f vector = (Vector3f)value;
                    float[] imVec = {vector.x, vector.y, vector.z};
                    if(ImGui.dragFloat3(name + ": ", imVec)){
                        vector.set(imVec[0], imVec[1], imVec[2]);
                    }
                    continue;
                }
                if(type == Vector4f.class) {
                    Vector4f vector = (Vector4f)value;
                    float[] imVec = {vector.x, vector.y, vector.z, vector.w};
                    if(ImGui.dragFloat3(name + ": ", imVec)){
                        vector.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                    }
                    continue;
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if(isPrivate){
                field.setAccessible(false);
            }
        }
    }
}
