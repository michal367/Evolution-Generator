package main;

import java.util.Arrays;
import java.util.Random;

public class Genotype {
    private DirectionRotation[] genotype;
    public int genotypeSize;
    public int genesAmount;

    public Genotype(){
        genotypeSize = 32;
        genotype = new DirectionRotation[genotypeSize];
        genesAmount = DirectionRotation.values().length;

        generateGenotype();
    }

    public Genotype(Genotype gen1, Genotype gen2){
        genotypeSize = 32;
        genotype = new DirectionRotation[genotypeSize];
        genesAmount = DirectionRotation.values().length;

        generateGenotype(gen1, gen2);
    }

    private void generateGenotype(){
        for(int i=0; i<genesAmount; i++)
            genotype[i] = DirectionRotation.getDirectionRotation(i);

        Random rand = new Random();
        for(int i=genesAmount; i<genotypeSize; i++)
            genotype[i] = DirectionRotation.getDirectionRotation(rand.nextInt(genesAmount));

        Arrays.sort(genotype);
    }
    private void generateGenotype(Genotype gen1, Genotype gen2){
        int[] indexes = new int[2];
        Random rand = new Random();
        indexes[0] = rand.nextInt(genotypeSize);
        do {
            indexes[1] = rand.nextInt(genotypeSize);
        }while(indexes[1] == indexes[0]);
        Arrays.sort(indexes);

        for(int i=0; i<indexes[0]; i++)
            genotype[i] = gen1.genotype[i];
        for(int i=indexes[0]; i<indexes[1]; i++)
            genotype[i] = gen2.genotype[i];
        for(int i=indexes[1]; i<genotypeSize; i++)
            genotype[i] = gen1.genotype[i];

        int generatedGenes = 0;
        int[] amount = new int[genesAmount];
        Arrays.fill(amount, 0);
        for(int i=0; i<genotypeSize; i++){
            int g = genotype[i].getNumber();
            if(amount[g] == 0){
                generatedGenes++;
            }
            amount[g]++;
        }
        for(int i=0; i<genesAmount; i++)
            if(amount[i] == 0){
                int randIndex;
                do {
                    randIndex = rand.nextInt(genotypeSize);
                }while(amount[genotype[randIndex].getNumber()] == 1); // don't change value that occurs only once
                genotype[randIndex] = DirectionRotation.getDirectionRotation(i);
                amount[i]++;
            }

        Arrays.sort(genotype);
    }

    public int[] getGenesAmount(){
        int[] genesCounter = new int[genesAmount];
        Arrays.fill(genesCounter, 0);
        for(int i=0; i<genotypeSize; i++)
            genesCounter[genotype[i].getNumber()]++;
        return genesCounter;
    }
    public DirectionRotation getDominantGene(){
        int[] genesCounter = getGenesAmount();
        int maxAmount = -1;
        DirectionRotation maxIndex = null;
        for(int i=0; i<genesAmount; i++){
            if(genesCounter[i] > maxAmount) {
                maxAmount = genesCounter[i];
                maxIndex = DirectionRotation.getDirectionRotation(i);
            }
        }
        return maxIndex;
    }

    public DirectionRotation getRandMove(){
        Random rand = new Random();
        int index = rand.nextInt(genotypeSize);
        return genotype[index];
    }

    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Genotype))
            return false;
        Genotype that = (Genotype) other;
        return (Arrays.equals(this.genotype, that.genotype));
    }
    public int hashCode() {
        int hash = 13;
        for(int i=0; i<genotypeSize; i++){
            hash += i*genotype[i].getNumber();
        }
        return hash;
    }

    public String toString(){
        StringBuilder builder = new StringBuilder();
        for (DirectionRotation value : genotype) {
            builder.append(value.getNumber());
        }
        return builder.toString();
    }
}
