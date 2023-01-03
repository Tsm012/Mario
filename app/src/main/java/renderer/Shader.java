package renderer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private int shaderProgramId;
    private boolean inUse = false;
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
        if(!inUse) {
            glUseProgram(shaderProgramId);
            inUse = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        inUse = false;
    }

    public void uploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadVec2f(String varName, Vector2f vector2f) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform2f(varLocation, vector2f.x, vector2f.y);
    }

    public void uploadVec3f(String varName, Vector3f vector3f) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform3f(varLocation, vector3f.x, vector3f.y, vector3f.z);
    }

    public void uploadVec4f(String varName, Vector4f vector4f) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform4f(varLocation, vector4f.x, vector4f.y, vector4f.z, vector4f.w);
    }

    public void uploadFloat(String varName, float floatValue) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform1f(varLocation, floatValue);
    }

    public void uploadInt(String varName, int intValue) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform1i(varLocation, intValue);
    }

    public void uploadTexture(String varName, int slot) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform1i(varLocation, slot
        );
    }

}
