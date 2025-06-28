package evertech1.pingcounter;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.packet.c2s.query.QueryPingC2SPacket;
import net.minecraft.util.Util;

import java.util.Timer;
import java.util.TimerTask;

public class Pinger {
    public static boolean isPinging = false;
    public static boolean isConnected = false;
    public static long ping = 0;
    public static PacketSender sender;
    private static TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if(sender != null && isConnected){
                sender.sendPacket(new QueryPingC2SPacket(Util.getMeasuringTimeMs()));
            }
        }
    };
    private static final Timer timer = new Timer("pingTimer");
    public static void startPinger(int interval){
        if(!isPinging) timer.schedule(timerTask, 0, interval);
        isPinging = true;
    }
}
