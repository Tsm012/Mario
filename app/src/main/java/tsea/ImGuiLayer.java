package tsea;

import imgui.ImGui;

public class ImGuiLayer {
    private boolean showText;

    public void imgui() {
        ImGui.begin("windows");

        if(ImGui.button("I am a button")){
            showText = true;
        }

        if (showText) {
            ImGui.text("You click a button");
            ImGui.sameLine();
            if(ImGui.button("Stop")){
                showText = false;
            }
        }

        ImGui.end();
    }
}
