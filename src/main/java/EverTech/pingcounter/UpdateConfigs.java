package EverTech.pingcounter;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class UpdateConfigs {
    public void updateGui(boolean isEnabled, int rT, int gT, int bT, int rB, int gB, int bB, float alphaB){
        Configuration config = new Configuration(new File("config/PingCounter.cfg"));
        config.load();
        ConfigCategory catGui = config.getCategory("gui");
        ConfigCategory catColor = config.getCategory("color");

        catGui.get("enableDisplay").set(isEnabled);
        catColor.get("rVal").set(rT);
        catColor.get("gVal").set(gT);
        catColor.get("bVal").set(bT);
        catColor.get("rValBG").set(rB);
        catColor.get("gValBG").set(gB);
        catColor.get("bValBG").set(bB);
        catColor.get("alphaValBG").set(alphaB);
        config.save();
    }
    public void updatePos(int x, int y, int custX, int custY, int selection){
        Configuration config = new Configuration(new File("config/PingCounter.cfg"));
        config.load();
        ConfigCategory catPos = config.getCategory("position");
        config.getCategory("gui").get("selection").set(selection);
        catPos.get("positionX").set(x);
        catPos.get("positionY").set(y);
        catPos.get("customX").set(custX);
        catPos.get("customY").set(custY);

        config.save();
    }
}
