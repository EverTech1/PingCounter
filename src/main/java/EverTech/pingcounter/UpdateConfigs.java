package EverTech.pingcounter;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class UpdateConfigs {
    public void update(int x, int y, int custX, int custY, int selection, boolean isEnabled, int r, int g, int b){
        Configuration config = new Configuration(new File("config/PingCounter.cfg"));

        config.load();
        ConfigCategory catPos = config.getCategory("position");
        ConfigCategory catGui = config.getCategory("gui");
        ConfigCategory catColor = config.getCategory("color");

        catPos.get("positionX").set(x);
        catPos.get("positionY").set(y);
        catPos.get("customX").set(custX);
        catPos.get("customY").set(custY);
        catGui.get("selection").set(selection);
        catGui.get("enableDisplay").set(isEnabled);
        catColor.get("rVal").set(r);
        catColor.get("gVal").set(g);
        catColor.get("bVal").set(b);
        config.save();
    }
}
