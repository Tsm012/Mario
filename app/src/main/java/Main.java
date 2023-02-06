import tsea.Window;
import tsea.core.ImGuiLayer;

public class Main {
    public static void main(String[] args){
        Window window = new Window(new ImGuiLayer());
        window.run();
        window.destroy();
    }
}
