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
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;


public class SettingsGui extends Screen {
    private final int boxWidth = 400;
    private final int boxHeight = 230;
    private int boxCornerX;
    private int boxCornerY;
    private Config cfg;
    private ButtonWidget toggleButton;
    private TextFieldWidget textBox;
    private List<Drawable> drawables;
    private float scaleFactor = 1;
    public SettingsGui(Text pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        drawables = Lists.newArrayList();
        cfg = ConfigHandler.config;
        scaleFactor = Math.min(1.0f, Math.min((float)width/boxWidth, (float)height/boxHeight));
        boxCornerX = (int) ((width/2.0)-(boxWidth*scaleFactor/2.0));
        boxCornerY = (int) ((height/2.0)-(boxHeight*scaleFactor/2.0));
        final int backStringLength = textRenderer.getWidth("Back");
        toggleButton = new ButtonWidget.Builder(Text.literal(cfg.enabled ? "Enabled" : "Disabled"), onPressButton(1)).position(20, 30).size(textRenderer.getWidth("Disabled") + 20, 20).build();
        drawables.add(addSelectableChild(toggleButton));
        drawables.add(addSelectableChild(new ButtonWidget.Builder(Text.literal("Back"), onPressButton(0)).position((boxWidth / 2) - (backStringLength / 2) - 30, boxHeight - 30).size(backStringLength + 60, 20).build()));
        drawables.add(addSelectableChild(new ButtonWidget.Builder(Text.literal("Color Settings"), onPressButton(2)).position(20, 60).size(textRenderer.getWidth("Color Settings") + 20, 20).build()));
        drawables.add(addSelectableChild(new ButtonWidget.Builder(Text.literal("Edit Position"), onPressButton(3)).position(20, 90).size(textRenderer.getWidth("Edit Position") + 20, 20).build()));
        drawables.add(addSelectableChild(new ButtonWidget.Builder(Text.literal("Reset"), onPressButton(4)).position(290, 150).size(textRenderer.getWidth("Reset") + 60, 20).build()));
        DecimalFormat scaleFormat = new DecimalFormat("Scale: 0.00");
        SliderWidget scaleSlider = new SliderWidget(20, 120, 256, 20, Text.literal(scaleFormat.format(Math.round(cfg.scale * 500) / 500.0)), cfg.scale / 5.0) {

            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal(scaleFormat.format(Math.round(cfg.scale * 500) / 500.0)));
            }

            @Override
            protected void applyValue() {
                cfg.scale = 0.1 + 0.05 * Math.floor(98 * value);
            }
        };
        drawables.add(addSelectableChild(scaleSlider));
        textBox = new TextFieldWidget(textRenderer, 256, 20, Text.literal("Enter display text here"));
        textBox.setText(cfg.displayText.replaceAll("(%%%%)", "%").replaceAll("(%1\\$d)", Matcher.quoteReplacement("$[ping]")));
        textBox.setPosition(20, 150);
        drawables.add(addSelectableChild(textBox));
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.getMatrices().pushMatrix();
        context.getMatrices().translate(boxCornerX, boxCornerY);
        context.getMatrices().scale(scaleFactor, scaleFactor);
        context.fill(0, 0, boxWidth, boxHeight, 0xA0000000);
        for(Drawable drawable : this.drawables){
            drawable.render(context, (int)((mouseX-boxCornerX)/scaleFactor), (int)((mouseY-boxCornerY)/scaleFactor), deltaTicks);
        }
        context.drawText(textRenderer, "Ping Counter settings", 20, 10, 0xFFFFFFFF, false);
        context.getMatrices().popMatrix();
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float deltaTicks){}

    private ButtonWidget.PressAction onPressButton(int id){
        return pressAction -> {
            switch(id){
                case 0:
                    close();
                    break;
                case 1:
                    cfg.enabled = !cfg.enabled;
                    toggleButton.setMessage(Text.literal(cfg.enabled?"Enabled":"Disabled"));
                    break;
                case 2:
                    MinecraftClient.getInstance().setScreen(new ColorSettingsGui(Text.literal("Edit color"), this));
                    break;
                case 3:
                    MinecraftClient.getInstance().setScreen(new EditPositionGui(Text.literal("Edit Position"), this));
                    break;
                case 4:
                    textBox.setText("Ping: $[ping]ms");
                    break;
            }
        };
    }

    @Override
    public void close() {
        if(textBox.getText().isBlank()) textBox.setText("Ping: $[ping]ms");
        ConfigHandler.updateValues();
        super.close();
    }

    @Override
    public void tick() {
        cfg.displayText = textBox.getText().replaceAll("%", "%%").replaceAll("(\\$\\[ping])", Matcher.quoteReplacement("%1$d"));
        super.tick();
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
