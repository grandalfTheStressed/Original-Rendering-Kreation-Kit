package com.orkDevEngine.core.engine.screen.shader.managers;

import com.orkDevEngine.core.engine.screen.WindowManager;
import com.orkDevEngine.core.engine.utils.exceptions.ShaderManagerException;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL32.GL_GEOMETRY_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_CONTROL_SHADER;
import static org.lwjgl.opengl.GL40.GL_TESS_EVALUATION_SHADER;
import static org.lwjgl.opengl.GL43.GL_COMPUTE_SHADER;

public abstract class ShaderManager {

    protected WindowManager windowManager;
    protected int programPointer;
    protected Integer vertexShaderPointer,
            tessalationControlShaderPointer,
            tessalationEvaluationShaderPointer,
            fragmentShaderPointer,
            geometryShaderPointer,
            computeShaderPointer;

    protected UniformManager uniformManager;

    public ShaderManager() {

    }

    public void init() throws ShaderManagerException {
        programPointer = glCreateProgram();
        if(programPointer == 0)
            throw new ShaderManagerException("Could not create shader");

        uniformManager = new UniformManager(programPointer);
    }

    public void createVertexShader(String shaderCode) throws ShaderManagerException {
        vertexShaderPointer = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createTessalationControlShader(String shaderCode) throws ShaderManagerException {
        tessalationControlShaderPointer = createShader(shaderCode, GL_TESS_CONTROL_SHADER);
    }

    public void createTessalationEvaluationShader(String shaderCode) throws ShaderManagerException {
        tessalationEvaluationShaderPointer = createShader(shaderCode, GL_TESS_EVALUATION_SHADER);
    }

    public void createGeometryShader(String shaderCode) throws ShaderManagerException {
        geometryShaderPointer = createShader(shaderCode, GL_GEOMETRY_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws ShaderManagerException {
        fragmentShaderPointer = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    public void createComputeShader(String shaderCode) throws ShaderManagerException {
        computeShaderPointer = createShader(shaderCode, GL_COMPUTE_SHADER);
    }

    public int createShader(String shaderCode, int shaderType) throws ShaderManagerException{
        int shaderID = GL20.glCreateShader(shaderType);
        if(shaderID == 0)
            throw new ShaderManagerException("Error creating shader, type: " + shaderType);

        glShaderSource(shaderID, shaderCode);
        glCompileShader(shaderID);

        if(glGetShaderi(shaderID, GL_COMPILE_STATUS) == 0)
            throw new ShaderManagerException("Error Compiling shader code of type: " + shaderType
                    + "\nInfo: " + glGetShaderInfoLog(shaderID, 1024)
                    + "\nCode: " + shaderCode);

        glAttachShader(programPointer, shaderID);
        return shaderID;
    }

    public void link() throws ShaderManagerException {
        glLinkProgram(programPointer);
        if(glGetProgrami(programPointer, GL_LINK_STATUS) == 0)
            throw new ShaderManagerException("Error Linking shader code: "
                    + "\nInfo: " + glGetProgramInfoLog(programPointer, 1024));
        if(vertexShaderPointer != 0)
            glDetachShader(programPointer, vertexShaderPointer);
        if(fragmentShaderPointer != 0)
            glDetachShader(programPointer, fragmentShaderPointer);

        glValidateProgram(programPointer);
        if(glGetProgrami(programPointer, GL_VALIDATE_STATUS) == 0)
            throw new ShaderManagerException("Error Validating shader code: "
                    + "\nInfo: " + glGetProgramInfoLog(programPointer, 1024));
    }

    public void bind() {
        glUseProgram(programPointer);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup () {
        unbind();
        if(programPointer != 0)
            glDeleteProgram(programPointer);

    }

    public UniformManager getUniformManager() {
        return uniformManager;
    }
}
