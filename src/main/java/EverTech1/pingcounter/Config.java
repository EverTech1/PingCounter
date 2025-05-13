package EverTech1.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue enabledConfig = BUILDER.define("enabled", true);
    private static final ForgeConfigSpec.IntValue textColorRedConfig = BUILDER.defineInRange("textColorRed", 255, 0, 255);
    private static final ForgeConfigSpec.IntValue textColorGreenConfig = BUILDER.defineInRange("textColorGreen", 255, 0, 255);
    private static final ForgeConfigSpec.IntValue textColorBlueConfig = BUILDER.defineInRange("textColorBlue", 255, 0, 255);
    private static final ForgeConfigSpec.IntValue backgroundColorAlphaConfig = BUILDER.defineInRange("backgroundColorAlpha", 100, 0, 255);
    private static final ForgeConfigSpec.IntValue backgroundColorRedConfig = BUILDER.defineInRange("backgroundColorRed", 0, 0, 255);
    private static final ForgeConfigSpec.IntValue backgroundColorGreenConfig = BUILDER.defineInRange("backgroundColorRed", 0, 0, 255);
    private static final ForgeConfigSpec.IntValue backgroundColorBlueConfig = BUILDER.defineInRange("backgroundColorRed", 0, 0, 255);
    private static final ForgeConfigSpec.DoubleValue offsetXConfig = BUILDER.defineInRange("offsetX", 0.45, 0.0, 1.0);
    private static final ForgeConfigSpec.DoubleValue offsetYConfig = BUILDER.defineInRange("offsetY", 0.45, 0.0, 1.0);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enabled;

    public static int textColorRed;
    public static int textColorGreen;
    public static int textColorBlue;

    public static int backgroundColorAlpha;
    public static int backgroundColorRed;
    public static int backgroundColorGreen;
    public static int backgroundColorBlue;

    public static double offsetX;
    public static double offsetY;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enabled = enabledConfig.get();
        textColorRed = textColorRedConfig.get();
        textColorGreen = textColorGreenConfig.get();
        textColorBlue = textColorBlueConfig.get();

        backgroundColorAlpha = backgroundColorAlphaConfig.get();
        backgroundColorRed = backgroundColorRedConfig.get();
        backgroundColorGreen = backgroundColorGreenConfig.get();
        backgroundColorBlue = backgroundColorBlueConfig.get();

        offsetX = offsetXConfig.get();
        offsetY = offsetYConfig.get();

    }
}
