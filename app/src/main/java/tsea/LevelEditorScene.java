package tsea;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

import renderer.Shader;


public class LevelEditorScene extends Scene {

    private int vaoId, vboId, eboId;
    private Shader defaultShader;    
    
    private float[] vertexArray = {
        //Position               //color 
        475.5f, 350.5f, 0.0f,    1.0f, 0.0f, 0.0f, 1.0f, // Bottom Left
        350.5f, 450.5f, 0.0f,    0.0f, 1.0f, 0.0f, 1.0f, // Top Left
        475.5f, 450.5f, 0.0f,    0.0f, 0.0f, 1.0f, 1.0f, // Top Right
        350.5f, 350.5f, 0.0f,    1.0f, 1.0f, 0.0f, 1.0f  // Bottom Right
    };

    //Counter Clockwise
    private int[] elementArray = {
        2, 1, 0,
        0, 1, 3
    };

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        this.defaultShader = new Shader("./assets/shaders/default_vertex_shader.glsl", "./assets/shaders/default_fragment_shader.glsl");
        defaultShader.compile();
        
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
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;

        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

        
    }

    public LevelEditorScene() {
        System.out.println("Level Editor Scene");
    }

    @Override
    public void update(double deltaTime) {
        defaultShader.use();
        camera.position.x -= deltaTime * 50.0f;

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);
        defaultShader.detach();
    }
}
