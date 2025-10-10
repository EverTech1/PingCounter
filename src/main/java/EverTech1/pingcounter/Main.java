package EverTech1.pingcounter;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@Mod(Main.MODID)
public class Main
{
    public static boolean notified = false; //User notified about latest version
    public static final String MODID = "pingcounter";
    public static KeyMapping keyMap = new KeyMapping("key.category.pingcounter.open_settings", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_MINUS, new KeyMapping.Category(ResourceLocation.fromNamespaceAndPath("pingcounter", "keybinds"))); //Settings keybind keymap
    public static Pinger pinger; //Server pinger instance
    public static boolean isEditing = false;
    public Main(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::registerKeys); //Register keybind
        modEventBus.addListener(this::onRegisterLayers); //Register ping layer
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        pinger = new Pinger();
    }

    public void onRegisterLayers(RegisterGuiLayersEvent e){
        e.registerBelow(ResourceLocation.withDefaultNamespace("chat"), ResourceLocation.fromNamespaceAndPath(MODID, "ping_layer"), new PingLayer());
    }
    private void registerKeys(final RegisterKeyMappingsEvent event){
        event.register(keyMap);
    }
}
