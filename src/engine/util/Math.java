package engine.util;

import engine.view.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Math {
    public static Matrix4f createTransformationMatrix(final Vector3f translation,
                                                      final float rx,
                                                      final float ry,
                                                      final float rz,
                                                      final float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rx), new Vector3f(1 /* x */, 0 /* y */, 0 /* z */), matrix, matrix);
        Matrix4f.rotate((float) java.lang.Math.toRadians(ry), new Vector3f(0 /* x */, 1 /* y */, 0 /* z */), matrix, matrix);
        Matrix4f.rotate((float) java.lang.Math.toRadians(rz), new Vector3f(0 /* x */, 0 /* y */, 1 /* z */), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
        return matrix;
    }

    public static Matrix4f createViewMatrix(final Camera camera) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.rotate((float) java.lang.Math.toRadians(camera.getPitch()), new Vector3f(1 /* x */, 0 /* y */, 0 /* z */), matrix, matrix);
        Matrix4f.rotate((float) java.lang.Math.toRadians(camera.getYaw()), new Vector3f(0 /* x */, 1 /* y */, 0 /* z */), matrix, matrix);
        Vector3f negativeCameraPos = new Vector3f(-camera.getX(), -camera.getY(), -camera.getZ());
        Matrix4f.translate(negativeCameraPos, matrix, matrix);
        return matrix;
    }
}
