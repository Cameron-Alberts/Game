package engine.view;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {
    private Vector3f position;
    private float pitch;
    private float yaw;
    private float roll;

    public Camera() {
        position = new Vector3f(0, 0, 0);
    }

    public void move() {
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            position.z -= 0.25f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            position.x += 0.25f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            position.x -= 0.25f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            position.z += 0.25f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_F)) {
            position.y -= 0.25f;
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            position.y += 0.25f;
        }
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public float getZ() {
        return position.getZ();
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
