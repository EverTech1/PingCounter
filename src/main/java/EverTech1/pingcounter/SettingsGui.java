package EverTech1.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.gui.widget.ForgeSlider;

import java.util.regex.Matcher;


public class SettingsGui extends Screen {
    private Minecraft minecraft;
    private final int boxWidth = 400;
    private final int boxHeight = 230;
    private Screen parentScreen;
    private int boxCornerX;
    private int boxCornerY;
    private Button toggleButton;
    private EditBox editBox;
    private ForgeSlider scaleSlider;
    protected SettingsGui(Component pTitle, Screen parent) {
        super(pTitle);
        parentScreen = parent;
    }

    @Override
    protected void init() {
        super.init();
        minecraft = Minecraft.getInstance();
        boxCornerX = (width/2)-(boxWidth/2);
        boxCornerY = (height/2)-(boxHeight/2);
        final int backStringLength = font.width("Back");
        toggleButton  = new Button.Builder(Component.literal(Config.enabled?"Enabled":"Disabled"), onPressButton(1)).pos(boxCornerX+20, boxCornerY+30).size(font.width("Disabled")+20, 20).build();
        addRenderableWidget(new Button.Builder(Component.literal("Back"), onPressButton(0)).pos((width / 2) - (backStringLength / 2) - 30, boxCornerY + boxHeight - 30).size(backStringLength + 60, 20).build());
        addRenderableWidget(toggleButton);
        addRenderableWidget(new Button.Builder(Component.literal("Color Settings"), onPressButton(2)).pos(boxCornerX+20, boxCornerY + 60).size(font.width("Color Settings") + 20, 20).build());
        addRenderableWidget(new Button.Builder(Component.literal("Edit Position"), onPressButton(3)).pos(boxCornerX+20, boxCornerY + 90).size(font.width("Edit Position") + 20, 20).build());
        addRenderableWidget(new Button.Builder(Component.literal("Reset"), onPressButton(4)).pos(boxCornerX+290, boxCornerY + 150).size(font.width("Reset")+60, 20).build());

        scaleSlider = new ForgeSlider(boxCornerX+20, boxCornerY+120, 256, 20, Component.literal("Scale: "), Component.literal(""), 0.1, 10.0, Config.scale, 0.1, 0, true);
        addRenderableWidget(scaleSlider);

        editBox = new EditBox(font, 256, 20, Component.literal("Enter display text here"));
        editBox.setValue(Config.displayText.replaceAll("(%%%%)", "%").replaceAll("(%1\\$d)", Matcher.quoteReplacement("$[ping]")));
        editBox.setPosition(boxCornerX+20, boxCornerY+150);
        addRenderableWidget(editBox);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.fill(boxCornerX, boxCornerY, boxCornerX+boxWidth, boxCornerY+boxHeight, 0xA0000000);
        for(Renderable renderable : this.renderables){
            renderable.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
        pGuiGraphics.drawString(font, "Ping Counter settings", boxCornerX+20, boxCornerY+10, 0xFFFFFFFF);
    }

    private Button.OnPress onPressButton(int id){
        return pButton -> {
            switch(id){
                case 0:
                    onClose();
                    break;
                case 1:
                    Config.enabled = !Config.enabled;
                    toggleButton.setMessage(Component.literal(Config.enabled?"Enabled":"Disabled"));
                    break;
                case 3:
                    minecraft.setScreen(new EditPositionGui(Component.literal("Edit Position"), this));
                case 4:
                    editBox.setValue("Ping: $[ping]ms");
                    break;
            }
        };
    }

    @Override
    public void onClose() {
        if(editBox.getValue().isBlank()) editBox.setValue("Ping: $[ping]ms");
        Config.displayText = editBox.getValue().replaceAll("%", "%%%%").replaceAll("(\\$\\[ping])", Matcher.quoteReplacement("%1$d"));
        Config.updateConfig();
        super.onClose();
    }

    @Override
    public void tick() {
        Config.scale = scaleSlider.getValue();
        super.tick();
    }
}
