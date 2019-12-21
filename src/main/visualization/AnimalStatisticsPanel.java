package main.visualization;

import main.Animal;
import main.Genotype;
import main.Vector2d;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class AnimalStatisticsPanel extends JPanel {

    private SimulationWidget parent;

    private JComboBox<String> animalsList;
    private StatisticsPanel animalStatsPanel;

    private ArrayList<Animal> comboboxAnimals;
    private Animal followedAnimal;


    public AnimalStatisticsPanel(SimulationWidget parent, Vector2d size){
        ArrayList<String> labelsTextAnimal = new ArrayList<>(
                Arrays.asList("Position:", "Energy:", "Dead:", "Lifetime:",
                        "Children:", "Descendant:", "Genotype:")
        );
        this.parent = parent;
        comboboxAnimals = new ArrayList<>();

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0,0, size.x, size.y);
        panel.setBorder(BorderFactory.createTitledBorder("Animal Statistics"));

        animalsList = new JComboBox<>();
        animalsList.setBounds(10,20,size.x-20,20);
        animalsList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JComboBox<String> combo = (JComboBox<String>) event.getSource();

                if(combo.getItemCount() > 0) {
                    String selected = (String) combo.getSelectedItem();
                    if (selected.length() > 0) {

                        String indexStr = selected.substring(0, selected.indexOf("."));
                        int index = Integer.parseInt(indexStr) - 1;
                        followedAnimal = comboboxAnimals.get(index);
                        parent.setFollowedAnimal(followedAnimal);
                        // list with animal dominant gene - clear selection
                        parent.listClearSelection();
                    }
                }
            }
        });
        panel.add(animalsList);


        // ANIMAL STATS
        animalStatsPanel = new StatisticsPanel(new Vector2d(size.x, 195), labelsTextAnimal, false);
        animalStatsPanel.setBounds(10,40,size.x - 15, 220 - 25);
        panel.add(animalStatsPanel);

        setLayout(null);
        add(panel);
    }

    public void updateFollowedAnimalStats(){
        if(followedAnimal != null) {
            Vector2d animalPosition = followedAnimal.getPosition();
            int animalEnergy = followedAnimal.getEnergy();
            String isDead = followedAnimal.isDead() ? "YES" : "NO";
            int animalLifetime = followedAnimal.getLifetime();
            // no genotype, because genotype doesn't change

            ArrayList<String> statsStr = new ArrayList<>(Arrays.asList(
                    animalPosition.toString(), Integer.toString(animalEnergy), isDead, Integer.toString(animalLifetime)
            ));

            animalStatsPanel.updateStats(statsStr);
        }
    }
    public void updateChildrenAndDescendants(int lastPausedEra, int actualEra){
        if(followedAnimal != null) {
            int animalChildrenAmount = followedAnimal.getChildrenAmount(lastPausedEra);
            int animalDescendantAmount = followedAnimal.getDescendantAmount(lastPausedEra);

            animalStatsPanel.updateStat(4, Integer.toString(animalChildrenAmount) + "  (" + lastPausedEra + "-" + actualEra + " era)");
            animalStatsPanel.updateStat(5, Integer.toString(animalDescendantAmount) + "  (" + lastPausedEra + "-" + actualEra + " era)");
        }
    }
    public void setFollowedAnimalStats(int lastPausedEra, int actualEra){
        updateFollowedAnimalStats();
        updateChildrenAndDescendants(lastPausedEra, actualEra);

        // genotype is set here (only once), because it doesn't change
        Genotype animalGenotype = followedAnimal.getGenotype();
        String animalGenotypeStr = animalGenotype.toString();
        animalGenotypeStr = "<html>" + animalGenotypeStr.substring(0, 8) + "<br/>" +
                animalGenotypeStr.substring(8, 16) + "<br/>" +
                animalGenotypeStr.substring(16, 24) + "<br/>" +
                animalGenotypeStr.substring(24, animalGenotypeStr.length()) + "</html>";
        animalStatsPanel.updateStat(6, animalGenotypeStr);
    }


    public void setCombobox(ArrayList<Animal> animals){
        animalsList.removeAllItems();

        comboboxAnimals = animals;
        for(int i=0; i<animals.size(); i++) {
            String item = (i+1) + ". Animal";
            animalsList.addItem(item);
        }
    }

    public void setFollowedAnimal(Animal animal) {
        this.followedAnimal = animal;
    }

    public void clearCombobox(){
        animalsList.removeAllItems();
        comboboxAnimals = new ArrayList<>();
    }
    public void clearStats(){
        animalStatsPanel.clearStats();
    }
}
