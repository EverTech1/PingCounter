package EverTech1.pingcounter.GUI;

import EverTech1.pingcounter.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;

public class ColorSettingsGui extends Screen {
    private Minecraft minecraft;
    private final int boxWidth = 327;
    private final int boxHeight = 270;
    private final Screen parentScreen;
    private int boxCornerX;
    private int boxCornerY;
    private float scaleFactor = 1;

    private ExtendedSlider sliderTextR;
    private ExtendedSlider sliderTextG;
    private ExtendedSlider sliderTextB;

    private ExtendedSlider sliderBackgroundA;
    private ExtendedSlider sliderBackgroundR;
    private ExtendedSlider sliderBackgroundG;
    private ExtendedSlider sliderBackgroundB;


    public ColorSettingsGui(Component pTitle, Screen parent) {
        super(pTitle);
        parentScreen = parent;
    }

    @Override
    protected void init() {
        super.init();
        minecraft = Minecraft.getInstance();
        scaleFactor = Math.min(1.0f, Math.min((float)width/boxWidth, (float)height/boxHeight));
        boxCornerX = (int) ((width/2.0)-(boxWidth*scaleFactor/2.0));
        boxCornerY = (int) ((height/2.0)-(boxHeight*scaleFactor/2.0));
        int backLength = font.width("Back");
        //Buttons
        addRenderableWidget(new Button.Builder(Component.literal("Back"), (pButton -> minecraft.setScreen(parentScreen))).pos(boxWidth/2-backLength/2-30, boxHeight-30).size(backLength+60, 20).build());
        addRenderableWidget(new Button.Builder(Component.literal(Config.textShadow ? "Shadow: On " : "Shadow: Off"), pButton -> {
            Config.textShadow = !Config.textShadow;
            pButton.setMessage(Component.literal(Config.textShadow ? "Shadow: On " : "Shadow: Off"));
        }).pos(20, 205).size(font.width("Shadow: Off"+20), 20).build());
        //Sliders
        //Background
        sliderBackgroundA = new ExtendedSlider(20, 20, 256, 20, Component.literal("Opacity: "), Component.literal("%"),0, 100, (double) Config.backgroundColorAlpha /2.55, 1, 0, true);
        sliderBackgroundR = new ExtendedSlider(20, 45, 256, 20, Component.literal("Red: "), Component.literal(""),0, 255, Config.backgroundColorRed, 1, 0, true);
        sliderBackgroundG = new ExtendedSlider(20, 70, 256, 20, Component.literal("Green: "), Component.literal(""),0, 255, Config.backgroundColorGreen, 1, 0, true);
        sliderBackgroundB = new ExtendedSlider(20, 95, 256, 20, Component.literal("Blue: "), Component.literal(""),0, 255, Config.backgroundColorBlue, 1, 0, true);
        //Text
        sliderTextR = new ExtendedSlider(20, 130, 256, 20, Component.literal("Red: "), Component.literal(""),0, 255, Config.textColorRed, 1, 0, true);
        sliderTextG = new ExtendedSlider(20, 155, 256, 20, Component.literal("Green: "), Component.literal(""),0, 255, Config.textColorGreen, 1, 0, true);
        sliderTextB = new ExtendedSlider(20, 180, 256, 20, Component.literal("Blue: "), Component.literal(""),0, 255, Config.textColorBlue, 1, 0, true);

        addRenderableWidget(sliderBackgroundA);
        addRenderableWidget(sliderBackgroundR);
        addRenderableWidget(sliderBackgroundG);
        addRenderableWidget(sliderBackgroundB);
        addRenderableWidget(sliderTextR);
        addRenderableWidget(sliderTextG);
        addRenderableWidget(sliderTextB);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        final int textColor = 0x10000*Config.textColorRed + 0x100*Config.textColorGreen + Config.textColorBlue;
        final int backgroundColor = 0x10000*Config.backgroundColorRed + 0x100*Config.backgroundColorGreen + Config.backgroundColorBlue;

        guiGraphics.pose().translate(boxCornerX, boxCornerY, 0);
        guiGraphics.pose().scale(scaleFactor, scaleFactor, 1);
        guiGraphics.fill(0, 0, boxWidth, boxHeight, 0xA0000000);
        guiGraphics.drawString(font, "Background:", 20, 10, 0xFFFFFFFF);
        guiGraphics.drawString(font, "Text:", 20, 120, 0xFFFFFFFF);
        //Draw outline
        guiGraphics.fill(285, 44, 307, 116, 0xFFFFFFFF);
        guiGraphics.fill(285, 129, 307, 201, 0xFFFFFFFF);
        //Draw samples
        guiGraphics.fill(286, 45, 306, 115, backgroundColor|0xFF000000);
        guiGraphics.fill(286, 130, 306, 200, textColor|0xFF000000);
        for(Renderable renderable : this.renderables){
            renderable.render(guiGraphics, (int)((mouseX-boxCornerX)/scaleFactor), (int)((mouseY-boxCornerY)/scaleFactor), partialTick);
        }
        guiGraphics.pose().popPose();
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked((int)((mouseX-boxCornerX)/scaleFactor), (int)((mouseY-boxCornerY)/scaleFactor), button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return super.mouseDragged((int)((mouseX-boxCornerX)/scaleFactor), (int)((mouseY-boxCornerY)/scaleFactor), button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return super.mouseReleased((int)((mouseX-boxCornerX)/scaleFactor), (int)((mouseY-boxCornerY)/scaleFactor), button);
    }
}
