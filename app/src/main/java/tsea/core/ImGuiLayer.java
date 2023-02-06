package tsea.core;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiConfigFlags;
import imgui.gl3.ImGuiImplGl3;
import imgui.glfw.ImGuiImplGlfw;
import tsea.scenes.Scene;

import static org.lwjgl.glfw.GLFW.*;

public class ImGuiLayer {
    private boolean showText;
    private final ImGuiImplGlfw imGuiGlfw = new ImGuiImplGlfw();
	private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public void imgui() {
    }

    public void init(long windowPointer, String glslVersion) {
        ImGui.createContext();
		ImGuiIO io = ImGui.getIO();
		io.addConfigFlags(ImGuiConfigFlags.ViewportsEnable);
        io.setIniFilename("imgui.ini");
		imGuiGlfw.init(windowPointer, true);
		imGuiGl3.init(glslVersion);
    }

    public void dispose() {
        this.imGuiGlfw.dispose();
		this.imGuiGl3.dispose();
    }

    public void update(double deltaTime, Scene currentScene) {
        imGuiGlfw.newFrame();
        ImGui.newFrame();

        currentScene.sceneImgui();
        imgui();

        ImGui.render();
        imGuiGl3.renderDrawData(ImGui.getDrawData());

        if (ImGui.getIO().hasConfigFlags(ImGuiConfigFlags.ViewportsEnable)) {
            final long backupWindowPtr = glfwGetCurrentContext();
            ImGui.updatePlatformWindows();
            ImGui.renderPlatformWindowsDefault();
            glfwMakeContextCurrent(backupWindowPtr);
        }
    }
}
