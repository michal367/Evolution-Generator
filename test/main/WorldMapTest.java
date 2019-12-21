package main;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Map;

public class WorldMapTest {
    int width = 10;
    int height = 10;
    double jungleRatio = 0.4;
    int startEnergy = 200;
    int moveEnergy = 10;
    int plantEnergy = 40;
    int animalAmount = 5;
    int plantsAmount = 5;
    @Test
    public void removeDeadAnimals() {
        WorldMap map = new WorldMap(width,height,jungleRatio);
        Animal animal1 = new Animal(new Vector2d(2,3), 20, moveEnergy);
        Animal animal2 = new Animal(new Vector2d(2,3), 50, moveEnergy);
        Animal animal3 = new Animal(new Vector2d(2,3), 0, moveEnergy);
        Animal animal4 = new Animal(new Vector2d(2,3), -20, moveEnergy);
        Animal animal5 = new Animal(new Vector2d(2,3), -1, moveEnergy);
        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        map.placeAnimal(animal3);
        map.placeAnimal(animal4);
        map.placeAnimal(animal5);

        map.removeDeadAnimals();
        ArrayList<Animal> animals = map.getAnimals();

        Assert.assertTrue(animals.indexOf(animal1) != -1);
        Assert.assertTrue(animals.indexOf(animal2) != -1);
        Assert.assertTrue(animals.indexOf(animal3) != -1);
        Assert.assertTrue(animals.indexOf(animal4) == -1);
        Assert.assertTrue(animals.indexOf(animal5) == -1);
    }

    @Test
    public void moveAnimals() {
        WorldMap map = new WorldMap(width,height,jungleRatio);

        Vector2d pos1 = new Vector2d(2,3);
        Vector2d pos2 = new Vector2d(4,6);
        Vector2d pos3 = new Vector2d(6,9);

        Animal animal1 = new Animal(pos1, startEnergy, moveEnergy);
        Animal animal2 = new Animal(pos2, startEnergy, moveEnergy);
        Animal animal3 = new Animal(pos3, startEnergy, moveEnergy);
        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        map.placeAnimal(animal3);

        map.moveAnimals();

        Assert.assertTrue(pos1.equals(animal1.getPosition().subtract(animal1.getOrientation().toVector())));
        Assert.assertTrue(pos2.equals(animal2.getPosition().subtract(animal2.getOrientation().toVector())));

        Vector2d newPos3 = animal3.getPosition().subtract(animal3.getOrientation().toVector());
        newPos3 = new Vector2d((newPos3.x + width)%width, (newPos3.y + height)%height); // correct pos if animal came out of the world
        Assert.assertTrue(pos3.equals(newPos3));
    }

    @Test
    public void eat() {
        WorldMap map = new WorldMap(width,height,jungleRatio);

        Animal animal1 = new Animal(new Vector2d(2,3), startEnergy, moveEnergy);
        Animal animal2 = new Animal(new Vector2d(2,3), startEnergy+30, moveEnergy);
        Animal animal3 = new Animal(new Vector2d(2,3), startEnergy+30, moveEnergy);

        Animal animal4 = new Animal(new Vector2d(6,8), startEnergy, moveEnergy);

        Plant plant1 = new Plant(new Vector2d(2,3), plantEnergy);

        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        map.placeAnimal(animal3);
        map.placeAnimal(animal4);
        map.placePlant(plant1);

        map.eat();

        Assert.assertTrue(animal1.getEnergy() == startEnergy); // animal1 didn't eat plant
        Assert.assertTrue(animal2.getEnergy() == startEnergy+30 + plantEnergy/2); // animal2 ate half of the plant
        Assert.assertTrue(animal3.getEnergy() == startEnergy+30 + plantEnergy/2); // animal3 ate half of the plant
        Assert.assertTrue(animal4.getEnergy() == startEnergy); // animal4 didn't eat plant
    }

    @Test
    public void reproduce() {
        WorldMap map = new WorldMap(width,height,jungleRatio);

        Vector2d pos1 = new Vector2d(2,3);
        Vector2d pos2 = new Vector2d(6,9);

        Animal animal1 = new Animal(pos1, 20, moveEnergy);
        Animal animal2 = new Animal(pos1, 50, moveEnergy);

        Animal animal3 = new Animal(pos2, 200, moveEnergy);
        Animal animal4 = new Animal(pos2, 500, moveEnergy);
        Animal animal5 = new Animal(pos2, 20, moveEnergy);
        Animal animal6 = new Animal(pos2, 50, moveEnergy);

        map.placeAnimal(animal1);
        map.placeAnimal(animal2);
        map.placeAnimal(animal3);
        map.placeAnimal(animal4);
        map.placeAnimal(animal5);
        map.placeAnimal(animal6);

        map.reproduce(startEnergy/2); // minEnergy = 100

        Map<Vector2d, ArrayList<Animal>> animalsMap = map.getAnimalsMap();
        boolean isNewAnimalExist = checkIsAnimalAround(animalsMap, pos1);
        Assert.assertFalse(isNewAnimalExist); // in first case there should be no animal

        isNewAnimalExist = checkIsAnimalAround(animalsMap, pos2);
        Assert.assertTrue(isNewAnimalExist); // in second case there should be animal
    }
    private boolean checkIsAnimalAround(Map<Vector2d, ArrayList<Animal>> animalsMap, Vector2d position){
        for(int i=-1; i<=1; i++){
            for(int j=-1; j<=1; j++){
                if(!(i == 0 && j == 0)) {
                    Vector2d pos = position.add(new Vector2d(i, j));
                    pos = new Vector2d((pos.x + width) % width, (pos.y + height) % height);
                    if(animalsMap.containsKey(pos))
                        return true;
                }
            }
        }
        return false;
    }
}
