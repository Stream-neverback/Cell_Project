package proj;

import java.io.OutputStreamWriter;

import static proj.RandomSampleGenerate.SampleGenerate;

public class Main {
    public static void main(String[] args){
        SimulationSystem system = SimulationSystem.getInstance();
        double dt = 1.0/15.0;
        boolean benchmark = false;
        boolean isGUIMode = true;
        boolean isMouseMode = false;
        boolean isDeleteMode = false;
        boolean isChangeColorMode = false;
        boolean isBruteMode = false;
        boolean isplayerModeRandomGen = false;
        boolean showFrameRate = false;
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
                else if("--terminal".equals(s))
                    isGUIMode = false;
                else if("benchmark".equals(s))
                    benchmark = true;
                else if("brute".equals(s))
                    isBruteMode = true;
                else if ("playerModeRandomGen".equals(s))
                    isplayerModeRandomGen = true;
                else if ("showFrameRate".equals(s))
                    showFrameRate = true;
            }
        }
        system.benchmark = benchmark;
        system.isGUIMode = isGUIMode;
        if (isGUIMode) system.showFrameRate = true;
        else if (showFrameRate) system.showFrameRate = true;
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
        if (isplayerModeRandomGen) SampleGenerate();
        Console console = new Console(mode, isplayerModeRandomGen);
        if (!isBruteMode) system.simulation(console, dt);
        else system.simulationBrute(console, dt);
    }
}
