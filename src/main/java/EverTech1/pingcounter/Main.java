package EverTech1.pingcounter;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@Mod(Main.MODID)
public class Main
{
    public static boolean notified = false; //User notified about latest version
    public static final String MODID = "pingcounter";
    public static KeyMapping keyMap = new KeyMapping("key.pingcounter.open_settings", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_MINUS, "key.categories.pingcounter"); //Settings keybind keymap
    public static Pinger pinger; //Server pinger instance
    public static boolean isEditing = false;
    public Main(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::registerKeys); //Register keybind
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        pinger = new Pinger();
    }

    private void registerKeys(final RegisterKeyMappingsEvent event){
        event.register(keyMap);
    }
}
