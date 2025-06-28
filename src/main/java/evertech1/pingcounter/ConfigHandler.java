package evertech1.pingcounter;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;

public class ConfigHandler {
    private static final Gson gson = new Gson();
    public static Config config = new Config();
    private static void createFile(){
        File f = new File("./config/pingcounter.json");
        try {
            if(f.createNewFile()) updateValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static{
        createFile();
    }
    public static void getValues(){
        createFile();
        try(FileReader file = new FileReader("./config/pingcounter.json")){
            try{
                config = gson.fromJson(file, Config.class);
            }
            catch(JsonSyntaxException ignored){
                updateValues();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void updateValues(){
        createFile();
        try(FileWriter file = new FileWriter("./config/pingcounter.json")){
            gson.toJson(config, file);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
