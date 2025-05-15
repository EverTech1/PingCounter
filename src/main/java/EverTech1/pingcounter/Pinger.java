package EverTech1.pingcounter;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
import net.minecraft.network.Connection;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

public class Pinger {
    public static long latency;
    public static boolean isPinging = false;
    private static Timer timer;
    private long pingSent;
    private boolean startPinging;
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
                    connection.send(new ServerboundPingRequestPacket(Util.getMillis()));
                }
            }, 0, interval);
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
