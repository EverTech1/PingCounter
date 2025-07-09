package EverTech1.pingcounter.GUI;

import EverTech1.pingcounter.Config;
import EverTech1.pingcounter.Events;
import EverTech1.pingcounter.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class EditPositionGui extends Screen {
    private Minecraft mc;
    private final Screen parent;
    private boolean grabbed = false;
    protected EditPositionGui(Component pTitle, Screen parent) {
        super(pTitle);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        mc = Minecraft.getInstance();
        addRenderableWidget(new Button.Builder(Component.literal("Back"), buttonOnPress(0)).pos((width/2)-(font.width("Back")/2)-15, height-30).size(font.width("Back")+30, 20).build());
        addRenderableWidget(new Button.Builder(Component.literal("Reset"), buttonOnPress(1)).pos((width/2)-(font.width("Reset")/2)-15, height-60).size(font.width("Reset")+30, 20).build());
        Main.isEditing = true;
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        pGuiGraphics.fill(0, 0, width, height, -11, 0xA0000000);
        Events.drawPing(pGuiGraphics);
        for(Renderable renderable : this.renderables){
            renderable.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        }
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if(grabbed){
            Config.posX = Math.min(Math.max(Config.posX+pDragX/width, 0), 1);
            Config.posY = Math.min(Math.max(Config.posY+pDragY/height, 0), 1);
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    private Button.OnPress buttonOnPress(int id){
        return pButton -> {
            if(id==0){
                Main.isEditing = false;
                mc.setScreen(parent);
            }else{
                Config.posX = 0.028;
                Config.posY = 0.05;
            }
        };
    }

    @Override
    public void onClose() {
        Main.isEditing = false;
        Config.updateConfig();
        super.onClose();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        final int[] pos = {(int) (minecraft.getWindow().getGuiScaledWidth()*Config.posX), (int) (minecraft.getWindow().getGuiScaledHeight()*Config.posY)};
        final double scale = 3*Config.scale/minecraft.getWindow().getGuiScale();
        final int stringSize = font.width(String.format(Config.displayText, 999));
        if(mouseX>=pos[0]-(int)(5*scale) && mouseX<=pos[0]+(int)((stringSize+5)*scale) && mouseY>=pos[1]-(int)(5*scale) && mouseY<=pos[1]+(int)((font.lineHeight+4)*scale)){
            grabbed = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        grabbed = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }
}
