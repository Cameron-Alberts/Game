package engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager implements AutoCloseable {
    private static final String TITLE = "Game";
    private static final int WIDTH = 1280;
    private static final int HEIGHT =  720;
    private static final int FPS_CAP = 144;

    public void create() throws LWJGLException {
        ContextAttribs contextAttribs = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);
        Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
        Display.setTitle(TITLE);
        Display.create(new PixelFormat(), contextAttribs);
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
    }

    public boolean isClosedRequested() {
        return Display.isCloseRequested();
    }

    public void update() {
        Display.sync(FPS_CAP);
        Display.update();
    }

    @Override
    public void close() {
        Display.destroy();
    }
}
