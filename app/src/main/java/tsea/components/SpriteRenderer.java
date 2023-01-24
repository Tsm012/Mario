package tsea.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import imgui.ImGui;
import renderer.Texture;
import tsea.scenes.Transform;

public class SpriteRenderer extends Component{

    private Sprite sprite = new Sprite();
    private Vector4f color = new Vector4f(1,1,1,1);

    private transient Transform lastTransform;
    private transient boolean isDirty = false;
    
    public void setColor(Vector4f color) {
        if (this.color.equals(color)){
            this.color = color;
            this.isDirty = true;
        }
    }
    
    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }


    @Override
    public void start() {      
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(double deltatime) {    
        if (!this.lastTransform.equals(this.gameObject.transform)){
            this.gameObject.transform.copy(this.lastTransform);
             this.isDirty = true;
        }    
    }

    public Vector4f getColor() {
        return this.color;
    }
    
    public Texture getTexture() {
        return sprite.getTexture();
    }

    public void setTexture(Texture texture) {
        sprite.setTexture(texture);
    }
    public Vector2f[] getTextureCoordinates() {
        return sprite.getTextureCoordinates();
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void setClean() {
        this.isDirty = false;
    }

    @Override
    public void imgui(){
        ImGui.showDemoWindow();
    }
}
