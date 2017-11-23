package engine.shaders;

import engine.renderer.Renderer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static engine.shaders.ShaderProgram.ShaderType.FRAGMENT_SHADER;
import static engine.shaders.ShaderProgram.ShaderType.VERTEX_SHADER;

public abstract class ShaderProgram implements AutoCloseable {
    private static final FloatBuffer MATRIX_BUFFER = BufferUtils.createFloatBuffer(4 * 4);

    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    public enum ShaderType {
        VERTEX_SHADER(GL20.GL_VERTEX_SHADER),
        FRAGMENT_SHADER(GL20.GL_FRAGMENT_SHADER);

        private final int value;

        ShaderType(final int value) {
            this.value = value;
        }
    }

    public ShaderProgram(final String vertexFilePath, final String fragmentFilePath) {
        vertexShaderID = loadShader(vertexFilePath, VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFilePath, FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(final String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    protected void loadFloat(final int location, final float value) {
        GL20.glUniform1f(location, value);
    }

    protected void loadVector(final int location, final Vector3f vector) {
        GL20.glUniform3f(location, vector.getX(), vector.getY(), vector.getZ());
    }

    protected void loadBoolean(final int location, final boolean value) {
        int boolValue = value ? 1 : 0;
        GL20.glUniform1i(location, boolValue);
    }

    protected void loadMatrix(final int location, final Matrix4f matrix) {
        matrix.store(MATRIX_BUFFER);
        MATRIX_BUFFER.flip();
        GL20.glUniformMatrix4(location, false /* transpose */, MATRIX_BUFFER);
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(Renderer.UNBIND);
    }

    @Override
    public void close() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(final int attribute, final String variableName) {
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

   private static int loadShader(final String filePath, final ShaderType shaderType) {
        StringBuilder shaderSource = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                shaderSource.append(line).append('\n');
            }
        } catch (IOException e) {
            System.err.println("Could't read file " + filePath);
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(shaderType.value);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            System.err.println(GL20.glGetShaderInfoLog(shaderID, 500 /* maxLength */));
            System.err.println("Could not compile shader.");
            System.exit(-1);
        }
        return shaderID;
   }
}
