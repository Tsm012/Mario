package tsea.components;

public class FontRenderer extends Component{

    @Override
    public void start() {      
        if(gameObject.getComponent(SpriteRenderer.class) != null) {
            System.out.println("Found renderer");
        } 
    }

    @Override
    public void update(double deltatime) {        
    }
    
}
