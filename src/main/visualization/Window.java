package main.visualization;

import main.Simulation;
import main.Vector2d;
import main.WriteFile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Window extends JFrame implements ActionListener{
    private JPanel panel;
    private MenuPanel menuPanel;
    private JButton pauseButtonAll;
    private JButton saveFileButton;

    private ArrayList<SimulationWidget> simWidgets;

    private Vector2d size;
    private Timer timer;

    public Window(){
        size = new Vector2d(400,500);
        simWidgets = new ArrayList<>();

        initComponents();

        setTitle("Evolution Generator");
        setVisible(true);
        setFocusable(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        timer = new Timer(0, this);
    }


    private void initComponents(){
        panel = new JPanel();
        panel.setPreferredSize(new Dimension(size.x,size.y));
        panel.setVisible(true);
        panel.setLayout(null);

        menuPanel = new MenuPanel();
        menuPanel.setBounds(20,20,380,350);
        panel.add(menuPanel);

        // buttons
        ButtonClickListener btnListener = new ButtonClickListener();

        JButton renderButton = new JButton("Render");
        renderButton.setBounds((size.x-200)/2, 380, 200, 25);
        renderButton.setActionCommand("Render");
        renderButton.addActionListener(btnListener);

        pauseButtonAll = new JButton("Pause");
        pauseButtonAll.setBounds((size.x-200)/2, 415, 200, 25);
        pauseButtonAll.setEnabled(false);
        pauseButtonAll.setActionCommand("PauseAll");
        pauseButtonAll.addActionListener(btnListener);

        saveFileButton = new JButton("Save world stats to file");
        saveFileButton.setBounds((size.x-200)/2, 450, 200, 25);
        saveFileButton.setEnabled(false);
        saveFileButton.setActionCommand("SaveToFile");
        saveFileButton.addActionListener(btnListener);

        panel.add(renderButton);
        panel.add(pauseButtonAll);
        panel.add(saveFileButton);

        add(panel);
        pack();
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        for (SimulationWidget sw : simWidgets)
            if(!sw.getPaused())
                sw.run();
    }

    public void start(){
        timer.stop();
        // parameters from inputs
        try {
            int width = Integer.parseInt(menuPanel.getTextFieldText("width"));
            int height = Integer.parseInt(menuPanel.getTextFieldText("height"));
            double jungleRatio = Double.parseDouble(menuPanel.getTextFieldText("jungleRatio"));
            int startEnergy = Integer.parseInt(menuPanel.getTextFieldText("startEnergy"));
            int moveEnergy = Integer.parseInt(menuPanel.getTextFieldText("moveEnergy"));
            int plantEnergy = Integer.parseInt(menuPanel.getTextFieldText("plantEnergy"));
            int animalAmount = Integer.parseInt(menuPanel.getTextFieldText("animalAmount"));
            int plantsAmount = Integer.parseInt(menuPanel.getTextFieldText("plantsAmount"));
            int time = Integer.parseInt(menuPanel.getTextFieldText("timer"));
            int simulationAmount = Integer.parseInt(menuPanel.getTextFieldText("simulationAmount"));


            clearSimulationWidgets();
            panel.setPreferredSize(new Dimension(size.x * (simulationAmount + 1) + (simulationAmount - 1) * 10, 850));
            //panel.revalidate();
            pack();

            for (int i = 0; i < simulationAmount; i++) {
                Simulation simulation = new Simulation(width, height, jungleRatio,
                        startEnergy, moveEnergy, plantEnergy,
                        animalAmount, plantsAmount
                );
                SimulationWidget sw = new SimulationWidget(Window.this, simulation);
                sw.setBounds((i + 1) * size.x + i * 10, 0, 400, 850);
                simWidgets.add(sw);
                panel.add(sw);
                sw.repaintRenderer();
            }

            pauseButtonAll.setActionCommand("PauseAll");
            pauseButtonAll.setText("Pause");
            pauseButtonAll.setEnabled(true);
            saveFileButton.setEnabled(true);

            timer.setDelay(time);
            timer.setInitialDelay(time);
            timer.start();
        }
        catch(Exception e) {
            e.printStackTrace();
            ErrorBox.infoBox(e.toString(), "Exception Error");
        }
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if(command.equals("Render")){
                start();
            }
            else if(command.equals("PauseAll")){
                pauseButtonAll.setActionCommand("ResumeAll");
                pauseButtonAll.setText("Resume");
                for(SimulationWidget sw : simWidgets){
                    sw.pause();
                }
            }
            else if(command.equals("ResumeAll")){
                pauseButtonAll.setActionCommand("PauseAll");
                pauseButtonAll.setText("Pause");
                for(SimulationWidget sw : simWidgets){
                    sw.resume();
                }
            }
            else if(command.equals("SaveToFile")){
                saveStatsToFile();
            }
        }
    }

    public void saveStatsToFile(){
        StringBuilder data = new StringBuilder("INPUT:\n");
        //input
        ArrayList<String> menuLabelsText = menuPanel.getLabelsText();
        ArrayList<JFormattedTextField> menuTextFields = menuPanel.getTextFields();
        for(int i=0; i<menuLabelsText.size(); i++){
            data.append(menuLabelsText.get(i) + " ");
            data.append(menuTextFields.get(i).getText());
            data.append("\n");
        }

        //output
        data.append("\n\nOUTPUT:\n");

        for(int i=0; i<simWidgets.size(); i++){
            data.append("Simulation: " + (i+1) + "\n");
            data.append(simWidgets.get(i).getSimulation().getMap().getStats().fullStatistics.getFormattedTextStats());
            data.append("\n\n");
        }
        System.out.println(data);
        WriteFile.write("stats.txt", data.toString());
    }

    public void checkPauseButton() {
        for (SimulationWidget sw : simWidgets) {
            if (sw.getPaused().equals(Boolean.FALSE))
                return;
        }
        pauseButtonAll.setActionCommand("ResumeAll");
        pauseButtonAll.setText("Resume");
    }
    public void checkResumeButton() {
        for (SimulationWidget sw : simWidgets) {
            if (sw.getPaused().equals(Boolean.TRUE))
                return;
        }
        pauseButtonAll.setActionCommand("PauseAll");
        pauseButtonAll.setText("Pause");
    }

    public void clearSimulationWidgets(){
        for (SimulationWidget sw : simWidgets)
            panel.remove(sw);
        simWidgets.clear();
        panel.repaint();
    }
}