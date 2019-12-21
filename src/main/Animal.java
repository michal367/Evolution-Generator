package main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Animal implements MapElement {
    private Vector2d position;
    private Genotype genotype;
    private int energy;
    private int moveEnergy;

    private int birthEra;
    private int lifetime;

    private MapDirection orientation;

    private ArrayList<Animal> children;

    public Animal(Vector2d position, int energy, int moveEnergy){
        this.position = position;
        genotype = new Genotype();
        this.energy = energy;
        this.moveEnergy = moveEnergy;
        lifetime = 0;
        children = new ArrayList<>();
        orientation = MapDirection.getRandom();
        birthEra = 0;
    }

    public Animal(Animal animal1, Animal animal2){
        genotype = new Genotype(animal1.genotype, animal2.genotype);
        energy = (animal1.energy + animal2.energy)/4;
        animal1.energy -= animal1.energy/4;
        animal2.energy -= animal2.energy/4;
        animal1.children.add(this);
        animal2.children.add(this);
        moveEnergy = animal1.moveEnergy;
        lifetime = 0;
        children = new ArrayList<>();
        orientation = MapDirection.getRandom();
        birthEra = animal1.birthEra + animal1.lifetime;
    }


    public Vector2d move(){
        if(energy >= moveEnergy) {
            DirectionRotation randRotation = genotype.getRandMove();
            orientation = orientation.add(randRotation);
            position = position.add(orientation.toVector());
            energy -= moveEnergy;
        }
        energy -= moveEnergy;
        lifetime++;
        return position;
    }
    public Vector2d moveWithBoundaries(Vector2d size){
        if(energy >= moveEnergy) {
            DirectionRotation randRotation = genotype.getRandMove();
            orientation = orientation.add(randRotation);
            Vector2d randVector = orientation.toVector();
            position = new Vector2d((position.x + randVector.x + size.x) % size.x,
                    (position.y + randVector.y + size.y) % size.y);
        }
        energy -= moveEnergy;
        lifetime++;
        return position;
    }

    public void eat(int energy){
        this.energy += energy;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public boolean isDead(){
        return (energy < 0);
    }

    public Vector2d getPosition() {
        return position;
    }
    public int getEnergy() {
        return energy;
    }
    public Genotype getGenotype(){
        return genotype;
    }
    public int getLifetime() {
        return lifetime;
    }

    public MapDirection getOrientation() {
        return orientation;
    }

    public int getChildrenAmount() {
        return children.size();
    }
    public int getChildrenAmount(int minEra) {
        int size = 0;
        for(Animal child : children){
            if(child.birthEra >= minEra)
                size++;
        }
        return size;
    }
    private Set<Animal> getDescendantSet(){
        Set<Animal> descendants = new HashSet<>(children);
        for(Animal child : children)
            descendants.addAll(child.getDescendantSet());
        return descendants;
    }
    public int getDescendantAmount(){
        return getDescendantSet().size();
    }
    private Set<Animal> getDescendantSet(int minEra){
        Set<Animal> descendants = new HashSet<>();
        for(Animal child : children) {
            if(child.birthEra >= minEra) {
                descendants.add(child);
                descendants.addAll(child.getDescendantSet(minEra));
            }
        }
        return descendants;
    }
    public int getDescendantAmount(int minEra){
        return getDescendantSet(minEra).size();
    }


    public String toString(){
        return "Animal (" + position.x + "," + position.y+") : " + energy + " : " + genotype;
    }
}
