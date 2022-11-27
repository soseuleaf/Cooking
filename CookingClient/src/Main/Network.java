package Main;

import Component.Packet.*;
import Component.Type.BlockType;
import Component.Type.FoodType;
import Component.Type.WorkState;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.UUID;

public class Network implements Serializable {
    private static final UUID uuid = UUID.randomUUID();
    private final CookTogether cookTogether;
    private Socket socket; // 연결소켓
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private String username;

    public Network(CookTogether cookTogether, String username, String ip_addr, String port_no) {
        this.cookTogether = cookTogether;
        try {
            this.username = username;

            socket = new Socket(ip_addr, Integer.parseInt(port_no));
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.flush();
            ois = new ObjectInputStream(socket.getInputStream());

            ListenThread listenThread = new ListenThread();
            listenThread.start();

            sendConnectPacket(100);
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }

    }

    class ListenThread extends Thread {
        public void run() {
            Object obcm = null;
            String msg;

            while (true) {
                try {
                    obcm = ois.readUnshared();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                    System.exit(0);
                }
                if (obcm == null) {
                    break;
                }

                //msg = String.format("[%s] %s", packet.uuid.toString(), packet.data);
                //System.out.println(msg);

                if (obcm instanceof ConnectPacket packet) {
                    cookTogether.recvConnectPacket(packet);
                } else if (obcm instanceof UserPacket packet) {
                    cookTogether.recvUserPacket(packet);
                } else if (obcm instanceof BlockPacket packet) {
                    cookTogether.recvBlockPacket(packet);
                } else if (obcm instanceof EventPacket packet) {
                    cookTogether.recvEventPacket(packet);
                } else if (obcm instanceof StatePacket packet) {
                    cookTogether.recvStatePacket(packet);
                } else {
                    System.out.println("Unknown Packet");
                }
            }
        }
    }

    public void sendConnectPacket(int code) {
        sendObject(new ConnectPacket(uuid, code, "유저", 100, 100));
    }

    public void sendUserPacket(int x, int y, FoodType foodType) {
        sendObject(new UserPacket(uuid, x, y, foodType));
    }

    public void sendBlockPacket(BlockType blockType, int y, int x, FoodType[] foodType, WorkState workState, double progress) {
        sendObject(new BlockPacket(blockType, y, x, foodType, workState, progress));
    }

    public void sendEventPacket(int code, UUID uuid, FoodType foodType) {
        sendObject(new EventPacket(code, uuid, foodType));
    }

    public void sendObject(Object ob) { // 서버로 메세지를 보내는 메소드
        try {
            oos.writeUnshared(ob);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
