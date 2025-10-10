package EverTech1.pingcounter.Mixins;

import EverTech1.pingcounter.Main;
import EverTech1.pingcounter.Pinger;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundStartConfigurationPacket;
import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class PingMixin{

    @Inject(method="handlePongResponse", at=@At("HEAD"))
    public void handlePongResponse(ClientboundPongResponsePacket pPacket, CallbackInfo ci) {
        Pinger.latency = Util.getMillis()-pPacket.time();

    }

    @Inject(method="handleConfigurationStart", at=@At("HEAD"))
    public void configurationStart(ClientboundStartConfigurationPacket packet, CallbackInfo ci){
        Pinger.stopPinging();
    }
}

