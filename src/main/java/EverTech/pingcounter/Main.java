package EverTech.pingcounter;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.Timer;

@Mod(modid = Main.MODID, version = Main.VERSION, updateJSON = "https://gist.githubusercontent.com/EverTech1/2a2b99b663a910156457a59ecec0375e/raw/updateModrinth.json")
public class Main
{
    public static final String MODID = "pingCounter";
    public static final String VERSION = "1.8.1";
    public static boolean hideSettingsButton = false;
    public static boolean enableTextShadow = true;
    public static float customX = 0;
    public static float customY = 0;
    public static int selection = 0;
    public static int redValText = 255;
    public static int greenValText = 255;
    public static int blueValText = 255;
    public static int redValBg = 0;
    public static int greenValBg = 0;
    public static int blueValBg = 0;
    public static float alphaValBg = 0.5F;
    public static double scalar = 1;
    public static boolean enableDisplay = true;
    public static String displayText = "Ping: $[ping]ms";
    public static KeyBinding openSettings = new KeyBinding("Open settings", Keyboard.KEY_SUBTRACT, "PingCounter");
    @EventHandler
    public void init(FMLInitializationEvent event){
        ForgeVersion.startVersionCheck();
        Configuration config = new Configuration(new File("config/PingCounter.cfg"));
        config.load();
        hideSettingsButton = config.get("gui", "hideSettingsButton", false).getBoolean();
        enableTextShadow = config.get("color", "enableTextShadow", true).getBoolean();
        float tempCustomX = (float)config.get("position", "customX", 0.0).getDouble();
        float tempCustomY = (float)config.get("position", "customY", 0.0).getDouble();
        customX = tempCustomX > 1 ? 0 : tempCustomX;
        customY = tempCustomY > 1 ? 0 : tempCustomY;
        selection = config.get("gui", "selection", 0).getInt();
        enableDisplay = config.get("gui", "enableDisplay", true).getBoolean();
        redValText = config.get("color", "rVal", 255).getInt();
        greenValText = config.get("color", "gVal", 255).getInt();
        blueValText = config.get("color", "bVal",255).getInt();
        redValBg = config.get("color", "rValBG", 0).getInt();
        greenValBg = config.get("color", "gValBG", 0).getInt();
        blueValBg = config.get("color", "bValBG", 0).getInt();
        alphaValBg = (float)config.get("color", "alphaValBG", 0.5).getDouble();
        scalar = config.get("position", "scalar", 1.0).getDouble();
        displayText = config.get("gui", "displayText", "Ping: $[ping]ms").getString();
        config.save();
        ClientRegistry.registerKeyBinding(openSettings);
        new Timer().schedule(new PingTimer(), 0, 2000);
        MinecraftForge.EVENT_BUS.register(new Events());
    }
}

