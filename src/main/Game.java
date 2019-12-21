package main;

import main.visualization.ErrorBox;
import main.visualization.Window;

public class Game {
    public static void main(String[] args) {
        try {
            Window wnd = new Window();
        }
        catch(Exception e) {
            e.printStackTrace();
            ErrorBox.infoBox(e.toString(), "Exception Error");
            System.exit(1);
        }
    }
}
