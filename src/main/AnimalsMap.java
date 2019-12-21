package main;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class AnimalsMap extends HashMap<Vector2d, ArrayList<Animal>> {

    private Comparator<Animal> energyComparator = new Comparator<Animal>() {
        @Override
        public int compare(Animal i1, Animal i2) {
            return (i2.getEnergy() - i1.getEnergy());
        }
    };

    public void add(Vector2d key, Animal value) {
        ArrayList<Animal> tempList = null;
        if (containsKey(key)) {
            tempList = get(key);
            if(tempList == null)
                tempList = new ArrayList<>();
            tempList.add(value);
            // sort elements by energy desc
            tempList.sort(energyComparator);
        } else {
            tempList = new ArrayList<>();
            tempList.add(value);
        }
        put(key,tempList);
    }

    public void sort(Vector2d key){ // sort ArrayList by energy
        ArrayList<Animal> tempList = get(key);
        // sort elements by energy desc
        tempList.sort(energyComparator);
        put(key,tempList);
    }
}
