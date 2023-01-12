package util;

import java.io.File;
import java.util.HashMap;

import java.util.Map;

import renderer.Shader;
import renderer.Texture;

public class AssetPool {
    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();

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
            Texture shader = new Texture(textureFile.getAbsolutePath());
            AssetPool.textures.put(textureFile.getAbsolutePath(), shader);
        }
        return AssetPool.textures.get(textureFile.getAbsolutePath());
    }
}