package main.visualization;

import main.ReadJSON;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class MenuPanel extends JPanel {

    private ArrayList<String> labelsText;
    private ArrayList<JFormattedTextField> textFields;
    private ArrayList<String> standardInput;

    public MenuPanel(){
        labelsText = new ArrayList<>(
                Arrays.asList("Width (1-200):", "Height (1-200):", "Jungle Ratio (0.00 - 1.00):",
                        "Start Energy:", "Move Energy:", "Plant Energy:",
                        "Animal Amount", "Plants Amount:",
                        "Timer (ms):", "Simulation Amount:")
        );
        textFields = new ArrayList<>();

        /*standardInput = new ArrayList<>(Arrays.asList("10","10","0.40","200","10","40","5","5","500","1"));*/
        getStandardInputFromFile();

        initComponents();
    }

    private void getStandardInputFromFile(){
        ReadJSON parameterJSON = new ReadJSON("parameters.json");
        standardInput = new ArrayList<>();
        standardInput.add(parameterJSON.getValue("width"));
        standardInput.add(parameterJSON.getValue("height"));
        standardInput.add(parameterJSON.getValue("jungleRatio"));
        standardInput.add(parameterJSON.getValue("startEnergy"));
        standardInput.add(parameterJSON.getValue("moveEnergy"));
        standardInput.add(parameterJSON.getValue("plantEnergy"));
        standardInput.add(parameterJSON.getValue("animalAmount"));
        standardInput.add(parameterJSON.getValue("plantsAmount"));
        standardInput.add(parameterJSON.getValue("timer"));
        standardInput.add(parameterJSON.getValue("simulationAmount"));
    }

    private void initComponents(){
        // formatters for correct input
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.'); //separator for the decimals
        symbols.setGroupingSeparator(Character.MIN_VALUE); //separator for the thousands

        NumberFormat format = new DecimalFormat("#", symbols);
        NumberFormatter formatterMin0 = new NumberFormatter(format);
        formatterMin0.setValueClass(Integer.class);
        formatterMin0.setMinimum(0);
        formatterMin0.setAllowsInvalid(true);

        NumberFormatter formatter1to200 = new NumberFormatter(format);
        formatter1to200.setValueClass(Integer.class);
        formatter1to200.setMinimum(1);
        formatter1to200.setMaximum(200);
        formatter1to200.setAllowsInvalid(true);

        NumberFormat formatDouble = new DecimalFormat("0.00", symbols);
        NumberFormatter formatterDouble = new NumberFormatter(formatDouble);
        formatterDouble.setValueClass(Double.class);
        formatterDouble.setMinimum(0.0);
        formatterDouble.setMaximum(1.0);
        formatterDouble.setAllowsInvalid(true);

        // adding components to panel
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        //menuPanel.setPreferredSize(new Dimension(380, 430));
        menuPanel.setBounds(0,0,380,350);
        int textFieldRows = labelsText.size();
        for (int i = 0; i < textFieldRows; i++) {
            MyGridBagConstraints gbc = new MyGridBagConstraints(0, i);
            JLabel label = new JLabel(labelsText.get(i));
            menuPanel.add(label, gbc);

            JPanel panel2 = new JPanel();
            JFormattedTextField textField;
            if(i == 0 || i == 1) // width and height - can be from 1 to 200
                textField = new JFormattedTextField(formatter1to200);
            else if(i == 2) // jungle ratio - needs double formatter
                textField = new JFormattedTextField(formatterDouble);
            else
                textField = new JFormattedTextField(formatterMin0);
            textField.setColumns(10);
            textField.setText(standardInput.get(i));
            textFields.add(textField);
            panel2.add(textField);
            gbc.change(1, i);
            menuPanel.add(panel2, gbc);
        }

        setLayout(null);
        add(menuPanel);
    }


    public ArrayList<String> getLabelsText(){
        return labelsText;
    }
    public ArrayList<JFormattedTextField> getTextFields(){
        return textFields;
    }
    public ArrayList<String> getStandardInput(){
        return standardInput;
    }

    public String getTextFieldText(String arg){
        switch(arg) {
            case "width":
                return textFields.get(0).getText();
            case "height":
                return textFields.get(1).getText();
            case "jungleRatio":
                return textFields.get(2).getText();
            case "startEnergy":
                return textFields.get(3).getText();
            case "moveEnergy":
                return textFields.get(4).getText();
            case "plantEnergy":
                return textFields.get(5).getText();
            case "animalAmount":
                return textFields.get(6).getText();
            case "plantsAmount":
                return textFields.get(7).getText();
            case "timer":
                return textFields.get(8).getText();
            case "simulationAmount":
                return textFields.get(9).getText();
        }
        return "";
    }

}
