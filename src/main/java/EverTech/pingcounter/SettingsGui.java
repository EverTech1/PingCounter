package EverTech.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraftforge.fml.client.config.GuiSlider;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SettingsGui extends GuiScreen {
    public Minecraft mc = Minecraft.getMinecraft();
    private boolean fromKeybind = false;
    public SettingsGui(boolean fromKeybind){
        this.fromKeybind = fromKeybind;
    }
    int scaledWidth = new ScaledResolution(mc).getScaledWidth();
    int scaledHeight = new ScaledResolution(mc).getScaledHeight();
    private final List<Integer> allowed = Arrays.asList(14, 28, 199, 200, 203, 205, 207, 208, 211);
    private final int size = Minecraft.getMinecraft().fontRendererObj.getStringWidth("Ping: ms");
    private final int numSize = Minecraft.getMinecraft().fontRendererObj.getStringWidth("999");
    private final String[][] strings = {
            {"Top Left", "10", "10"}, {"Top Center",Integer.toString((scaledWidth/2)-size),"10"}, {"Top Right", Integer.toString((scaledWidth-size)-10-numSize), "10"},
            {"Middle Left","10" ,Integer.toString(scaledHeight/2) }, {"Middle Center",Integer.toString((scaledWidth/2)-size), Integer.toString(scaledHeight/2)}, {"Middle Right",Integer.toString(scaledWidth-size-10-numSize) ,Integer.toString(scaledHeight/2) },
            {"Bottom Left","10", Integer.toString(scaledHeight-10)}, {"Bottom Center",Integer.toString((scaledWidth/2)-size),Integer.toString(scaledHeight-10) }, {"Bottom Right",Integer.toString(scaledWidth-size-10-numSize), Integer.toString(scaledHeight-10)},
            {"Custom", String.valueOf(Main.customX), String.valueOf(Main.customY)}
    };

    private GuiTextField txtFieldX;
    private GuiTextField txtFieldY;

    @Override
    public void initGui() {
        super.initGui();
        mc.updateDisplay();
        int stringSize = mc.fontRendererObj.getStringWidth("Back");
        buttonList.add(new GuiButton(0,(scaledWidth/2)-(stringSize/2)-30, (scaledHeight/2)+70, stringSize+60, 20, "Back"));
        buttonList.add(new GuiButton(1, (scaledWidth/2)-180, (scaledHeight/2)-70, mc.fontRendererObj.getStringWidth("Toggle")+20, 20, "Toggle"));
        buttonList.add(new GuiButton(2, (scaledWidth/2)-180, (scaledHeight/2)-40, mc.fontRendererObj.getStringWidth("Change Position")+20, 20, "Change Position"));
        buttonList.add(new GuiButton(3, (scaledWidth/2)-180, (scaledHeight/2)-10,mc.fontRendererObj.getStringWidth("Color Settings")+20, 20, "Color Settings"));
        int offset = (scaledWidth/2)+mc.fontRendererObj.getStringWidth("Change Position")+mc.fontRendererObj.getStringWidth("XXXXXXXXXXXXX");
        this.txtFieldX = new GuiTextField(6, mc.fontRendererObj, offset-100, (scaledHeight/2)-35, 50, 20);
        this.txtFieldY = new GuiTextField(7, mc.fontRendererObj, offset-20, (scaledHeight/2)-35, 50, 20);
        txtFieldX.setText(String.valueOf(Main.customX));
        txtFieldY.setText(String.valueOf(Main.customY));
        txtFieldX.setMaxStringLength(5);
        txtFieldY.setMaxStringLength(5);
    }
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int offset = (scaledWidth/2)+mc.fontRendererObj.getStringWidth("Change Position")+mc.fontRendererObj.getStringWidth("XXXXXXXXXXXXX");
        drawRect((scaledWidth/2)-200,(scaledHeight/2)-100, (scaledWidth/2)+200, (scaledHeight/2)+100, 0xA0000000);
        if(Main.selection == 9){
            txtFieldX.drawTextBox();
            txtFieldY.drawTextBox();
            drawString(mc.fontRendererObj, "X: ", offset-120, (scaledHeight/2)-30, 0xFFFFFFFF);
            drawString(mc.fontRendererObj, "Y: ", offset-40, (scaledHeight/2)-30, 0xFFFFFFFF);
        }
        drawString(mc.fontRendererObj, "Ping Counter settings", (scaledWidth/2)-190, (scaledHeight/2)-90, 0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
        String toWrite = Main.enableDisplay ? "Enabled" : "Disabled";
        drawString(mc.fontRendererObj, toWrite, (scaledWidth/2)-140+mc.fontRendererObj.getStringWidth("Toggle"), (scaledHeight/2)-65, 0xFFFFFF);
        drawString(mc.fontRendererObj, strings[Main.selection][0], (scaledWidth/2)-140+mc.fontRendererObj.getStringWidth("Change Position"), (scaledHeight/2)-35, 0xFFFFFF);
    }

    @Override
    public void onGuiClosed() {
        new UpdateConfigs().updatePos(Main.positionX, Main.positionY, Main.customX, Main.customY, Main.selection);
        super.onGuiClosed();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if(Main.selection == 9){
            if(allowed.contains(keyCode)){
                txtFieldX.textboxKeyTyped(typedChar, keyCode);
                txtFieldY.textboxKeyTyped(typedChar, keyCode);
            }
            else {
                try {
                    Integer.parseInt(String.valueOf(typedChar));
                    txtFieldX.textboxKeyTyped(typedChar, keyCode);
                    txtFieldY.textboxKeyTyped(typedChar, keyCode);
                } catch (Throwable err) {
                    //I do not care
                }
            }
            Main.positionX = Main.customX = Objects.equals(txtFieldX.getText(), "") ? 0 : Integer.parseInt(txtFieldX.getText());
            Main.positionY = Main.customY = Objects.equals(txtFieldY.getText(), "") ? 0 : Integer.parseInt(txtFieldY.getText());
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        txtFieldX.mouseClicked(mouseX, mouseY, mouseButton);
        txtFieldY.mouseClicked(mouseX, mouseY, mouseButton);
    }
    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        switch(button.id){
            case 0:
                if (fromKeybind) {
                    mc.thePlayer.closeScreen();
                } else {
                    mc.displayGuiScreen(new GuiIngameMenu());
                }
                break;
            case 1:
                Main.enableDisplay = !Main.enableDisplay;
                break;
            case 2:
                Main.selection = (Main.selection + 1)%10;
                String[] selected = strings[Main.selection];
                Main.positionX = Main.selection == 9 ? Main.customX : Integer.parseInt(selected[1]);
                Main.positionY = Main.selection == 9 ? Main.customY : Integer.parseInt(selected[2]);;
                break;
            case 3:
                mc.displayGuiScreen(new ColorSettingsGui(this));
                break;
        }
    }
}

