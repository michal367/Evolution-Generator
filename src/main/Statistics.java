package main;

public class Statistics {
    public SingleStatistics singleStatistics;
    public FullStatistics fullStatistics;

    public Statistics(WorldMap map){
        singleStatistics = new SingleStatistics(map);
        fullStatistics = new FullStatistics(map);
    }

    public void update(){
        singleStatistics.update();
        fullStatistics.update(singleStatistics);
    }

    public void updateDeadAnimals(int deadAnimalLifetime){
        singleStatistics.updateDeadAnimals(deadAnimalLifetime);
        fullStatistics.updateDeadAnimals(deadAnimalLifetime);
    }
}
