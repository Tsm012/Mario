package tsea;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import imgui.ImGui;
import renderer.DebugDraw;
import renderer.FrameBuffer;
import tsea.core.ImGuiLayer;
import tsea.input.KeyListener;
import tsea.input.MouseListener;
import tsea.scenes.LevelEditorScene;
import tsea.scenes.LevelScene;
import tsea.scenes.Scene;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {

	
	private String title;
	private int width, height;
	private FrameBuffer framebuffer;
	public static int getWidth() {
        return get().width;
    }

    public static int getHeight() {
        return get().height;
    }

    public static void setWidth(int newWidth) {
        get().width = newWidth;
    }

    public static void setHeight(int newHeight) {
        get().height = newHeight;
    }

	public float red, blue, green, alpha;

	private ImGuiLayer imguiLayer;
	private long windowPointer;
	private String glslVersion;

	enum SCENE {
		LEVEL_EDITOR,
		LEVEL
	}

	public Window(ImGuiLayer imguiLayer) {
		this.imguiLayer = imguiLayer;
		this.width = 1920;
		this.height = 1080;
		this.title = "Mario";
		this.red = this.blue = this.green = this.alpha = 1;
	}

	private static Window window = null;
	public static Window get() {
		if (Window.window == null) {
			Window.window = new Window(new ImGuiLayer());
		}
		return Window.window;
	}

	private static Scene currentScene = null;
	public Scene getScene() {
		return currentScene;
	}

	private void initWindow() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();

		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if ( !glfwInit() )
			throw new IllegalStateException("Unable to initialize GLFW");

		this.glslVersion = "#version 330";

		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
	
		windowPointer = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
		if (windowPointer == NULL)
			throw new RuntimeException("Failed to create the GLFW window");

		glfwSetCursorPosCallback(windowPointer, MouseListener::mousePositionCallback);
		glfwSetMouseButtonCallback(windowPointer, MouseListener::mouseButtonCallback);
		glfwSetScrollCallback(windowPointer, MouseListener::mouseScrollCallback);
		glfwSetKeyCallback(windowPointer, KeyListener::keyCallback);

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(windowPointer, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(
				windowPointer,
					(vidmode.width() - pWidth.get(0)) / 2,
					(vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically	

		glfwMakeContextCurrent(windowPointer);	
		glfwSwapInterval(1);
		glfwShowWindow(windowPointer);
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);

		this.framebuffer = new FrameBuffer(3840, 2160);
        glViewport(0, 0, 3840, 2160);

		Window.changeScene(SCENE.LEVEL_EDITOR);
	}

	public void destroy() {
		imguiLayer.dispose();
		ImGui.destroyContext();
		Callbacks.glfwFreeCallbacks(windowPointer);
		glfwDestroyWindow(windowPointer);
		glfwTerminate();
	}


	public static void changeScene(SCENE scene) {
		switch (scene) {
			case LEVEL_EDITOR:
				currentScene = new LevelEditorScene();
				break;
			case LEVEL:
				currentScene = new LevelScene();
				break;
			default:
				//Bad
				return;
		}
		currentScene.load();
		currentScene.init();
		currentScene.start();
	}

	public void run() {
		System.out.println("Hello LWJGL " + Version.getVersion() + "!");

		init();
		loop();

		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(windowPointer);
		glfwDestroyWindow(windowPointer);

		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}

	public void init() {
		initWindow();
		this.imguiLayer.init(this.windowPointer, this.glslVersion);
	}

	private void loop() {
		// Set the clear color
		glClearColor(red, green, blue, alpha);

		double beginTime = glfwGetTime();
		double endTime;
		double deltaTime = -1.0f;

		

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while (!glfwWindowShouldClose(windowPointer)) {
			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();

			DebugDraw.beginFrame();

			this.framebuffer.bind();

			glClearColor(red, green, blue, alpha);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			if (deltaTime >= 0) {
				DebugDraw.draw();
				currentScene.update(deltaTime);
			}
			this.framebuffer.unbind();
			imguiLayer.update(deltaTime, currentScene);

			glfwSwapBuffers(windowPointer); // swap the color buffers

			endTime = glfwGetTime();

			deltaTime = endTime - beginTime;
			beginTime = endTime;
		}

		currentScene.saveExit();
	}

	public static FrameBuffer getFramebuffer() {
        return get().framebuffer;
    }

    public static float getTargetAspectRatio() {
        return 16.0f / 9.0f;
    }
}