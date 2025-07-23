package evertech1.pingcounter.GUI;

import com.google.common.collect.Lists;
import evertech1.pingcounter.Config;
import evertech1.pingcounter.ConfigHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.List;

public class ColorSettingsGui extends Screen {
    private final Config cfg = ConfigHandler.config;
    private MinecraftClient minecraft;
    private final int boxWidth = 327;
    private final int boxHeight = 270;
    private final Screen parentScreen;
    private int boxCornerX;
    private int boxCornerY;
    private float scaleFactor = 1;
    private List<Drawable> drawables;
    protected ColorSettingsGui(Text title, Screen parent) {
        super(title);
        this.parentScreen = parent;
    }

    @Override
    protected void init() {
        super.init();
        drawables = Lists.newArrayList();
        minecraft = MinecraftClient.getInstance();
        scaleFactor = Math.min(1.0f, Math.min((float)width/boxWidth, (float)height/boxHeight));
        boxCornerX = (int) ((width/2.0)-(boxWidth*scaleFactor/2.0));
        boxCornerY = (int) ((height/2.0)-(boxHeight*scaleFactor/2.0));
        int backLength = textRenderer.getWidth("Back");
        //Buttons
        drawables.add(addSelectableChild(new ButtonWidget.Builder(Text.literal("Back"), (button -> minecraft.setScreen(parentScreen))).position(boxWidth/2-backLength/2-30, boxHeight-30).size(backLength+60, 20).build()));
        drawables.add(addSelectableChild(new ButtonWidget.Builder(Text.literal(cfg.textShadow ? "Shadow: On " : "Shadow: Off"), button -> {
            cfg.textShadow = !cfg.textShadow;
            button.setMessage(Text.literal(cfg.textShadow ? "Shadow: On " : "Shadow: Off"));
        }).position(20, 205).size(textRenderer.getWidth("Shadow: Off"+20), 20).build()));
        //Sliders
        //Background
        SliderWidget sliderBackgroundA = new SliderWidget(20, 20, 256, 20, Text.literal(String.format("Opacity: %d%%", Math.round(cfg.backgroundColorAlpha / 2.55))), cfg.backgroundColorAlpha / 255.0) {
            @Override
            protected void updateMessage() {
                setMessage(Text.literal(String.format("Opacity: %d%%", Math.round(cfg.backgroundColorAlpha / 2.55))));
            }

            @Override
            protected void applyValue() {
                cfg.backgroundColorAlpha = (int) Math.round(value * 255);
            }
        };
        SliderWidget sliderBackgroundR = new SliderWidget(20, 45, 256, 20, Text.literal(String.format("Red: %d", cfg.backgroundColorRed)), cfg.backgroundColorRed / 255.0) {

            @Override
            protected void updateMessage() {
                setMessage(Text.literal(String.format("Red: %d", cfg.backgroundColorRed)));
            }

            @Override
            protected void applyValue() {
                cfg.backgroundColorRed = (int) Math.round(value * 255);
            }
        };
        SliderWidget sliderBackgroundG = new SliderWidget(20, 70, 256, 20, Text.literal(String.format("Green: %d", cfg.backgroundColorGreen)), cfg.backgroundColorGreen / 255.0) {

            @Override
            protected void updateMessage() {
                setMessage(Text.literal(String.format("Green: %d", cfg.backgroundColorGreen)));
            }

            @Override
            protected void applyValue() {
                cfg.backgroundColorGreen = (int) Math.round(value * 255);
            }
        };
        SliderWidget sliderBackgroundB = new SliderWidget(20, 95, 256, 20, Text.literal(String.format("Blue: %d", cfg.backgroundColorBlue)), cfg.backgroundColorBlue / 255.0) {

            @Override
            protected void updateMessage() {
                setMessage(Text.literal(String.format("Blue: %d", cfg.backgroundColorBlue)));
            }

            @Override
            protected void applyValue() {
                cfg.backgroundColorBlue = (int) Math.round(value * 255);
            }
        };
        //Text
        SliderWidget sliderTextR = new SliderWidget(20, 130, 256, 20, Text.literal(String.format("Red: %d", cfg.textColorRed)), cfg.textColorRed / 255.0) {

            @Override
            protected void updateMessage() {
                setMessage(Text.literal(String.format("Red: %d", cfg.textColorRed)));
            }

            @Override
            protected void applyValue() {
                cfg.textColorRed = (int) Math.round(value * 255);
            }
        };
        SliderWidget sliderTextG = new SliderWidget(20, 155, 256, 20, Text.literal(String.format("Red: %d", cfg.textColorGreen)), cfg.textColorGreen / 255.0) {

            @Override
            protected void updateMessage() {
                setMessage(Text.literal(String.format("Green: %d", cfg.textColorGreen)));
            }

            @Override
            protected void applyValue() {
                cfg.textColorGreen = (int) Math.round(value * 255);
            }
        };
        SliderWidget sliderTextB = new SliderWidget(20, 180, 256, 20, Text.literal(String.format("Red: %d", cfg.textColorBlue)), cfg.textColorBlue / 255.0) {

            @Override
            protected void updateMessage() {
                setMessage(Text.literal(String.format("Blue: %d", cfg.textColorBlue)));
            }

            @Override
            protected void applyValue() {
                cfg.textColorBlue = (int) Math.round(value * 255);
            }
        };

        drawables.add(addSelectableChild(sliderBackgroundA));
        drawables.add(addSelectableChild(sliderBackgroundR));
        drawables.add(addSelectableChild(sliderBackgroundG));
        drawables.add(addSelectableChild(sliderBackgroundB));
        drawables.add(addSelectableChild(sliderTextR));
        drawables.add(addSelectableChild(sliderTextG));
        drawables.add(addSelectableChild(sliderTextB));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        final int textColor = 0x10000 * cfg.textColorRed + 0x100 * cfg.textColorGreen + cfg.textColorBlue;
        final int backgroundColor = 0x10000 * cfg.backgroundColorRed + 0x100 * cfg.backgroundColorGreen + cfg.backgroundColorBlue;
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(boxCornerX, boxCornerY);
        context.getMatrices().scale(scaleFactor, scaleFactor);
        context.fill(0, 0, boxWidth, boxHeight, 0xA0000000);
        context.drawText(textRenderer, "Background:", 20, 10, 0xFFFFFFFF, false);
        context.drawText(textRenderer, "Text:", 20, 120, 0xFFFFFFFF, false);
        //Draw outline
        context.fill(285, 44, 307, 116, 0xFFFFFFFF);
        context.fill(285, 129, 307, 201, 0xFFFFFFFF);
        //Draw samples
        context.fill(286, 45, 306, 115, backgroundColor|0xFF000000);
        context.fill(286, 130, 306, 200, textColor|0xFF000000);
        for(Drawable drawable : drawables){
            drawable.render(context, (int)((mouseX-boxCornerX)/scaleFactor), (int)((mouseY-boxCornerY)/scaleFactor), deltaTicks);
        }
        context.getMatrices().popMatrix();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks){}

    @Override
    public void close() {
        ConfigHandler.updateValues();
        super.close();
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
