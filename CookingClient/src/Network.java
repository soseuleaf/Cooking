import Data.EventEnum;
import Data.EventPacket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Network {
    private static final long serialVersionUID = 1L;
    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
    private CookTogether cookTogether;
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

            sendLoginPacket();

            ListenThread listenThread = new ListenThread();
            listenThread.start();
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }

    }

    class ListenThread extends Thread {
        public void run() {
            Object obcm;
            String msg;
            EventPacket cm;

            while (true) {
                try {
                    obcm = ois.readObject();
                } catch (ClassNotFoundException | IOException e) {
                    e.printStackTrace();
                    break;
                }

                if (obcm == null) {
                    continue;
                }

                if (obcm instanceof EventPacket) {
                    cm = (EventPacket) obcm;
                    cookTogether.recvEventPacket(cm);
                    msg = String.format("[%s]\n%s", cm.UserName, cm.data);
                    System.out.println(msg);
                } else {
                    System.out.println("Unknown Packet");
                }
            }
        }
    }

    public void sendLoginPacket() {
        sendObject(new EventPacket(EventEnum.CONNECT, username, "Hello",0, 0));
    }

    public void sendMovePacket(EventEnum code, String message, int x, int y){
        sendObject(new EventPacket(code, username, message, x, y));
    }

    public void sendObject(Object ob) { // 서버로 메세지를 보내는 메소드
        try {
            oos.writeObject(ob);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
