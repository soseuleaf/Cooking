//JavaObjServer.java ObjectStream 기반 채팅 Server

import Component.Order;
import Component.Packet.*;
import Component.Type.FoodType;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serial;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Timer;
import java.util.*;

public class JavaGameServer extends JFrame {
    @Serial
    private static final long serialVersionUID = 1L;
    private final JTextArea textArea;
    private final JPanel contentPane;
    private final JTextField txtPortNumber;
    private ServerSocket socket; // 서버소켓
    private Socket client_socket; // accept() 에서 생성된 client 소켓
    private final Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터


    /* 게임 서비스 관련 */
    private double time = 6000;
    private int score = 1;
    private final ArrayList<Order> orderArrayList = new ArrayList<>();

    public JavaGameServer() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 338, 440);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(12, 10, 300, 298);
        contentPane.add(scrollPane);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        JLabel lblNewLabel = new JLabel("Port Number");
        lblNewLabel.setBounds(13, 318, 87, 26);
        contentPane.add(lblNewLabel);

        txtPortNumber = new JTextField();
        txtPortNumber.setHorizontalAlignment(SwingConstants.CENTER);
        txtPortNumber.setText("30000");
        txtPortNumber.setBounds(112, 318, 199, 26);
        contentPane.add(txtPortNumber);
        txtPortNumber.setColumns(10);

        JButton btnServerStart = new JButton("Server Start");
        btnServerStart.addActionListener(e -> {
            try {
                socket = new ServerSocket(Integer.parseInt(txtPortNumber.getText()));
            } catch (NumberFormatException | IOException e1) {
                e1.printStackTrace();
            }
            AppendText("Chat Server Running..");
            btnServerStart.setText("Chat Server Running..");
            btnServerStart.setEnabled(false); // 서버를 더이상 실행시키지 못 하게 막는다
            txtPortNumber.setEnabled(false); // 더이상 포트번호 수정못 하게 막는다

            AcceptServer accept_server = new AcceptServer();
            accept_server.start();

            FoodType[] foodTypes = {
                    FoodType.COOKED_SALMON,
                    FoodType.COOKED_BACON,
                    FoodType.SLICED_BREAD,
                    FoodType.FRENCH_FRIES,
                    FoodType.STEAK,
                    FoodType.FRIED_MANDOO,
                    FoodType.FRIED_EGG,
                    FoodType.FRIED_CHICKEN,
                    FoodType.MUSHROOM_SOUP,
                    FoodType.TOMATO_SOUP,
                    FoodType.ONION_SOUP,
            };

            //주문을 수시로 전송
            Timer m = new Timer();
            TimerTask order = new TimerTask() {
                @Override
                public void run() {
                    UUID uuid = UUID.randomUUID();
                    FoodType needFood = foodTypes[new Random().nextInt(foodTypes.length)];
                    Order order = Order.NewOrder(uuid, needFood);
                    sendOrder(uuid, needFood);
                    AppendText("new order! " + order);
                    synchronized (this) {
                        orderArrayList.add(order);
                    }
                }
            };
            m.schedule(order, 0, 10000);

            // 수시로 상태 동기화
            Timer m2 = new Timer();
            TimerTask orderUpdate = new TimerTask() {
                @Override
                public void run() {
                    time--;
                    score++;
                    synchronized (this) {
                        for (Iterator<Order> i = orderArrayList.iterator(); i.hasNext(); ) {
                            Order order = i.next();
                            order.updateTime();
                            System.out.println("order: " + order);
                            if (order.isExpirationOrder()) {
                                score -= 1000;
                                sendRemoveOrder(order.getUuid());
                                i.remove();
                            }
                        }
                    }
                    System.out.println("-------------------------");
                    sendState();
                }
            };
            m2.schedule(orderUpdate, 0, 1000);
        });
        btnServerStart.setBounds(12, 356, 150, 35);
        contentPane.add(btnServerStart);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                JavaGameServer frame = new JavaGameServer();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void AppendText(String str) {
        textArea.append(str + "\n");
        textArea.setCaretPosition(textArea.getText().length());
    }

    public void sendOrder(UUID uuid, FoodType foodType) {
        EventPacket eventPacket = new EventPacket(10, uuid, foodType);
        sendToAll(eventPacket);
    }

    public void sendRemoveOrder(UUID uuid) {
        EventPacket eventPacket = new EventPacket(30, uuid, null);
        sendToAll(eventPacket);
    }

    public void sendState() {
        StatePacket statePacket = new StatePacket(time, score);
        sendToAll(statePacket);
    }

    public void sendToAll(Object object) {
        synchronized (this) {
            for (int i = 0; i < UserVec.size(); i++) {
                UserService user = (UserService) UserVec.elementAt(i);
                if (Objects.equals(user.UserStatus, "O")) user.WriteOneObject(object);
            }
        }
    }

//    public void AppendObject(FoodPacket packet) {
//        textArea.append("code = " + packet.code + "id = " + packet.uuid.toString() + " x: " + packet.x + " y: " + packet.y);
//        textArea.append("\n");
//        textArea.setCaretPosition(textArea.getText().length());
//    }

    // 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
    class AcceptServer extends Thread {
        @SuppressWarnings("unchecked")
        public void run() {
            while (true) { // 사용자 접속을 계속해서 받기 위해 while문
                try {
                    AppendText("Waiting new clients ...");
                    client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
                    AppendText("새로운 참가자 from " + client_socket);
                    // User 당 하나씩 Thread 생성
                    UserService new_user = new UserService(client_socket);
                    UserVec.add(new_user); // 새로운 참가자 배열에 추가
                    new_user.start(); // 만든 객체의 스레드 실행
                    AppendText("현재 참가자 수 " + UserVec.size());
                } catch (IOException e) {
                    AppendText("accept() error");
                    // System.exit(0);
                }
            }
        }
    }

    // User 당 생성되는 Thread
    class UserService extends Thread {
        public UUID uuid;
        public String UserStatus;
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private Socket client_socket;
        private final Vector user_vc;

        public UserService(Socket client_socket) {
            // TODO Auto-generated constructor stub
            // 매개변수로 넘어온 자료 저장
            this.client_socket = client_socket;
            this.user_vc = UserVec;
            try {
                oos = new ObjectOutputStream(client_socket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(client_socket.getInputStream());
            } catch (Exception e) {
                AppendText("userService error");
            }
        }

        public void Login() {
            AppendText("새로운 참가자 " + uuid.toString() + " 입장.");
        }

        public void Logout() {
            //String msg = "[" + UserName + "]님이 퇴장 하였습니다.\n";
            UserVec.removeElement(this); // Logout한 현재 객체를 벡터에서 지운다
            //WriteAll(msg); // 나를 제외한 다른 User들에게 전송
            //AppendText("사용자 " + "[" + UserName + "] 퇴장. 현재 참가자 수 " + UserVec.size());
        }

        public void WriteOtherObject(Object ob) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (Objects.equals(user.UserStatus, "O") && user != this) {
                    user.WriteOneObject(ob);
                }
            }
        }

        public void WriteAllObject(Object ob) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (Objects.equals(user.UserStatus, "O")) {
                    user.WriteOneObject(ob);
                }
            }
        }

        public void WriteOneObject(Object ob) {
            try {
                oos.writeObject(ob);
            } catch (IOException e) {
                AppendText("oos.writeObject(ob) error");
                try {
                    ois.close();
                    oos.close();
                    client_socket.close();
                    client_socket = null;
                    ois = null;
                    oos = null;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                Logout();
            }
        }

        public void run() {
            while (true) {
                try {
                    // 선언 및 객체 읽고 에러 처리
                    Object obcm;
                    if (socket == null) break;
                    try {
                        obcm = ois.readObject();
                        if (obcm == null) break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }

                    // 패킷 판단하고 전달
                    if (obcm instanceof ConnectPacket packet) {
                        switch (packet.getCode()) {
                            case 100 -> {
                                uuid = packet.getUuid();
                                UserStatus = "O"; // Online 상태
                                WriteOtherObject(packet);
                                Login();
                            }
                            case 200 -> WriteOtherObject(packet);
                            default -> System.out.println("Unknown Packet");
                        }
                    } else if (obcm instanceof UserPacket packet) {
                        WriteOtherObject(packet);
                    } else if (obcm instanceof BlockPacket packet) {
                        WriteOtherObject(packet);
                    } else if (obcm instanceof EventPacket packet) {
                        synchronized (this) {
                            for (Order order : orderArrayList) {
                                if (packet.getOrderUuid().equals(order.getUuid())) {
                                    if (order.getFoodType() == order.getFoodType()) {
                                        score += 1000;
                                    } else {
                                        score -= 1000;
                                    }
                                    order.updateTime(999);
                                    break;
                                }
                            }
                        }
                        sendRemoveOrder(packet.getOrderUuid());
                    } else {
                        System.out.println("Unknown Packet");
                    }
                } catch (IOException e) {
                    AppendText("ois.readObject() error");
                    try {
                        ois.close();
                        oos.close();
                        client_socket.close();
                        Logout(); // 에러가난 현재 객체를 벡터에서 지운다
                        break;
                    } catch (Exception ee) {
                        break;
                    }
                }
            }
        }
    }
}
