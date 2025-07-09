package EverTech1.pingcounter;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;

import java.util.Timer;
import java.util.TimerTask;

public class Pinger {
    public static long latency;
    public static boolean isPinging = false;
    private static Timer timer;
    public static Connection connection;
    Minecraft mc;
    public Pinger(){
        mc = Minecraft.getInstance();
    }
    public void startPinging(int interval){
        if(!mc.isLocalServer()&&!isPinging) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(connection!=null) connection.send(new ServerboundPingRequestPacket(Util.getMillis()));
                }
            }, 3000, interval);
            isPinging = true;
        }
    }
    public void stopPinging(){
        if(isPinging){
            timer.cancel();
            isPinging = false;
        }
    }
}
