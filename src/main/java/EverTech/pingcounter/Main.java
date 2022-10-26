package EverTech.pingcounter;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

import java.io.File;


@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main
{
    public static final String MODID = "pingCounter";
    public static final String VERSION = "1.0.0";
    public static int positionX = 10;
    public static int positionY = 10;
    public static int selection = 0;
    public static int redVal = 255;
    public static int greenVal = 255;
    public static int blueVal = 255;
    public static boolean enableDisplay = true;
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Configuration config = new Configuration(new File("config/PingCounter.cfg"));
        config.load();
        positionX = config.get("general", "positionX", 10).getInt();
        positionY = config.get("general", "positionY", 10).getInt();
        selection = config.get("general", "selection", 0).getInt();
        enableDisplay = config.get("general", "enableDisplay", true).getBoolean();
        redVal = config.get("general", "rVal", 255).getInt();
        greenVal = config.get("general", "gVal", 255).getInt();
        blueVal = config.get("general", "bVal",255).getInt();
        config.save();
        MinecraftForge.EVENT_BUS.register(new Events());
    }

}
