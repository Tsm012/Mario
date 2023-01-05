package tsea;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tsea.components.Component;

public class GameObject {

    private String name;
    private List<Component> components = new ArrayList<Component>();

    public GameObject(String name) {
        this.name = name;
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
        System.out.println("starting components for " + this.name);
        this.components.forEach(component -> component.start()); 
    }
}
