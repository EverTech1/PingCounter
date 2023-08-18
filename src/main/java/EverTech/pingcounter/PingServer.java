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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

@SideOnly(Side.CLIENT)
public class PingServer {
    PingServer(ServerData server){
        try {
            sendPing(server);
        }
        catch(Throwable err){
            err.printStackTrace();
        }
    }
    private void sendPing(ServerData server) throws IOException {
        ServerAddress address = ServerAddress.fromString(server.serverIP);
        Socket s = new Socket();
        s.setSoTimeout(5000);
        long timeBefore = System.currentTimeMillis();
        s.connect(new InetSocketAddress(address.getIP(), address.getPort()));
        s.close();
        Events.latency = System.currentTimeMillis() - timeBefore;
    }
}
