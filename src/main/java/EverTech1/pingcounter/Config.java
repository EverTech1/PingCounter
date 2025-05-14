package EverTech1.pingcounter;

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
    private static final ForgeConfigSpec.BooleanValue textShadowConfig = BUILDER.define("textShadow", true);
    private static final ForgeConfigSpec.DoubleValue scaleConfig = BUILDER.defineInRange("scale", 1.0, 0.0, 10.0);
    private static final ForgeConfigSpec.DoubleValue posXConfig = BUILDER.defineInRange("posX", 0.028, 0.0, 1.0);
    private static final ForgeConfigSpec.DoubleValue posYConfig = BUILDER.defineInRange("posY", 0.05, 0.0, 1.0);
    private static final ForgeConfigSpec.ConfigValue<String> displayTextConfig = BUILDER.define("displayText", "Ping %1$dms");

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean enabled;

    public static int textColorRed;
    public static int textColorGreen;
    public static int textColorBlue;

    public static int backgroundColorAlpha;
    public static int backgroundColorRed;
    public static int backgroundColorGreen;
    public static int backgroundColorBlue;
    public static boolean textShadow;
    public static double scale;
    public static double posX;
    public static double posY;
    public static String displayText;



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
        textShadow = textShadowConfig.get();
        scale = scaleConfig.get();

        posX = posXConfig.get();
        posY = posYConfig.get();
        displayText = displayTextConfig.get();
    }

    static void updateConfig(){
        enabledConfig.set(enabled);
        textColorRedConfig.set(textColorRed);
        textColorGreenConfig.set(textColorGreen);
        textColorBlueConfig.set(textColorBlue);
        backgroundColorAlphaConfig.set(backgroundColorAlpha);
        backgroundColorRedConfig.set(backgroundColorRed);
        backgroundColorGreenConfig.set(backgroundColorGreen);
        backgroundColorBlueConfig.set(backgroundColorBlue);
        textShadowConfig.set(textShadow);
        scaleConfig.set(scale);
        posXConfig.set(posX);
        posYConfig.set(posY);
        displayTextConfig.set(displayText);
    }
}
