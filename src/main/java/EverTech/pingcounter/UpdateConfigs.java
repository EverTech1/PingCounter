package EverTech.pingcounter;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class UpdateConfigs {
    public void updateGui(){
        Configuration config = new Configuration(new File("config/PingCounter.cfg"));
        config.load();
        ConfigCategory catGui = config.getCategory("gui");
        ConfigCategory catColor = config.getCategory("color");

        catGui.get("enableDisplay").set(Main.enableDisplay);
        catColor.get("rVal").set(Main.redValText);
        catColor.get("gVal").set(Main.greenValText);
        catColor.get("bVal").set(Main.blueValText);
        catColor.get("rValBG").set(Main.redValBg);
        catColor.get("gValBG").set(Main.greenValBg);
        catColor.get("bValBG").set(Main.blueValBg);
        catColor.get("alphaValBG").set(Main.alphaValBg);
        catColor.get("enableTextShadow").set(Main.enableTextShadow);
        config.save();
    }
    public void updatePos(){
        Configuration config = new Configuration(new File("config/PingCounter.cfg"));
        config.load();
        ConfigCategory catPos = config.getCategory("position");
        config.getCategory("gui").get("displayText").set(Main.displayText);
        catPos.get("customX").set(Main.customX);
        catPos.get("customY").set(Main.customY);
        catPos.get("scalar").set(Main.scalar);
        config.save();
    }
}
