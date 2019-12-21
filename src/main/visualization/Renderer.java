package main.visualization;

import main.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Renderer extends JPanel implements MouseListener {
    private SimulationWidget parent;

    private Vector2d tileSize;
    private Vector2d size;
    private WorldMap map;
    private int startEnergy;

    private ArrayList<Animal> followedAnimals;

    public Renderer(SimulationWidget parent, WorldMap map, Vector2d rendererSize, int startEnergy){
        this.parent = parent;

        this.map = map;
        size = rendererSize;
        tileSize = new Vector2d(size.x/map.getSize().x, size.y/map.getSize().y);
        this.startEnergy = startEnergy;
        followedAnimals = new ArrayList<>();

        addMouseListener(this);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(map != null){
            Graphics2D g2 = (Graphics2D)g;

            // biomes
            for(int i=0; i<map.getSize().x; i++){
                for(int j=0; j<map.getSize().y; j++){
                    int posX = i*tileSize.x;
                    int posY = j*tileSize.y;

                    if(map.getBiomes()[i][j] == Biome.SAVANNA){
                        g2.setColor(Colors.savanna);
                        g2.fillRect(posX,posY,tileSize.x,tileSize.y);

                        //border
                        g2.setColor(Color.BLACK);
                        g2.setStroke(new BasicStroke(1));
                        g2.drawRect(posX,posY,tileSize.x,tileSize.y);
                    }
                    else if(map.getBiomes()[i][j] == Biome.JUNGLE){
                        g2.setColor(Colors.jungle);
                        g2.fillRect(posX,posY,tileSize.x,tileSize.y);

                        //border
                        g2.setColor(Color.BLACK);
                        g2.setStroke(new BasicStroke(1));
                        g2.drawRect(posX,posY,tileSize.x,tileSize.y);
                    }
                }
            }

            // plants
            for (Map.Entry<Vector2d, Plant> plantsMap : map.getPlantsMap().entrySet()) {
                int posX = (int)((plantsMap.getKey().x+0.2)*tileSize.x);
                int posY = (int)((plantsMap.getKey().y+0.2)*tileSize.y);

                g2.setColor(Colors.plant);
                g2.fillRect(posX, posY, (int) (0.6 * tileSize.x), (int) (0.6 * tileSize.y));
            }

            // animals
            for (Map.Entry<Vector2d, ArrayList<Animal>> animalsMap : map.getAnimalsMap().entrySet()) {
                int posX = (int)((animalsMap.getKey().x+0.2)*tileSize.x);
                int posY = (int)((animalsMap.getKey().y+0.2)*tileSize.y);

                Animal animal = animalsMap.getValue().get(0);

                if(animal.getEnergy() >= startEnergy)
                    g2.setColor(Colors.animalEnergy1);
                else if(animal.getEnergy() >= 3*startEnergy/4)
                    g2.setColor(Colors.animalEnergy2);
                else if(animal.getEnergy() >= 2*startEnergy/4)
                    g2.setColor(Colors.animalEnergy3);
                else if(animal.getEnergy() >= startEnergy/4)
                    g2.setColor(Colors.animalEnergy4);
                else
                    g2.setColor(Colors.animalEnergy5);

                g2.fillRect(posX, posY, (int) (0.6 * tileSize.x), (int) (0.6 * tileSize.y));
            }

            // followed animal - animal that user clicked
            for(Animal animal : followedAnimals){
                int posX = (int)((animal.getPosition().x+0.2)*tileSize.x);
                int posY = (int)((animal.getPosition().y+0.2)*tileSize.y);

                if(!animal.isDead())
                    g2.setColor(Colors.followedAnimalBorder);
                else
                    g2.setColor(Colors.followedDeadAnimalBorder);
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(posX, posY, (int) (0.6 * tileSize.x), (int) (0.6 * tileSize.y));
            }
        }
    }


    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int posX = x/tileSize.x;
        int posY = y/tileSize.y;

        ArrayList<Animal> animals = map.getAnimalsMap().get(new Vector2d(posX,posY));
        if(animals != null) {
            parent.setAnimalsCombobox(animals);
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) { }
    @Override
    public void mouseReleased(MouseEvent mouseEvent) { }
    @Override
    public void mouseEntered(MouseEvent mouseEvent) { }
    @Override
    public void mouseExited(MouseEvent mouseEvent) { }

    public void setFollowedAnimals(ArrayList<Animal> animals){
        followedAnimals = animals;
        repaint();
    }
    public void setFollowedAnimal(Animal animal){
        followedAnimals = new ArrayList<>(Arrays.asList(animal));
        repaint();
    }
}
