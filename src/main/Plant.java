package main;

public class Plant implements MapElement{
    private Vector2d position;
    private int energy;

    public Plant(Vector2d position, int energy){
        this.position = position;
        this.energy = energy;
    }

    public Vector2d getPosition() {
        return position;
    }

    public int getEnergy() {
        return energy;
    }

    public String toString(){
        return "Plant (" + position.x + "," + position.y+") : " + energy;
    }
}
