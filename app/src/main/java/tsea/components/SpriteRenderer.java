package tsea.components;

import org.joml.Vector2f;
import org.joml.Vector4f;

import imgui.ImGui;
import renderer.Texture;
import tsea.scenes.Transform;

public class SpriteRenderer extends Component{

    private Transform lastTransform;
    private Vector4f color;
    private boolean isDirty = false;
    
    public void setColor(Vector4f color) {
        if (this.color.equals(color)){
            this.color = color;
            this.isDirty = true;
        }

    }
    private Sprite sprite;

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

    public SpriteRenderer(Vector4f color) {
        this.color = color;
        this.sprite = new Sprite(null);
        this.isDirty = true;
    }

    public SpriteRenderer(Sprite sprite) {
        this.sprite = sprite;
        this.color = new Vector4f(1,1,1,1);
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
