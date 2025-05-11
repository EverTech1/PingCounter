package EverTech1.pingcounter.Mixins;

import EverTech1.pingcounter.Main;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class DrawTextMixin {
    @Inject(method="render", at=@At("TAIL"))
    private void renderCustomText(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){
        Main.textOffset=(Main.textOffset+0.005f)%50;
        guiGraphics.fill(5, 5, 100, 35, 0x9FFFFFFF);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(0.5f, 0.5f, 0.5f);
        guiGraphics.drawString(Minecraft.getInstance().font, "Ass", 10.5f+Main.textOffset, 10.5f, 0xFFFFFFFF, false);
        guiGraphics.pose().popPose();
    }
}
