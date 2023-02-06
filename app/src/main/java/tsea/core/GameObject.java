package tsea.core;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tsea.components.Component;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int UniqueId = -1;
    private String name;
    private List<Component> components;
    public Transform transform;
    private int zIndex;

    public int getzIndex() {
        return zIndex;
    }

    public int getUniqueId() {
        return UniqueId;
    }

    public List<Component> getallComponents() {
        return this.components;
    }

    public static void init(int maxId) {
        ID_COUNTER = maxId;
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.components = new ArrayList<Component>();
        this.transform = transform;
        this.zIndex = zIndex;
        this.UniqueId = ID_COUNTER++;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        var gameComponent = components.stream().filter(component -> {
            return componentClass.isAssignableFrom(component.getClass());
        }).findAny();
        
        if (gameComponent.isPresent()){
            return componentClass.cast(gameComponent.get()); 
        }

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        this.components = components.stream()
                        .filter(component -> !componentClass.isAssignableFrom(component.getClass()))
                        .collect(Collectors.toList());
    }

    public void addComponent(Component component) {
        component.generateUniqueId();
        this.components.add(component); 
        component.gameObject = this;
    }

    public void update(double deltaTime) {
        this.components.forEach(component -> component.update(deltaTime)); 
    }

    public void start() {
        this.components.forEach(component -> component.start()); 
    }

    public void imgui() {
        this.components.forEach(component -> component.imgui()); 
    }
}
