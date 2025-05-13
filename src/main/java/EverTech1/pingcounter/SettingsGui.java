package EverTech1.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Supplier;

public class SettingsGui extends Screen {
    private Minecraft minecraft;
    private final int boxWidth = 400;
    private final int boxHeight = 230;
    private Screen parentScreen;
    private int boxCornerX;
    private int boxCornerY;
    private Button toggleButton;
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
        toggleButton  = new Button.Builder(Component.literal("Toggle"), onPressButton(1)).pos(boxCornerX+20, boxCornerY+30).size(font.width("Disabled")+20, 20).build();
        addRenderableWidget(new Button.Builder(Component.literal("Back"), onPressButton(0)).pos((width / 2) - (backStringLength / 2) - 30, boxCornerY + boxHeight - 30).size(backStringLength + 60, 20).build());
        addRenderableWidget(toggleButton);
        //addRenderableWidget(new Button.Builder(Component.literal("sex"), onPressButton(0)).pos((width / 2) - (backStringLength / 2) - 30, boxCornerY + boxHeight - 30).size(backStringLength + 60, 20).build());
        //addRenderableWidget(new Button.Builder(Component.literal("sex"), onPressButton(0)).pos((width / 2) - (backStringLength / 2) - 30, boxCornerY + boxHeight - 30).size(backStringLength + 60, 20).build());
        //addRenderableWidget(new Button.Builder(Component.literal("sex"), onPressButton(0)).pos((width / 2) - (backStringLength / 2) - 30, boxCornerY + boxHeight - 30).size(backStringLength + 60, 20).build());
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.fill(boxCornerX, boxCornerY, boxCornerX+boxWidth, boxCornerY+boxHeight, 0xA0000000);
        for(Renderable renderable : this.renderables){
            renderable.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
        //super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        pGuiGraphics.drawString(font, "Ping Counter settings", boxCornerX+20, boxCornerY+10, 0xFFFFFFFF);
    }

    private Button.OnPress onPressButton(int id){
        return new Button.OnPress() {
            @Override
            public void onPress(Button pButton) {
                switch(id){
                    case 0:
                        minecraft.setScreen(parentScreen);
                        break;
                    case 1:
                        toggleButton.setMessage(Component.literal("sex"));
                        break;
                }
            }
        };
    }
}
