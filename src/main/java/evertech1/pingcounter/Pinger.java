package evertech1.pingcounter;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket;
import net.minecraft.util.Util;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

public class Pinger {
    public static boolean isPinging = false;
    public static boolean isConnected = false;
    public static long ping = 0;
    public static PacketSender sender;
    private static final Runnable pingerTask = new Runnable() {
        public void run() {
            if(sender != null && isConnected){
                sender.sendPacket(new QueryPingC2SPacket(Util.getMeasuringTimeMs()));
            }
        }
    };
    private final static ScheduledExecutorService pingerExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture<?> pinger;
    public static void startPinger(int interval){
        if(!isPinging) {
            isPinging = true;
            pinger = pingerExecutor.scheduleAtFixedRate(pingerTask, 3000, 2000, TimeUnit.MILLISECONDS);
        }

    }
    public static void stopPinger(){
        if(isPinging && pinger.state().equals(Future.State.RUNNING)) pinger.cancel(true);
        isPinging = false;
    }
}
