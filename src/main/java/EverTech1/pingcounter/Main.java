package EverTech1.pingcounter;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod(Main.MODID)
public class Main
{
    public static boolean notified = false; //User notified about latest version
    public static final String MODID = "pingcounter";
    public static KeyMapping keyMap = new KeyMapping("key.pingcounter.open_settings", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_MINUS, "key.categories.pingcounter"); //Settings keybind keymap
    public static Pinger pinger; //Server pinger instance
    public static boolean isEditing = false;
    public Main(FMLJavaModLoadingContext context)
    {
        var modEventBus = context.getModBusGroup();
        RegisterKeyMappingsEvent.getBus(modEventBus).addListener(this::registerKeys); //Register keybind
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        pinger = new Pinger();
    }

    private void registerKeys(RegisterKeyMappingsEvent event){
        event.register(keyMap);
    }
}
