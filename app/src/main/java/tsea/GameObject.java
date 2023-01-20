package tsea;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tsea.components.Component;
import tsea.scenes.Transform;

public class GameObject {

    private String name;
    private List<Component> components;
    public Transform transform;
    private int zIndex;

    public int getzIndex() {
        return zIndex;
    }

    public GameObject(String name) {
        this.name = name;
        this.components = new ArrayList<Component>();
        this.transform = new Transform();
        this.zIndex = 0;
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.name = name;
        this.components = new ArrayList<Component>();
        this.transform = transform;
        this.zIndex = zIndex;
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
        this.components.add(component); 
        component.gameObject = this;
    }

    public void update(double deltaTime) {
        this.components.forEach(component -> component.update(deltaTime)); 
    }

    public void start() {
        this.components.forEach(component -> component.start()); 
    }
}
