package tsea.scenes;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.*;

import renderer.Shader;
import renderer.Texture;
import tsea.GameObject;
import tsea.components.FontRenderer;
import tsea.components.SpriteRenderer;


public class LevelEditorScene extends Scene {

    private int vaoId, vboId, eboId;
    private Shader defaultShader;
    private Texture testTexture;
    
    private float[] vertexArray = {
        //Position           //color 
        450f, 325f, 0.0f,    1.0f, 0.0f, 0.0f, 1.0f,    1, 1,// Bottom Right
        300f, 400f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f,    0, 0,// Top Left
        450f, 425f, 0.0f,    1.0f, 0.0f, 1.0f, 1.0f,    1, 0,// Top Right
        300f, 300f, 0.0f,    1.0f, 1.0f, 0.0f, 1.0f,    0, 1// Bottom Left
    };

    //Counter Clockwise
    private int[] elementArray = {
        2, 1, 0,
        0, 1, 3
    };

    @Override
    public void init() {
        var testObject = new GameObject("shantz");
        testObject.addComponent(new SpriteRenderer());
        testObject.addComponent(new FontRenderer());
        this.addGameObjectsToScene(testObject);

        this.camera = new Camera(new Vector2f());
        this.defaultShader = new Shader("./assets/shaders/default_vertex_shader.glsl", "./assets/shaders/default_fragment_shader.glsl");
        defaultShader.compile();

        this.testTexture = new Texture("./assets/images/apple.jpg");
        
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        int positionsSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * Float.BYTES;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    public LevelEditorScene() {
        System.out.println("Level Editor Scene");
    }

    @Override
    public void update(double deltaTime) {
        defaultShader.use();
        
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", (float) glfwGetTime());

        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        defaultShader.detach();

        this.gameObjects.forEach(gameObject -> gameObject.update(deltaTime));
    }
}
