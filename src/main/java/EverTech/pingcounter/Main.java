package EverTech.pingcounter;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;


@Mod(modid = Main.MODID, version = Main.VERSION)
public class Main
{
    public static final String MODID = "pingCounter";
    public static final String VERSION = "1.1.0";
    public static boolean hideSettingsButton = false;
    public static int positionX = 10;
    public static int positionY = 10;
    public static int customX = 0;
    public static int customY = 0;
    public static int selection = 0;
    public static int redVal = 255;
    public static int greenVal = 255;
    public static int blueVal = 255;
    public static boolean enableDisplay = true;
    public static KeyBinding openSettings = new KeyBinding("Open settings", Keyboard.KEY_SUBTRACT, "PingCounter");
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        Configuration config = new Configuration(new File("config/PingCounter.cfg"));
        config.load();
        hideSettingsButton = config.get("gui", "hideSettingsButton", false).getBoolean();
        positionX = config.get("position", "positionX", 10).getInt();
        positionY = config.get("position", "positionY", 10).getInt();
        customX = config.get("position", "customX", 0).getInt();
        customY = config.get("position", "customY", 0).getInt();
        selection = config.get("gui", "selection", 0).getInt();
        enableDisplay = config.get("gui", "enableDisplay", true).getBoolean();
        redVal = config.get("color", "rVal", 255).getInt();
        greenVal = config.get("color", "gVal", 255).getInt();
        blueVal = config.get("color", "bVal",255).getInt();
        config.save();
        ClientRegistry.registerKeyBinding(openSettings);
        MinecraftForge.EVENT_BUS.register(new Events());
    }
}

