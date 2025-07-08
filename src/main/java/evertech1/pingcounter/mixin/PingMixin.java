package evertech1.pingcounter.mixin;

import evertech1.pingcounter.Pinger;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.EnterReconfigurationS2CPacket;
import net.minecraft.network.packet.s2c.query.PingResultS2CPacket;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class PingMixin {
    @Inject(at = @At("HEAD"), method="onPingResult")
    public void onPingResult(PingResultS2CPacket packet, CallbackInfo ci){
        Pinger.ping = Util.getMeasuringTimeMs() - packet.startTime();
    }
    @Inject(at = @At("HEAD"), method="onEnterReconfiguration")
    public void onReconfigure(EnterReconfigurationS2CPacket packet, CallbackInfo ci){
        Pinger.stopPinger();
        Pinger.sender = null;
    }
}
