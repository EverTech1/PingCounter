package evertech1.pingcounter.GUI;

import evertech1.pingcounter.Config;
import evertech1.pingcounter.ConfigHandler;
import evertech1.pingcounter.PingCounter;
import evertech1.pingcounter.RenderPing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class EditPositionGui extends Screen {
    private Config cfg;
    private MinecraftClient minecraft = MinecraftClient.getInstance();
    private final Screen parentScreen;
    private ButtonWidget backButton;
    private ButtonWidget resetButton;
    public EditPositionGui(Text title, Screen parent){
        super(title);
        parentScreen = parent;
    }

    @Override
    protected void init() {
        PingCounter.isEditing = true;
        cfg = ConfigHandler.config;

        backButton = addSelectableChild(new ButtonWidget.Builder(Text.literal("Back"), button->{
            ConfigHandler.updateValues();
            PingCounter.isEditing = false;
            minecraft.setScreen(parentScreen);
        }).position((width/2)-(textRenderer.getWidth("Back")/2)-15, height-30).size(textRenderer.getWidth("Back")+30, 20).build());
        resetButton = addSelectableChild(new ButtonWidget.Builder(Text.literal("Reset"), button -> {
            cfg.posX = 0.028;
            cfg.posY = 0.05;
        }).position((width/2)-(textRenderer.getWidth("Reset")/2)-15, height-60).size(textRenderer.getWidth("Reset")+30, 20).build());

        super.init();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        context.fill(0, 0, width, height, -11, 0xA0000000);
        RenderPing.renderOverlay(context);
        backButton.render(context, mouseX, mouseY, deltaTicks);
        resetButton.render(context, mouseX, mouseY, deltaTicks);
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        final int[] pos = {(int) (minecraft.getWindow().getScaledWidth()*cfg.posX), (int) (minecraft.getWindow().getScaledHeight()*cfg.posY)};
        final double scale = 3*cfg.scale/minecraft.getWindow().getScaleFactor();
        final int stringSize = textRenderer.getWidth(String.format(cfg.displayText, 999));
        if(pMouseX-pDragX>=pos[0]-(int)(5*scale) && pMouseX-pDragX<=pos[0]+(int)((stringSize+5)*scale) && pMouseY-pDragY>=pos[1]-(int)(5*scale) && pMouseY-pDragY<=pos[1]+(int)((textRenderer.fontHeight+4)*scale)){
            cfg.posX = Math.min(Math.max(cfg.posX+pDragX/width, 0), 1);
            cfg.posY = Math.min(Math.max(cfg.posY+pDragY/height, 0), 1);
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public void close() {
        PingCounter.isEditing = false;
        super.close();

    }
}
