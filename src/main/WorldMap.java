package main;

import java.util.*;
import java.util.stream.Collectors;

public class WorldMap {
    private final Vector2d size;
    private Biome[][] biomes;

    private ArrayList<Animal> animals;
    private AnimalsMap animalsMap = new AnimalsMap();
    private Map<Vector2d, Plant> plantsMap = new HashMap<>();

    private Set<Vector2d> emptyCells;

    private Statistics stats;

    public WorldMap(int sizeX, int sizeY, double jungleRatio){
        size = new Vector2d(sizeX,sizeY);

        biomes = new Biome[sizeX][sizeY];
        generateBiomes(jungleRatio);

        animals = new ArrayList<>();

        emptyCells = new HashSet<>();
        for(int i=0; i<size.x; i++)
            for(int j=0; j<size.y; j++)
                emptyCells.add(new Vector2d(i,j));

        stats = new Statistics(this);
    }


    private void generateBiomes(double jungleRatio){
        for(int i=0; i<size.x; i++){
            for(int j=0; j<size.y; j++){
                biomes[i][j] = Biome.SAVANNA;
            }
        }

        Vector2d jungleSize = new Vector2d((int)(size.x*jungleRatio), (int)(size.y*jungleRatio));
        Vector2d jungleStart = new Vector2d((size.x-jungleSize.x)/2, (size.y-jungleSize.y)/2);
        Vector2d jungleEnd = new Vector2d(jungleStart.x+jungleSize.x-1, jungleStart.y+jungleSize.y-1);
        for(int i=jungleStart.x; i<=jungleEnd.x; i++){
            for(int j=jungleStart.y; j<=jungleEnd.y; j++){
                biomes[i][j] = Biome.JUNGLE;
            }
        }
    }

    public boolean isOccupied(Vector2d position){
        return (animalsMap.containsKey(position) || plantsMap.containsKey(position));
    }
    public boolean outOfMap(Vector2d position){
        return position.x < 0 || position.x >= size.x || position.y < 0 || position.y >= size.y;
    }



    public void removeDeadAnimals(){
        for(int i=0; i<animals.size(); i++){
            if(animals.get(i).isDead()) {
                stats.updateDeadAnimals(animals.get(i).getLifetime());
                removeAnimal(animals.get(i));
                i--;
            }
        }
    }
    public void moveAnimals(){
        for(Animal animal : animals){
            if(animalsMap.get(animal.getPosition()).size() == 1){
                animalsMap.remove(animal.getPosition());
                emptyCells.add(animal.getPosition());
            }
            else
                animalsMap.get(animal.getPosition()).remove(animal);

            Vector2d pos = animal.moveWithBoundaries(size);

            animalsMap.add(pos, animal);
            emptyCells.remove(pos);
        }
    }
    public void eat(){
        for(Map.Entry<Vector2d, ArrayList<Animal>> animals : animalsMap.entrySet()) {
            if(plantsMap.containsKey(animals.getKey())){
                Plant plant = plantsMap.get(animals.getKey());
                int plantEnergy = plant.getEnergy();
                removePlant(plant);

                int maxEnergy = animals.getValue().get(0).getEnergy(); // arrayList sorted by energy desc
                int maxEnergyCounter = 0;
                for(Animal animal : animals.getValue()){
                    if(animal.getEnergy() == maxEnergy)
                        maxEnergyCounter++;
                    else
                        break;
                }

                int dividedPlantEnergy = plantEnergy / maxEnergyCounter;
                for(int i=0; i<maxEnergyCounter; i++){
                    animals.getValue().get(i).eat(dividedPlantEnergy);
                }
            }
        }
    }
    public void reproduce(int minEnergy){
        ArrayList<Animal> children = new ArrayList<>();

        for (Map.Entry<Vector2d, ArrayList<Animal>> entry : animalsMap.entrySet()) {
            if(entry.getValue().size() >= 2){
                // list is sorted desc by energy - we get 2 animals with biggest energy
                Animal animal1 = entry.getValue().get(0);
                Animal animal2 = entry.getValue().get(1);
                if(animal1.getEnergy() >= minEnergy && animal2.getEnergy() >= minEnergy){
                    Animal child = new Animal(animal1, animal2);

                    Vector2d newPos = getRandEmptyPosAround(animal1.getPosition());
                    if(newPos != null)
                        child.setPosition(newPos);
                    else
                        child.setPosition(getRandPosAround(animal1.getPosition()));
                    children.add(child);

                    animalsMap.sort(animal1.getPosition());
                }
            }
        }
        for(Animal child : children) {
            placeAnimal(child);
            animalsMap.sort(child.getPosition());
        }
    }


    // rand position
    public Vector2d getRandEmptyPos(){
        if(emptyCells.size() > 0) {
            Random rand = new Random();
            int index = rand.nextInt(emptyCells.size());
            return (Vector2d) emptyCells.toArray()[index];
        }
        return null;
    }
    public Vector2d getRandEmptyPos(Biome biome){
        List<Vector2d> emptyCellsInBiome = emptyCells
                .stream()
                .filter(p -> biomes[p.x][p.y] == biome)
                .map(p -> (Vector2d) p)
                .collect(Collectors.toList());
        if(emptyCellsInBiome.size() > 0) {
            Random rand = new Random();
            int index = rand.nextInt(emptyCellsInBiome.size());
            return (Vector2d) emptyCellsInBiome.toArray()[index];
        }
        return null;
    }
    public Vector2d getRandEmptyPosAround(Vector2d position){
        ArrayList<Vector2d> emptyCellsAround = new ArrayList<>();

        for(int i=-1; i<=1; i++){
            for(int j=-1; j<=1; j++){
                Vector2d pos = position.add(new Vector2d(i,j));
                pos = new Vector2d((pos.x + size.x)%size.x,(pos.y + size.y)%size.y);
                if(emptyCells.contains(pos))
                    emptyCellsAround.add(pos);
            }
        }
        if(emptyCellsAround.size() > 0) {
            Random rand = new Random();
            int index = rand.nextInt(emptyCellsAround.size());
            return emptyCellsAround.get(index);
        }
        return null;
    }
    public Vector2d getRandPosAround(Vector2d position){
        ArrayList<Vector2d> cellsAround = new ArrayList<>();

        for(int i=-1; i<=1; i++){
            for(int j=-1; j<=1; j++){
                if(!(i == 0 && j == 0)) {
                    Vector2d pos = position.add(new Vector2d(i, j));
                    pos = new Vector2d((pos.x + size.x) % size.x, (pos.y + size.y) % size.y);
                    cellsAround.add(pos);
                }
            }
        }
        Random rand = new Random();
        int index = rand.nextInt(cellsAround.size());
        return cellsAround.get(index);
    }

    // ANIMALS
    public void generateAnimal(int energy, int moveEnergy){
        Vector2d randPos = getRandEmptyPos();
        if(randPos != null) {
            Animal animal = new Animal(randPos, energy, moveEnergy);
            placeAnimal(animal);
        }
    }
    public void generateAnimals(int n, int energy, int moveEnergy){
        for(int i=0; i<n; i++){
            generateAnimal(energy, moveEnergy);
        }
    }
    public void placeAnimal(Animal animal){
        animals.add(animal);
        animalsMap.add(animal.getPosition(), animal);
        emptyCells.remove(animal.getPosition());
    }
    public void removeAnimal(Animal animal){
        if(animalsMap.get(animal.getPosition()).size() == 1){
            animalsMap.remove(animal.getPosition());
            emptyCells.add(animal.getPosition());
        }
        else
            animalsMap.get(animal.getPosition()).remove(animal);
        animals.remove(animal);
    }


    // PLANTS
    public void generatePlant(int energy){
        Vector2d randPos = getRandEmptyPos();
        if(randPos != null) {
            Plant plant = new Plant(randPos, energy);
            placePlant(plant);
        }
    }
    public void generatePlant(Biome biome, int energy){
        Vector2d randPos = getRandEmptyPos(biome);
        if(randPos != null) {
            Plant plant = new Plant(randPos, energy);
            placePlant(plant);
        }
    }
    public void generatePlants(int n, int energy){
        for(int i=0; i<n; i++){
            generatePlant(energy);
        }
    }
    public void placePlant(Plant plant){
        plantsMap.put(plant.getPosition(), plant);
        emptyCells.remove(plant.getPosition());
    }
    public void removePlant(Plant plant){
        plantsMap.remove(plant.getPosition());
        emptyCells.add(plant.getPosition());
    }


    public ArrayList<Animal> getAnimalsWithGenotype(Genotype genotype){
        ArrayList<Animal> result = new ArrayList<>();
        if(genotype != null) {
            for (Animal animal : animals) {
                if (animal.getGenotype().equals(genotype))
                    result.add(animal);
            }
        }
        return result;
    }
    public ArrayList<Animal> getAnimalsWithDominantGenotype(){
        return getAnimalsWithGenotype(stats.singleStatistics.dominantGenotype);
    }

    public ArrayList<Animal> getAnimals() {
        return animals;
    }
    public Map<Vector2d, ArrayList<Animal>> getAnimalsMap(){
        return animalsMap;
    }
    public Map<Vector2d, Plant> getPlantsMap() {
        return plantsMap;
    }

    public Biome[][] getBiomes(){
        return biomes;
}
    public Biome getBiome(Vector2d position){
        return biomes[position.x][position.y];
    }
    public Vector2d getSize(){
        return size;
    }

    public void updateStats(){
        stats.update();
    }
    public Statistics getStats() {
        return stats;
    }
}
