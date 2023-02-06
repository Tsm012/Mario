package tsea.components;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import renderer.Texture;

public class Spritesheet {
    private List<Sprite> sprites;
    
    public Sprite getSprite(int index) {
        return sprites.get(index);
    }

    private Texture texture;

    public Spritesheet(Texture texture, int spriteWidth, int spriteHeight, int numberOfSprites, int spacing) {
        this.sprites = new ArrayList<>();
        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getHeight() - spriteHeight;

        for (int i=0; i<numberOfSprites; i++){
            float topY = (currentY + spriteHeight) / (float)texture.getHeight(); 
            float rightX = (currentX + spriteWidth) / (float)texture.getWidth();
            float leftX = currentX / (float) texture.getWidth(); 
            float bottomY = currentY / (float) texture.getHeight();

            Vector2f[] textureCoordinates = new Vector2f[] {
                new Vector2f(rightX, topY),
                new Vector2f(rightX, bottomY),
                new Vector2f(leftX, bottomY),
                new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTextureCoords(textureCoordinates);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if(currentX >= texture.getWidth()){
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public int size() {
        return sprites.size();
    }
}
