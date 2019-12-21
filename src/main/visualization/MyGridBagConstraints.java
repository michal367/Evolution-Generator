package main.visualization;

import java.awt.*;

public class MyGridBagConstraints extends GridBagConstraints {
    public MyGridBagConstraints(){
        gridwidth = 1;
        gridheight = 1;
        weighty = 1.0;
        insets = new Insets(5, 5, 5, 5);
        fill = GridBagConstraints.HORIZONTAL;
    }
    public MyGridBagConstraints(int x, int y){
        this();
        change(x, y);
    }
    public void change(int x, int y){
        gridx = x;
        gridy = y;
        weightx = x;
        anchor = (x == 0) ? GridBagConstraints.LINE_START : GridBagConstraints.LINE_END;
    }
}
