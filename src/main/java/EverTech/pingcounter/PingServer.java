package EverTech.pingcounter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.status.INetHandlerStatusClient;
import net.minecraft.network.status.client.C00PacketServerQuery;
import net.minecraft.network.status.client.C01PacketPing;
import net.minecraft.network.status.server.S00PacketServerInfo;
import net.minecraft.network.status.server.S01PacketPong;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SideOnly(Side.CLIENT)
public class PingServer {
    //Minecraft's server pinger without the unnecessary stuff
    PingServer(ServerData server){
        try {
            sendPing(server);
        }
        catch(Throwable err){
            err.printStackTrace();
        }
    }
    private void sendPing(ServerData server) throws UnknownHostException{
        ServerAddress address = ServerAddress.fromString(server.serverIP);
        final NetworkManager networkmanager = NetworkManager.createNetworkManagerAndConnect(InetAddress.getByName(address.getIP()), address.getPort(), false);
        networkmanager.setNetHandler(new INetHandlerStatusClient() {
            private long sendTime;
            private boolean someBool = false;
            public void handleServerInfo(S00PacketServerInfo packetIn) {
                if (someBool) {
                    networkmanager.closeChannel(new ChatComponentText("Received unrequested status"));
                } else {
                    someBool = true;
                    sendTime = Minecraft.getSystemTime();
                    networkmanager.sendPacket(new C01PacketPing(sendTime));
                }
            }
            public void handlePong(S01PacketPong packetIn) {
                long i = sendTime;
                long j = Minecraft.getSystemTime();
                Events.latency = j - i;
                networkmanager.closeChannel(new ChatComponentText(""));
            }
            @Override
            public void onDisconnect(IChatComponent iChatComponent) {
            }
        });
        try {
            networkmanager.sendPacket(new C00Handshake(47, address.getIP(), address.getPort(), EnumConnectionState.STATUS));
            networkmanager.sendPacket(new C00PacketServerQuery());
        } catch (Throwable var5) {
            //IDC
        }
        if(networkmanager.isChannelOpen()){
            networkmanager.processReceivedPackets();
        }
        else {
            networkmanager.checkDisconnected();
        }
    }
}
