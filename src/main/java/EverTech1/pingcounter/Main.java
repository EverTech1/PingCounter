package EverTech1.pingcounter;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
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
    public static KeyMapping keyMap = new KeyMapping("key.category.pingcounter.open_settings", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_MINUS, new KeyMapping.Category(ResourceLocation.fromNamespaceAndPath("pingcounter", "keybinds"))); //Settings keybind keymap
    public static Pinger pinger; //Server pinger instance
    public static boolean isEditing = false;
    public Main(FMLJavaModLoadingContext context)
    {
        RegisterKeyMappingsEvent.BUS.addListener(this::registerKeys); //Register keybind
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        pinger = new Pinger();
    }

    private void registerKeys(RegisterKeyMappingsEvent event){
        event.register(keyMap);
    }
}
