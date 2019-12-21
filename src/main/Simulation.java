package main;

public class Simulation {
    private WorldMap map;

    //parameters
    public int startEnergy;
    private int moveEnergy;
    private int plantEnergy;
    private int animalAmount;
    private int plantsAmount;

    public Simulation(int width, int height, double jungleRatio, int startEnergy, int moveEnergy, int plantEnergy, int animalAmount, int plantsAmount){
        map = new WorldMap(width, height, jungleRatio);
        this.startEnergy = startEnergy;
        this.moveEnergy = moveEnergy;
        this.plantEnergy = plantEnergy;
        this.animalAmount = animalAmount;
        this.plantsAmount = plantsAmount;

        map.generateAnimals(animalAmount, startEnergy, moveEnergy);
        map.generatePlants(plantsAmount, plantEnergy);

        map.updateStats();
    }

    public void run(){
        map.removeDeadAnimals();
        map.moveAnimals();
        map.eat();
        map.reproduce(startEnergy/2);
        map.generatePlant(Biome.SAVANNA, plantEnergy);
        map.generatePlant(Biome.JUNGLE, plantEnergy);

        map.updateStats();
    }

    public WorldMap getMap(){
        return map;
    }


}
