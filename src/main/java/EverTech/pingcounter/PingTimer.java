package EverTech.pingcounter;

import net.minecraft.client.Minecraft;

import java.util.TimerTask;

public class PingTimer extends TimerTask {
    private final Minecraft mc = Minecraft.getMinecraft();
    @Override
    public void run() {
        if (Minecraft.getMinecraft().theWorld != null) {
            try {
                if (mc.getIntegratedServer() == null) {
                    PingServer pinger = new PingServer(mc.getCurrentServerData());
                    if(Main.useOldPingMethod) pinger.sendPingOld();
                    else pinger.sendPing();
                }
                else{
                    Events.latency = 0;
                }
            } catch (Throwable err) {
                err.printStackTrace();
            }
        }
    }
}

