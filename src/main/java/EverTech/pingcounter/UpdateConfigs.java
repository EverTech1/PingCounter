package EverTech.pingcounter;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class UpdateConfigs {
    public void update(int x, int y, int selection, boolean isEnabled, int r, int g, int b){
        Configuration config = new Configuration(new File("config/PingCounter.cfg"));

        config.load();
        ConfigCategory cat = config.getCategory("general");

        cat.get("positionX").set(x);
        cat.get("positionY").set(y);
        cat.get("selection").set(selection);
        cat.get("enableDisplay").set(isEnabled);
        cat.get("rVal").set(r);
        cat.get("gVal").set(g);
        cat.get("bVal").set(b);
        config.save();
    }
}
