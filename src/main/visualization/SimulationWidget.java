package main.visualization;

import main.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class SimulationWidget extends JPanel{
    private Window parent;
    private Vector2d size;
    private Vector2d rendererSize;
    private Boolean paused;
    private int lastPausedEra;

    private Simulation simulation;

    private Renderer renderer;
    private JButton pauseButton;
    private StatisticsPanel statsPanel;
    private AnimalStatisticsPanel animalStatsPanel;

    // animals with dominant genotype list - JList
    private DefaultListModel<String> animalsDominantGeneModel;
    private JList<String> animalsDominantGeneList;
    private ArrayList<Animal> animalsWithDominantGene;

    public SimulationWidget(Window parent, Simulation simulation){
        this.parent = parent;
        this.simulation = simulation;

        size = new Vector2d(400, 850);
        rendererSize = new Vector2d(400, 400);
        paused = Boolean.FALSE;

        initComponents();
    }

    public void initComponents(){
        setLayout(null);
        setPreferredSize(new Dimension(size.x, size.y));

        // RENDERER
        renderer = new Renderer(this, simulation.getMap(), rendererSize, simulation.startEnergy);
        renderer.setBackground(Color.BLACK);
        renderer.setBounds(0, 0, rendererSize.x,rendererSize.y);
        renderer.setVisible(true);
        add(renderer);

        // PAUSE BUTTON
        pauseButton = new JButton("Pause");
        pauseButton.setBounds((rendererSize.x-100)/2, rendererSize.y+10, 100,30);
        pauseButton.setActionCommand("Pause");
        pauseButton.addActionListener(new ButtonClickListener());
        add(pauseButton);


        // WORLD STATS
        ArrayList<String> labelsText = new ArrayList<>(
                Arrays.asList("Era:", "Animal Amount:", "Plant Amount:",
                        "Animals Avg Energy:", "Animals Avg Lifetime:", "Avg Children Amount:",
                        "Dominant Genotype:")
        );
        statsPanel = new StatisticsPanel(new Vector2d(5*rendererSize.x/9, 240), labelsText, true);
        statsPanel.setBounds(0,rendererSize.y + 50,5*rendererSize.x/9,240);
        statsPanel.setBorder(BorderFactory.createTitledBorder("World Statistics"));
        add(statsPanel);
        updateStats();

        // ANIMAL STATS
        animalStatsPanel = new AnimalStatisticsPanel(this, new Vector2d(4*rendererSize.x/9, 240));
        animalStatsPanel.setBounds(5*rendererSize.x/9, rendererSize.y + 50, 4*rendererSize.x/9, 240);
        add(animalStatsPanel);

        // ANIMALS WITH DOMINANT GENOTYPE
        JPanel animalsDominantGenePanel = new JPanel(null);
        animalsDominantGenePanel.setBounds(0,rendererSize.y + 300, rendererSize.x,120);
        animalsDominantGenePanel.setBorder(BorderFactory.createTitledBorder("Animals With Dominant Genotype"));

        animalsDominantGeneModel = new DefaultListModel<>();
        animalsDominantGeneList = new JList(animalsDominantGeneModel);
        animalsDominantGeneList.setBounds(0,0,rendererSize.x-20,60);
        animalsDominantGeneList.addListSelectionListener(
                new ListSelectionListener() {
                    public void valueChanged(ListSelectionEvent listSelectionEvent) {
                        boolean adjust = listSelectionEvent.getValueIsAdjusting();
                        if (!adjust) {
                            JList list = (JList) listSelectionEvent.getSource();
                            if(list.getSelectedValue() != null) {
                                String selectionValue = list.getSelectedValue().toString();
                                //System.out.println(selectionValue);
                                String indexStr = selectionValue.substring(0, selectionValue.indexOf("."));
                                int index = Integer.parseInt(indexStr) - 1;
                                Animal animal = animalsWithDominantGene.get(index);
                                SimulationWidget.this.animalStatsPanel.setFollowedAnimal(animal);
                                SimulationWidget.this.setFollowedAnimal(animal);
                                // remove all items from combobox with animals
                                animalStatsPanel.clearCombobox();
                            }
                        }
                    }
                }
        );
        updateAnimalsDominantGene();

        JScrollPane listScrollPane = new JScrollPane(animalsDominantGeneList);
        listScrollPane.setBounds(10,20,rendererSize.x-20,60);
        animalsDominantGenePanel.add(listScrollPane);

        // BUTTONS
        JButton showAllButton = new JButton("Show All");
        showAllButton.setBounds(10,80, 100,30);
        showAllButton.setActionCommand("ShowAll");
        showAllButton.addActionListener(new ButtonClickListener());
        animalsDominantGenePanel.add(showAllButton);

        JButton ClearSelectedButton = new JButton("Clear Selected");
        ClearSelectedButton.setBounds(120,80, 120,30);
        ClearSelectedButton.setActionCommand("ClearSelection");
        ClearSelectedButton.addActionListener(new ButtonClickListener());
        animalsDominantGenePanel.add(ClearSelectedButton);

        add(animalsDominantGenePanel);
    }



    public void run(){
        simulation.run();
        renderer.repaint();
        updateStats();
        animalStatsPanel.updateFollowedAnimalStats();
        updateAnimalsDominantGene();
    }
    public void repaintRenderer(){
        renderer.repaint();
    }

    public void updateStats(){
        SingleStatistics stats = simulation.getMap().getStats().singleStatistics;
        ArrayList<String> statsStr = new ArrayList<>(Arrays.asList(stats.toString().split(";")));
        if(!statsStr.get(6).equals("-"))
            statsStr.set(6, "<html>" + statsStr.get(6).substring(0, 8) + "<br/>" +
                    statsStr.get(6).substring(8, 16) + "<br/>" +
                    statsStr.get(6).substring(16, 24) + "<br/>" +
                    statsStr.get(6).substring(24) + "</html>");
        statsPanel.updateStats(statsStr);
    }



    public void setAnimalsCombobox(ArrayList<Animal> animals){
        animalStatsPanel.setCombobox(animals);
    }
    public void setFollowedAnimal(Animal animal) {
        renderer.setFollowedAnimal(animal);
        int actualEra = getSimulation().getMap().getStats().singleStatistics.era;
        animalStatsPanel.setFollowedAnimalStats(lastPausedEra, actualEra);
    }



    public void updateAnimalsDominantGene(){
        animalsWithDominantGene = simulation.getMap().getAnimalsWithDominantGenotype();
        animalsDominantGeneModel.removeAllElements();
        for (int i = 0; i < animalsWithDominantGene.size(); i++) {
            animalsDominantGeneModel.addElement((i+1) + ". Animal:  " + animalsWithDominantGene.get(i).getGenotype());
        }
    }
    public void listClearSelection(){
        animalsDominantGeneList.clearSelection();
    }



    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if(command.equals("Pause")){
                pause();
                parent.checkPauseButton();
            }
            else if(command.equals("Resume")){
                resume();
                parent.checkResumeButton();
            }
            else if(command.equals("ShowAll")){
                clearAnimalStats();

                renderer.setFollowedAnimals(animalsWithDominantGene);
            }
            else if(command.equals("ClearSelection")){
                clearAnimalStats();
                animalsDominantGeneList.clearSelection();
                renderer.setFollowedAnimals(new ArrayList<>());
            }
        }
    }

    public void clearAnimalStats(){
        animalStatsPanel.clearCombobox();
        animalStatsPanel.clearStats();
        animalStatsPanel.clearFollowedAnimal();
    }

    public void pause(){
        int actualEra = getSimulation().getMap().getStats().singleStatistics.era;
        animalStatsPanel.updateChildrenAndDescendants(lastPausedEra, actualEra);

        paused = Boolean.TRUE;
        pauseButton.setActionCommand("Resume");
        pauseButton.setText("Resume");
    }
    public void resume(){
        lastPausedEra = simulation.getMap().getStats().singleStatistics.era;
        paused = Boolean.FALSE;
        pauseButton.setActionCommand("Pause");
        pauseButton.setText("Pause");
    }

    public Boolean getPaused(){
        return paused;
    }
    public Simulation getSimulation(){
        return simulation;
    }
}
