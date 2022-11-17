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

@Mod(modid = Main.MODID, version = Main.VERSION, updateJSON = "https://gist.githubusercontent.com/EverTech1/62b9c537189b016db2e7525ccb0ddfde/raw/update.json")
public class Main
{
    public static final String MODID = "pingCounter";
    public static final String VERSION = "1.3.0";
    public static boolean hideSettingsButton = false;
    public static int positionX = 10;
    public static int positionY = 10;
    public static int customX = 0;
    public static int customY = 0;
    public static int selection = 0;
    public static int redValText = 255;
    public static int greenValText = 255;
    public static int blueValText = 255;
    public static int redValBg = 0;
    public static int greenValBg = 0;
    public static int blueValBg = 0;
    public static float alphaValBg = 0.5F;

    public static boolean enableDisplay = true;
    public static KeyBinding openSettings = new KeyBinding("Open settings", Keyboard.KEY_SUBTRACT, "PingCounter");
    @EventHandler
    public void init(FMLInitializationEvent event){
        ForgeVersion.startVersionCheck();
        Configuration config = new Configuration(new File("config/PingCounter.cfg"));
        config.load();
        hideSettingsButton = config.get("gui", "hideSettingsButton", false).getBoolean();
        positionX = config.get("position", "positionX", 10).getInt();
        positionY = config.get("position", "positionY", 10).getInt();
        customX = config.get("position", "customX", 0).getInt();
        customY = config.get("position", "customY", 0).getInt();
        selection = config.get("gui", "selection", 0).getInt();
        enableDisplay = config.get("gui", "enableDisplay", true).getBoolean();
        redValText = config.get("color", "rVal", 255).getInt();
        greenValText = config.get("color", "gVal", 255).getInt();
        blueValText = config.get("color", "bVal",255).getInt();
        redValBg = config.get("color", "rValBG", 0).getInt();
        greenValBg = config.get("color", "gValBG", 0).getInt();
        blueValBg = config.get("color", "bValBG", 0).getInt();
        alphaValBg = (float)config.get("color", "alphaValBG", 0.5).getDouble();
        config.save();
        ClientRegistry.registerKeyBinding(openSettings);
        new Timer().schedule(new PingTimer(), 0, 5000);
        MinecraftForge.EVENT_BUS.register(new Events());
    }
}

