package main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import main.visualization.ErrorBox;

import java.io.FileReader;
import java.io.IOException;

public class ReadJSON
{
    private JSONObject obj;

    @SuppressWarnings("unchecked")
    public ReadJSON(String pathToFile)
    {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(pathToFile)) {
            obj = (JSONObject) jsonParser.parse(reader);
        }
        catch (IOException | ParseException e) {
            e.printStackTrace();
            System.out.println(e);
            ErrorBox.infoBox("JSON parameters.json: " + e.toString(), "Exception Error");
        }
    }

    public String getValue(String key)
    {
        Object value = obj.get(key);
        return value.toString();
    }
}