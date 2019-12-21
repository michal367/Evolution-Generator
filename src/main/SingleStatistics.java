package main;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class SingleStatistics {
    WorldMap map;

    public int era;
    public int animalAmount; // live
    public int plantAmount;
    public float animalsAverageEnergy; // live animals
    public int animalsAverageLifetime; // dead animals
    public float animalsAverageChildrenAmount; // live + dead
    public Genotype dominantGenotype;

    private int deadAnimalsAmount;
    private int deadAnimalsLifetimeSum;
    private int deadAnimalsChildren;

    public SingleStatistics(WorldMap map){
        this.map = map;
        era = -1;

        deadAnimalsAmount = 0;
        deadAnimalsLifetimeSum = 0;
        deadAnimalsChildren = 0;
    }

    public void update(){
        era++;

        animalAmount = map.getAnimals().size();
        plantAmount = map.getPlantsMap().size();

        int energySum = 0;
        int childrenSum = 0;
        for(Animal animal : map.getAnimals()){
            energySum += animal.getEnergy();
            childrenSum += animal.getChildrenAmount();
        }
        if(animalAmount != 0) {
            animalsAverageEnergy = (float)energySum / animalAmount;
            animalsAverageChildrenAmount = (float)childrenSum / animalAmount;
        }
        else {
            animalsAverageEnergy = 0;
            animalsAverageChildrenAmount = 0;
        }

        if(deadAnimalsAmount != 0){
            animalsAverageLifetime = deadAnimalsLifetimeSum / deadAnimalsAmount;
        }
        else
            animalsAverageLifetime = 0;

        dominantGenotype = getDominantGenotype();
    }

    public void updateDeadAnimals(int deadAnimalLifetime){
        deadAnimalsAmount++;
        deadAnimalsLifetimeSum += deadAnimalLifetime;
    }

    private Genotype getDominantGenotype(){
        HashMap<Genotype, Integer> genotypeCounter = new HashMap<>();
        for(Animal animal : map.getAnimals()){
            Genotype genotype = animal.getGenotype();
            genotypeCounter.merge(genotype, 1, Integer::sum);
        }
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
        return "Era: " + era + "\n" +
                "Animal Amount: " + animalAmount + "\n" +
                "Plant Amount: " + plantAmount + "\n" +
                "Dominant genotype: " + dominantGenotype.toString() + "\n" +
                "Animals Avg Energy: " + animalsAverageEnergy + "\n" +
                "Animals Avg Lifetime: " + animalsAverageLifetime + "\n" +
                "Avg Children Amount: " + animalsAverageChildrenAmount;
    }

    public String toString(){
        String dominantGenotypeStr;
        if(dominantGenotype != null)
            dominantGenotypeStr = dominantGenotype.toString();
        else
            dominantGenotypeStr = "-";

        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
        otherSymbols.setDecimalSeparator('.');
        otherSymbols.setGroupingSeparator(Character.MIN_VALUE);
        NumberFormat formatter = new DecimalFormat("#.##", otherSymbols);

        return era + ";" + animalAmount + ";" + plantAmount + ";" +
                formatter.format(animalsAverageEnergy) + ";" + animalsAverageLifetime + ";" +
                formatter.format(animalsAverageChildrenAmount) + ";" + dominantGenotypeStr;
    }
}