package renderer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int shaderProgramId;
    private String vertexSource = "";
    private String fragmentSource = "";

    public Shader(String vertexSourceFile, String fragmentSourceFile) {
        try {
            String line; 
            BufferedReader bufferReader = new BufferedReader(new FileReader(vertexSourceFile));
            while ((line = bufferReader.readLine()) != null) {
                this.vertexSource += (line + System.getProperty("line.separator")); 
            }
            bufferReader.close();
            
            bufferReader = new BufferedReader(new FileReader(fragmentSourceFile));
            while ((line = bufferReader.readLine()) != null) {
                this.fragmentSource += (line + System.getProperty("line.separator")); 
            }
            bufferReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    public void compile() {
        int vertexId, fragmentId;

        vertexId = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexId, this.vertexSource);
        glCompileShader(vertexId);

        // Check For Errors
        if(glGetShaderi(vertexId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(vertexId, glGetShaderi(vertexId, GL_INFO_LOG_LENGTH)));
        }

        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentId, this.fragmentSource);
        glCompileShader(fragmentId);

        // Check For Errors
        if(glGetShaderi(fragmentId, GL_COMPILE_STATUS) == GL_FALSE) {
            System.out.println(glGetShaderInfoLog(fragmentId, glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH)));
        }

        // Link shaders
        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexId);
        glAttachShader(shaderProgramId, fragmentId);
        glLinkProgram(shaderProgramId);

        // Check For Errors
        if(glGetProgrami(shaderProgramId, GL_LINK_STATUS) == GL_FALSE) {
            System.out.println(glGetProgramInfoLog(shaderProgramId, glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH)));
        }

    }

    public void use() {
        glUseProgram(shaderProgramId);

    }

    public void detach() {
        glUseProgram(0);
    }

}
