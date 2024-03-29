package tsea.components;

import org.joml.Vector2f;

import renderer.Texture;

public class Sprite {

    private float width, height;
    private Texture texture = null;
    
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    private Vector2f[] textureCoords = {
                new Vector2f(1, 1),
                new Vector2f(1, 0),
                new Vector2f(0, 0),
                new Vector2f(0, 1)
            };

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTextureCoordinates() {
        return textureCoords;
    }

    public void setTextureCoords(Vector2f[] textureCoords) {
        this.textureCoords = textureCoords;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public int getTexId() {
        return texture == null ? -1 : texture.getId();
    }
}
