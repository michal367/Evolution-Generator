package main.visualization;

import main.Vector2d;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class StatisticsPanel extends JPanel {

    private ArrayList<JLabel> statsLabels;

    public StatisticsPanel(Vector2d size, ArrayList<String> labelsText, boolean margin) {

        statsLabels = new ArrayList<>();

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridBagLayout());
        if(margin)
            statsPanel.setBounds(10, 15, size.x - 15, size.y - 20);
        else
            statsPanel.setBounds(0, 0, size.x, size.y);
        int rows = labelsText.size();
        for (int i = 0; i < rows; i++) {
            MyGridBagConstraints gbc = new MyGridBagConstraints(0, i);
            JLabel label = new JLabel(labelsText.get(i));
            statsPanel.add(label, gbc);

            JLabel label2 = new JLabel();
            statsLabels.add(label2);
            gbc.change(1, i);
            statsPanel.add(label2, gbc);
        }

        setLayout(null);
        add(statsPanel);
    }

    public void updateStats(ArrayList<String> stats){
        for(int i=0; i<stats.size(); i++)
            statsLabels.get(i).setText(stats.get(i));
    }
    public void updateStat(int index, String str){
        statsLabels.get(index).setText(str);
    }

    public void clearStats(){
        for(JLabel label : statsLabels){
            label.setText("");
        }
    }


}
