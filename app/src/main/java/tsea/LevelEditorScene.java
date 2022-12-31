package tsea;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.ARBVertexArrayObject.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;


public class LevelEditorScene extends Scene {

    private String vertexShaderSource = "";

    private String fragmentShaderSource = "";

    private int vertexId, fragmentId, shaderProgram, vaoId, vboId, eboId;    
    
    private float[] vertexArray = {
        //Position             //color 
        0.1f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f, // Bottom Left
        -0.4f, 0.1f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f, // Top Left
        0.2f, 0.4f, 0.0f,      0.0f, 0.0f, 1.0f, 1.0f, // Top Right
        -0.3f, -0.3f, 0.0f,    1.0f, 1.0f, 0.0f, 1.0f  // Bottom Right
    };

    //Counter Clockwise
    private int[] elementArray = {
        2, 1, 0,
        0, 1, 3
    };

    @Override
    public void init() {
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexId, vertexShaderSource);
        glCompileShader(vertexId);

        // Check For Errors
        if(glGetShaderi(vertexId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(vertexId, glGetShaderi(vertexId, GL_INFO_LOG_LENGTH)));
        }

        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentId, fragmentShaderSource);
        glCompileShader(fragmentId);

        // Check For Errors
        if(glGetShaderi(fragmentId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(fragmentId, glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH)));
        }

        // Link shaders
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexId);
        glAttachShader(shaderProgram, fragmentId);
        glLinkProgram(shaderProgram);

        // Check For Errors
        if(glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            System.out.println(glGetProgramInfoLog(shaderProgram, glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH)));
        }

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
        File defaultVertexShaderFile = new File("./assets/shaders/default_vertex_shader.glsl"); 
        File defaultFragmentShaderFile = new File("./assets/shaders/default_fragment_shader.glsl"); 
	  
        String line; 

        try {
            BufferedReader bufferReader = new BufferedReader(new FileReader(defaultVertexShaderFile));
            while ((line = bufferReader.readLine()) != null) {
                vertexShaderSource += (line + System.getProperty("line.separator")); 
            }
            bufferReader.close();
            
            bufferReader = new BufferedReader(new FileReader(defaultFragmentShaderFile));
            while ((line = bufferReader.readLine()) != null) {
                fragmentShaderSource += (line + System.getProperty("line.separator")); 
            }
            bufferReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        System.out.println("Level EditorScene");
    }

    @Override
    public void update(double deltaTime) {
        glUseProgram(shaderProgram);
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);
    }
}
