package engine.shaders;

import engine.view.Camera;
import engine.view.Light;
import engine.util.Math;
import org.lwjgl.util.vector.Matrix4f;

import static engine.loader.Loader.NORMAL_COORDS_ATTRIBUTE_NUMBER;
import static engine.loader.Loader.TEXTURE_COORDS_ATTRIBUTE_NUMBER;
import static engine.loader.Loader.VERTEX_POSITIONS_ATTRIBUTE_NUMBER;

public class StaticShader extends ShaderProgram {
    // Path variables for shaders
    private static final String VERTEX_FILE_PATH = "./src/engine/shaders/VertexShader";
    private static final String FRAGMENT_FILE_PATH = "./src/engine/shaders/FragmentShader";

    // Uniform location variable names
    private static final String TRANSFORMATION_MATRIX = "transformationMatrix";
    private static final String PROJECTION_MATRIX = "projectionMatrix";
    private static final String VIEW_MATRIX = "viewMatrix";
    private static final String LIGHT_POSITION = "lightPosition";
    private static final String LIGHT_COLOR = "lightColor";
    private static final String SHINE_DAMPER = "shineDamper";
    private static final String REFLECTIVITY = "reflectivity";

    // Shader inputs
    private static final String POSITION = "position";
    private static final String TEXTURE_COORDS = "textureCoords";
    private static final String NORMAL_COORDS = "normalCoords";

    private int transformationMatrixLocation;
    private int projectionMatrixLocation;
    private int viewMatrixLocation;
    private int lightPositionLocation;
    private int lightColorLocation;
    private int shineDamperLocation;
    private int reflectivityLocation;

    public StaticShader() {
        super(VERTEX_FILE_PATH, FRAGMENT_FILE_PATH);
    }

    @Override
    public void bindAttributes() {
        bindAttribute(VERTEX_POSITIONS_ATTRIBUTE_NUMBER, POSITION);
        bindAttribute(TEXTURE_COORDS_ATTRIBUTE_NUMBER, TEXTURE_COORDS);
        bindAttribute(NORMAL_COORDS_ATTRIBUTE_NUMBER, NORMAL_COORDS);
    }

    @Override
    protected void getAllUniformLocations() {
        transformationMatrixLocation = getUniformLocation(TRANSFORMATION_MATRIX);
        projectionMatrixLocation = getUniformLocation(PROJECTION_MATRIX);
        viewMatrixLocation = getUniformLocation(VIEW_MATRIX);
        lightPositionLocation = getUniformLocation(LIGHT_POSITION);
        lightColorLocation = getUniformLocation(LIGHT_COLOR);
        shineDamperLocation = getUniformLocation(SHINE_DAMPER);
        reflectivityLocation = getUniformLocation(REFLECTIVITY);
    }

    public void loadTransformationMatrix(final Matrix4f matrix) {
        loadMatrix(transformationMatrixLocation, matrix);
    }

    public void loadProjectionMatrix(final Matrix4f matrix) {
        loadMatrix(projectionMatrixLocation, matrix);
    }

    public void loadLight(final Light light) {
        loadVector(lightPositionLocation, light.getPosition());
        loadVector(lightColorLocation, light.getColor());
    }

    public void loadShineVariables(final float shineDamper, final float reflectivity) {
        loadFloat(shineDamperLocation, shineDamper);
        loadFloat(reflectivityLocation, reflectivity);
    }

    public void loadViewMatrix(final Camera camera) {
        loadMatrix(viewMatrixLocation, Math.createViewMatrix(camera));
    }
}
