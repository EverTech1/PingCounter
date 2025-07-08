package EverTech1.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;

import java.util.regex.Matcher;


public class SettingsGui extends Screen {
    private Minecraft minecraft;
    private final int boxWidth = 400;
    private final int boxHeight = 230;
    private int boxCornerX;
    private int boxCornerY;
    private Button toggleButton;
    private EditBox editBox;
    private ExtendedSlider scaleSlider;
    private float scaleFactor = 1;
    protected SettingsGui(Component pTitle) {
        super(pTitle);
    }

    @Override
    protected void init() {
        super.init();
        minecraft = Minecraft.getInstance();
        scaleFactor = Math.min(1.0f, Math.min((float)width/boxWidth, (float)height/boxHeight));
        boxCornerX = (int) ((width/2.0)-(boxWidth*scaleFactor/2.0));
        boxCornerY = (int) ((height/2.0)-(boxHeight*scaleFactor/2.0));
        final int backStringLength = font.width("Back");
        toggleButton  = new Button.Builder(Component.literal(Config.enabled?"Enabled":"Disabled"), onPressButton(1)).pos(20, 30).size(font.width("Disabled")+20, 20).build();
        addRenderableWidget(new Button.Builder(Component.literal("Back"), onPressButton(0)).pos((boxWidth / 2) - (backStringLength / 2) - 30, boxHeight - 30).size(backStringLength + 60, 20).build());
        addRenderableWidget(toggleButton);
        addRenderableWidget(new Button.Builder(Component.literal("Color Settings"), onPressButton(2)).pos(20, 60).size(font.width("Color Settings") + 20, 20).build());
        addRenderableWidget(new Button.Builder(Component.literal("Edit Position"), onPressButton(3)).pos(20, 90).size(font.width("Edit Position") + 20, 20).build());
        addRenderableWidget(new Button.Builder(Component.literal("Reset"), onPressButton(4)).pos(290, 150).size(font.width("Reset")+60, 20).build());

        scaleSlider = new ExtendedSlider(20, 120, 256, 20, Component.literal("Scale: "), Component.literal(""), 0.1, 5.0, Config.scale, 0.05, 0, true);
        addRenderableWidget(scaleSlider);

        editBox = new EditBox(font, 256, 20, Component.literal("Enter display text here"));
        editBox.setValue(Config.displayText.replaceAll("(%%%%)", "%").replaceAll("(%1\\$d)", Matcher.quoteReplacement("$[ping]")));
        editBox.setPosition(20, 150);
        addRenderableWidget(editBox);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float pPartialTick) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(boxCornerX, boxCornerY, 0);
        guiGraphics.pose().scale(scaleFactor, scaleFactor, 0);
        guiGraphics.fill(0, 0, boxWidth, boxHeight, 0xA0000000);
        for(Renderable renderable : this.renderables){
            renderable.render(guiGraphics, (int)((mouseX-boxCornerX)/scaleFactor), (int)((mouseY-boxCornerY)/scaleFactor), pPartialTick);
        }
        guiGraphics.drawString(font, "Ping Counter settings", 20, 10, 0xFFFFFFFF);
        guiGraphics.pose().popPose();
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
                case 2:
                    minecraft.setScreen(new ColorSettingsGui(Component.literal("Edit color"), this));
                    break;
                case 3:
                    minecraft.setScreen(new EditPositionGui(Component.literal("Edit Position"), this));
                    break;
                case 4:
                    editBox.setValue("Ping: $[ping]ms");
                    break;
            }
        };
    }

    @Override
    public void onClose() {
        if(editBox.getValue().isBlank()) editBox.setValue("Ping: $[ping]ms");
        Config.updateConfig();
        super.onClose();
    }

    @Override
    public void tick() {
        Config.scale = scaleSlider.getValue();
        Config.displayText = editBox.getValue().replaceAll("%", "%%%%").replaceAll("(\\$\\[ping])", Matcher.quoteReplacement("%1$d"));
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
