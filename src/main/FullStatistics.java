package main;

import java.util.HashMap;
import java.util.Map;

public class FullStatistics {
    private WorldMap map;
    private int era;

    private float sumAnimalsAmount;
    private float sumPlantsAmount;

    private int deadAnimalsAmount;
    private int sumLifetime;

    private float sumEnergyAvg;
    private float sumChildrenAvg;

    private HashMap<Genotype, Integer> genotypeCounter = new HashMap<>();

    public FullStatistics(WorldMap map){
        this.map = map;

        era = -1;
        sumAnimalsAmount = 0;
        sumPlantsAmount = 0;
        sumEnergyAvg = 0;
        sumChildrenAvg = 0;

        deadAnimalsAmount = 0;
        sumLifetime = 0;
    }

    public void update(SingleStatistics singleStats){
        era++;

        sumAnimalsAmount += singleStats.animalAmount;
        sumPlantsAmount += singleStats.plantAmount;

        sumEnergyAvg += singleStats.animalsAverageEnergy;
        System.out.println(sumEnergyAvg);
        sumChildrenAvg += singleStats.animalsAverageChildrenAmount;
        System.out.println(sumChildrenAvg);

        updateGenotype();
    }
    public void updateGenotype(){
        for(Animal animal : map.getAnimals()){
            Genotype genotype = animal.getGenotype();
            genotypeCounter.merge(genotype, 1, Integer::sum);
        }
    }
    public void updateDeadAnimals(int deadAnimalLifetime){
        deadAnimalsAmount++;
        sumLifetime += deadAnimalLifetime;
    }


    public Genotype getMaxGenotype(){
        if(genotypeCounter.size() > 0) {
            Map.Entry<Genotype, Integer> maxEntry = null;
            for (Map.Entry<Genotype, Integer> entry : genotypeCounter.entrySet()) {
                if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
                    maxEntry = entry;
            }
            return maxEntry.getKey();
        }
        else
            return null;
    }

    public String getFormattedTextStats(){
        if(era == 0)
            return "Amount of Eras: " + era + "\n" +
                    "Avg Animals Amount: 0" + "\n" +
                    "Avg Plants Amount: 0" + "\n" +
                    "Dominant genotype: -"+  "\n" +
                    "Avg Animals Energy: 0" + "\n" +
                    "Avg Animals Lifetime: 0" + "\n" +
                    "Avg Animals Avg Children Amount: 0";

        float avgLifetime = 0;
        if(deadAnimalsAmount != 0)
            avgLifetime = (float)sumLifetime/(deadAnimalsAmount);
        return "Amount of Eras: " + era + "\n" +
                "Avg Animals Amount: " + sumAnimalsAmount/era + "\n" +
                "Avg Plants Amount: " + sumPlantsAmount/era + "\n" +
                "Dominant genotype: " + getMaxGenotype().toString() + "\n" +
                "Avg Animals Energy: " + sumEnergyAvg/era + "\n" +
                "Avg Animals Lifetime: " + avgLifetime + "\n" +
                "Avg Animals Avg Children Amount: " + sumChildrenAvg/era;
    }
}
