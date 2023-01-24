package util;

import java.io.File;
import java.util.HashMap;

import java.util.Map;

import renderer.Shader;
import renderer.Texture;
import tsea.components.Spritesheet;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spriteSheets = new HashMap<>();

    public static Shader getShader(String vertexSource, String fragmentSource) {
        File vertexFile = new File(vertexSource);
        File fragmentFile = new File(fragmentSource);
        if (!AssetPool.shaders.containsKey(vertexFile.getAbsolutePath() + fragmentFile.getAbsolutePath())) {
            Shader shader = new Shader(vertexSource, fragmentSource);
            shader.compile();
            AssetPool.shaders.put(vertexFile.getAbsolutePath() + fragmentFile.getAbsolutePath(), shader);
        }
        return AssetPool.shaders.get(vertexFile.getAbsolutePath() + fragmentFile.getAbsolutePath());
    }

    public static Texture getTexture(String textureSource) {
        File textureFile = new File(textureSource);
        if (!AssetPool.shaders.containsKey(textureFile.getAbsolutePath())) {
            Texture texture = new Texture();
            texture.init(textureFile.getAbsolutePath());
            AssetPool.textures.put(textureFile.getAbsolutePath(), texture);
        }
        return AssetPool.textures.get(textureFile.getAbsolutePath());
    }

    public static void addSpritesheet(String resourceName, Spritesheet spritesheet){
        File spritesheetFile = new File(resourceName);
        if(!AssetPool.spriteSheets.containsKey(spritesheetFile.getAbsolutePath())){
            AssetPool.spriteSheets.put(spritesheetFile.getAbsolutePath(), spritesheet);
        }
    }

    public static Spritesheet getSpritesheet(String resourceName) {
        File spritesheetFile = new File(resourceName);
        return AssetPool.spriteSheets.getOrDefault(spritesheetFile.getAbsolutePath(), null);
    }
}
