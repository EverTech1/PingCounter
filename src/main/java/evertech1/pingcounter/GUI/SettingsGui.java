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
    private SliderWidget scaleSlider;
    private List<Drawable> drawables;

    public SettingsGui(Text pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        drawables = Lists.newArrayList();
        cfg = ConfigHandler.config;
        boxCornerX = (width/2)-(boxWidth/2);
        boxCornerY = (height/2)-(boxHeight/2);
        final int backStringLength = textRenderer.getWidth("Back");
        toggleButton = new ButtonWidget.Builder(Text.literal(cfg.enabled ? "Enabled" : "Disabled"), onPressButton(1)).position(boxCornerX + 20, boxCornerY + 30).size(textRenderer.getWidth("Disabled") + 20, 20).build();
        drawables.add(addSelectableChild(toggleButton));
        drawables.add(addSelectableChild(new ButtonWidget.Builder(Text.literal("Back"), onPressButton(0)).position((width / 2) - (backStringLength / 2) - 30, boxCornerY + boxHeight - 30).size(backStringLength + 60, 20).build()));
        drawables.add(addSelectableChild(new ButtonWidget.Builder(Text.literal("Color Settings"), onPressButton(2)).position(boxCornerX + 20, boxCornerY + 60).size(textRenderer.getWidth("Color Settings") + 20, 20).build()));
        drawables.add(addSelectableChild(new ButtonWidget.Builder(Text.literal("Edit Position"), onPressButton(3)).position(boxCornerX + 20, boxCornerY + 90).size(textRenderer.getWidth("Edit Position") + 20, 20).build()));
        drawables.add(addSelectableChild(new ButtonWidget.Builder(Text.literal("Reset"), onPressButton(4)).position(boxCornerX + 290, boxCornerY + 150).size(textRenderer.getWidth("Reset") + 60, 20).build()));
        DecimalFormat scaleFormat = new DecimalFormat("Scale: 0.00");
        scaleSlider = new SliderWidget(boxCornerX + 20, boxCornerY + 120, 256, 20, Text.literal(scaleFormat.format(Math.round(cfg.scale*500)/500.0)), cfg.scale/5.0) {

            @Override
            protected void updateMessage() {
                this.setMessage(Text.literal(scaleFormat.format(Math.round(cfg.scale*500)/500.0)));
            }

            @Override
            protected void applyValue() {
                cfg.scale = 0.1+0.05*Math.floor(98*value);
            }
        };
        drawables.add(addSelectableChild(scaleSlider));
        textBox = new TextFieldWidget(textRenderer, 256, 20, Text.literal("Enter display text here"));
        textBox.setText(cfg.displayText.replaceAll("(%%%%)", "%").replaceAll("(%1\\$d)", Matcher.quoteReplacement("$[ping]")));
        textBox.setPosition(boxCornerX+20, boxCornerY+150);
        drawables.add(addSelectableChild(textBox));
        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.fill(boxCornerX, boxCornerY, boxCornerX + boxWidth, boxCornerY + boxHeight, 0xA0000000);
        for(Drawable drawable : this.drawables){
            drawable.render(context, mouseX, mouseY, deltaTicks);
        }
        context.drawText(textRenderer, "Ping Counter settings", boxCornerX + 20, boxCornerY + 10, 0xFFFFFFFF, false);
    }

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
}
