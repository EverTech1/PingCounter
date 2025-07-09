package EverTech1.pingcounter.Mixins;

import EverTech1.pingcounter.Config;
import EverTech1.pingcounter.Events;
import EverTech1.pingcounter.Main;
import EverTech1.pingcounter.Pinger;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Gui.class)
public class DrawTextMixin {
    @Inject(method="renderChat", at=@At("RETURN"))
    private void renderPing(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci){
        if(Config.enabled && !Main.isEditing &&!Minecraft.getInstance().isLocalServer()&&!Minecraft.getInstance().gui.getDebugOverlay().showDebugScreen()){
            Events.drawPing(guiGraphics);
        }
    }
}
