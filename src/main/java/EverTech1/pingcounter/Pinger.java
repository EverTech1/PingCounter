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
    public void startPinging(int interval){
        if(!Minecraft.getInstance().isLocalServer()&&!isPinging&&connection!=null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(isPinging) connection.send(new ServerboundPingRequestPacket(Util.getMillis()));
                }
            }, 3000, interval);
            isPinging = true;
        }
    }
    public void stopPinging(){
        if(isPinging){
            isPinging = false;
            timer.cancel();
        }
    }
}
