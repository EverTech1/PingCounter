package EverTech1.pingcounter;

import net.minecraft.Util;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public class Pinger {
    public static boolean isPinging = false;
    public static long latency = 0;
    public static Connection connection;
    private static final Runnable pingerTask = new Runnable() {
        public void run() {
            if(connection != null){
                if(isPinging) connection.send(new ServerboundPingRequestPacket(Util.getMillis()));
            }
        }
    };
    private final static ScheduledExecutorService pingerExecutor = newSingleThreadScheduledExecutor();
    private static ScheduledFuture<?> pinger;
    public static void startPinging(int interval){
        if(!isPinging) {
            isPinging = true;
            if(pinger != null && pinger.state() == Future.State.RUNNING) pinger.cancel(true);
            pinger = pingerExecutor.scheduleAtFixedRate(pingerTask, 3000, interval, TimeUnit.MILLISECONDS);
        }

    }
    public static void stopPinging(){
        if(isPinging && pinger.state().equals(Future.State.RUNNING)) pinger.cancel(true);
        isPinging = false;
    }
}