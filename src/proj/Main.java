package proj;

import java.io.OutputStreamWriter;

public class Main {
    public static void main(String[] args){
        SimulationSystem system = SimulationSystem.getInstance();
        double dt = 1.0/15.0;
        boolean benchmark = false;
        boolean isGUIMode = true;
        boolean isMouseMode = false;
        boolean isDeleteMode = false;
        boolean isChangeColorMode = false;
        if(args.length != 0){
            for(String s:args){
                if("playerModeChangeColor".equals(s)){
                    isMouseMode = true;
                    isChangeColorMode = true;
                }
                else if("playerModeDelete".equals(s)) {
                    isMouseMode = true;
                    isDeleteMode = true;
                }
                else if("--ter".equals(s))
                    isGUIMode = false;
                else if("benchmark".equals(s))
                    benchmark = true;
            }
        }
        system.benchmark = benchmark;
        system.isGUIMode = isGUIMode;
        if (isGUIMode && isMouseMode) {
            system.isMouseMode = true;
            if (isChangeColorMode) {
                system.isChangeColorMode = true;
                system.isDeleteMode = false;
            }
            else if (isDeleteMode) {
                system.isChangeColorMode = false;
                system.isDeleteMode = true;
            }
        }
        String mode = isGUIMode ? "gui" : "ter";
        Console console = new Console(mode);
        system.simulation(console, dt);
    }
}
