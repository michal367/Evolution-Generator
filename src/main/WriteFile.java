package main;

import main.visualization.ErrorBox;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteFile {
    public static void write(String pathToFile, String data)
    {
        BufferedWriter writer = null;
        try{
            writer = new BufferedWriter(new FileWriter(pathToFile));
            String[] words = data.split("\n");
            for (String word: words) {
                writer.write(word);
                writer.newLine();
            }
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            ErrorBox.infoBox(e.toString(), "Exception Error");
        }
        finally{
            try {
                if(writer != null)
                    writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                ErrorBox.infoBox(e.toString(), "Exception Error");
            }
        }
    }
}
