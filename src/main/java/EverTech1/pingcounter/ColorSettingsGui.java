package EverTech1.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

public class ColorSettingsGui extends Screen {
    private Minecraft minecraft;
    private final int boxWidth = 327;
    private final int boxHeight = 270;
    private final Screen parentScreen;
    private int boxCornerX;
    private int boxCornerY;

    private ForgeSlider sliderTextR;
    private ForgeSlider sliderTextG;
    private ForgeSlider sliderTextB;

    private ForgeSlider sliderBackgroundA;
    private ForgeSlider sliderBackgroundR;
    private ForgeSlider sliderBackgroundG;
    private ForgeSlider sliderBackgroundB;


    protected ColorSettingsGui(Component pTitle, Screen parent) {
        super(pTitle);
        parentScreen = parent;
    }

    @Override
    protected void init() {
        super.init();
        minecraft = Minecraft.getInstance();
        boxCornerX = (width/2)-(boxWidth/2);
        boxCornerY = (height/2)-(boxHeight/2);
        int backLength = font.width("Back");
        //Buttons
        addRenderableWidget(new Button.Builder(Component.literal("Back"), (pButton -> minecraft.setScreen(parentScreen))).pos(boxCornerX+boxWidth/2-backLength/2-30, boxCornerY+boxHeight-30).size(backLength+60, 20).build());
        addRenderableWidget(new Button.Builder(Component.literal(Config.textShadow ? "Shadow: On " : "Shadow: Off"), pButton -> {
            Config.textShadow = !Config.textShadow;
            pButton.setMessage(Component.literal(Config.textShadow ? "Shadow: On " : "Shadow: Off"));
        }).pos(boxCornerX+20, boxCornerY+205).size(font.width("Shadow: Off"+20), 20).build());
        //Sliders
        //Background
        sliderBackgroundA = new ForgeSlider(boxCornerX + 20, boxCornerY+20, 256, 20, Component.literal("Opacity: "), Component.literal("%"),0, 100, (double) Config.backgroundColorAlpha /2.55, 1, 0, true);
        sliderBackgroundR = new ForgeSlider(boxCornerX + 20, boxCornerY+45, 256, 20, Component.literal("Red: "), Component.literal(""),0, 255, Config.backgroundColorRed, 1, 0, true);
        sliderBackgroundG = new ForgeSlider(boxCornerX + 20, boxCornerY+70, 256, 20, Component.literal("Green: "), Component.literal(""),0, 255, Config.backgroundColorGreen, 1, 0, true);
        sliderBackgroundB = new ForgeSlider(boxCornerX + 20, boxCornerY+95, 256, 20, Component.literal("Blue: "), Component.literal(""),0, 255, Config.backgroundColorBlue, 1, 0, true);
        //Text
        sliderTextR = new ForgeSlider(boxCornerX + 20, boxCornerY+130, 256, 20, Component.literal("Red: "), Component.literal(""),0, 255, Config.textColorRed, 1, 0, true);
        sliderTextG = new ForgeSlider(boxCornerX + 20, boxCornerY+155, 256, 20, Component.literal("Green: "), Component.literal(""),0, 255, Config.textColorGreen, 1, 0, true);
        sliderTextB = new ForgeSlider(boxCornerX + 20, boxCornerY+180, 256, 20, Component.literal("Blue: "), Component.literal(""),0, 255, Config.textColorBlue, 1, 0, true);

        addRenderableWidget(sliderBackgroundA);
        addRenderableWidget(sliderBackgroundR);
        addRenderableWidget(sliderBackgroundG);
        addRenderableWidget(sliderBackgroundB);
        addRenderableWidget(sliderTextR);
        addRenderableWidget(sliderTextG);
        addRenderableWidget(sliderTextB);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        final int textColor = 0x10000*Config.textColorRed + 0x100*Config.textColorGreen + Config.textColorBlue;
        final int backgroundColor = 0x10000*Config.backgroundColorRed + 0x100*Config.backgroundColorGreen + Config.backgroundColorBlue;

        pGuiGraphics.fill(boxCornerX, boxCornerY, boxCornerX+boxWidth, boxCornerY+boxHeight, 0xA0000000);
        pGuiGraphics.drawString(font, "Background:", boxCornerX+20, boxCornerY+10, 0xFFFFFFFF);
        pGuiGraphics.drawString(font, "Text:", boxCornerX+20, boxCornerY+120, 0xFFFFFFFF);
        //Draw outline
        pGuiGraphics.fill(boxCornerX+285, boxCornerY+44, boxCornerX+307, boxCornerY+116, 0xFFFFFFFF);
        pGuiGraphics.fill(boxCornerX+285, boxCornerY+129, boxCornerX+307, boxCornerY+201, 0xFFFFFFFF);
        //Draw samples
        pGuiGraphics.fill(boxCornerX+286, boxCornerY+45, boxCornerX+306, boxCornerY+115, backgroundColor|0xFF000000);
        pGuiGraphics.fill(boxCornerX+286, boxCornerY+130, boxCornerX+306, boxCornerY+200, textColor|0xFF000000);
        for(Renderable renderable : this.renderables){
            renderable.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
    }

    @Override
    public void tick() {
        Config.backgroundColorAlpha = (int)Math.round(2.55*sliderBackgroundA.getValue());
        Config.backgroundColorRed = sliderBackgroundR.getValueInt();
        Config.backgroundColorGreen = sliderBackgroundG.getValueInt();
        Config.backgroundColorBlue = sliderBackgroundB.getValueInt();

        Config.textColorRed = sliderTextR.getValueInt();
        Config.textColorGreen = sliderTextG.getValueInt();
        Config.textColorBlue = sliderTextB.getValueInt();

        super.tick();
    }

    @Override
    public void onClose() {
        Config.updateConfig();
        super.onClose();
    }
}
