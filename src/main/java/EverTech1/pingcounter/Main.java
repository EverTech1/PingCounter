package EverTech1.pingcounter;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod(Main.MODID)
public class Main
{
    public static boolean notified = false;
    public static final String MODID = "pingcounter";
    public static KeyMapping keyMap = new KeyMapping("key.pingcounter.open_settings", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_MINUS, "key.categories.pingcounter");
    public static Pinger pinger;
    public Main()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerKeys);
        modEventBus.register(keyMap);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        pinger = new Pinger();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
    }
    private void registerKeys(final RegisterKeyMappingsEvent event){
        event.register(keyMap);
    }
}
