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
    Minecraft mc;
    public Pinger(){
        mc = Minecraft.getInstance();
    }
    public void startPinging(int interval){
        if(!isPinging){
            isPinging = true;
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(!mc.isLocalServer()){
                       ServerData server = Minecraft.getInstance().getCurrentServer();
                       if(server!=null){
                           sendPing(server);
                       }
                       else{
                           latency = 0;
                       }
                   }else{
                       latency = 0;
                   }
               }
            },
            0,
            interval
            );
        }
    }
    public void stopPinging(){
        if(isPinging){
            timer.cancel();
            isPinging = false;
        }
    }
    private void sendPing(ServerData serverData){
        ServerAddress address = ServerAddress.parseString(serverData.ip);
        Optional<InetSocketAddress> optional = ServerNameResolver.DEFAULT.resolveAddress(address).map(ResolvedServerAddress::asInetSocketAddress);
        if(optional.isPresent()){
            final InetSocketAddress inetsocketaddress = optional.get();
            final Connection connection = Connection.connectToServer(inetsocketaddress, false, null);

            ClientStatusPacketListener clientStatusPacketListener = new ClientStatusPacketListener() {
                private boolean receivedPing;
                private long pingStart;

                @Override
                public void handleStatusResponse(ClientboundStatusResponsePacket pPacket) {
                    if(this.receivedPing) connection.disconnect(Component.translatable("multiplayer.status.unrequested"));
                    else{
                        this.receivedPing = true;
                        this.pingStart = Util.getMillis();
                        connection.send(new ServerboundPingRequestPacket(this.pingStart));
                    }
                }

                @Override
                public void handlePongResponse(ClientboundPongResponsePacket pPacket) {
                    latency = Util.getMillis() - this.pingStart;
                    connection.disconnect(Component.translatable("multiplayer.status.finished"));

                }

                @Override
                public void onDisconnect(DisconnectionDetails pDetails) {

                }

                @Override
                public boolean isAcceptingMessages() {
                    return true;
                }
            };
            try {
                connection.initiateServerboundStatusConnection(address.getHost(), address.getPort(), clientStatusPacketListener);
                connection.send(ServerboundStatusRequestPacket.INSTANCE);
            } catch (Throwable ignored) {
            }
        }
    }
}
