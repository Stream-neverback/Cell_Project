package proj;

public class Main {
    public static void main(String[] args){
        SimulationSystem system = SimulationSystem.getInstance();
        String mode = "gui";
        if (args[0].equals("--ter")) mode = "ter";
        Console console = new Console(mode);
        system.simulation(console, 1/15.0);
    }
}
