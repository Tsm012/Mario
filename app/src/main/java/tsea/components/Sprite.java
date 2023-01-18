package tsea.components;

import org.joml.Vector2f;

import renderer.Texture;

public class Sprite {
    private Texture texture;
    private Vector2f[] textureCoords;

    public Texture getTexture() {
        return texture;
    }

    public Vector2f[] getTextureCoordinates() {
        return textureCoords;
    }

    public void setTextureCoords(Vector2f[] textureCoords) {
        this.textureCoords = textureCoords;
    }

    public Sprite(Texture texture){
        this.texture = texture;
        this.textureCoords = new Vector2f[] {
            new Vector2f(1, 1),
            new Vector2f(1, 0),
            new Vector2f(0, 0),
            new Vector2f(0, 1)
        };
    }

    public Sprite(Texture texture, Vector2f[] textureCoords) {
        this.texture = texture;
        this.textureCoords = textureCoords;
    }
}