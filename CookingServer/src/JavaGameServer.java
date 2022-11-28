//JavaObjServer.java ObjectStream 기반 채팅 Server

import Component.Order;
import Component.Packet.*;
import Component.Type.FoodType;
import Component.Type.StateType;
import Component.Type.UserStatus;
import lombok.Getter;

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

    /* 서버 관련 */
    private ServerSocket socket; // 서버소켓
    private Socket client_socket; // accept() 에서 생성된 client 소켓
    private final ArrayList<UserService> userServices = new ArrayList<>(); // 연결된 사용자를 저장할 벡터


    /* 게임 서비스 관련 */
    private StateType gameState = StateType.WAIT;
    private double time = 6000;
    private int score = 1;
    private final ArrayList<Order> orders = new ArrayList<>();

    private final FoodType[] OrderFoodTypes = {
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

            startOrderTimer();
            startStateTimer();
        });

        JButton mapChange = new JButton("맵 변경");
        mapChange.addActionListener(e -> {
            if (gameState == StateType.GAME) {
                gameState = StateType.WAIT;
            } else {
                gameState = StateType.GAME;
            }
        });

        btnServerStart.setBounds(12, 356, 150, 35);
        mapChange.setBounds(162, 356, 150, 35);

        contentPane.add(btnServerStart);
        contentPane.add(mapChange);
    }

    public void startOrderTimer() {
        //주문을 수시로 전송
        Timer m = new Timer();
        TimerTask order = new TimerTask() {
            @Override
            public void run() {
                UUID uuid = UUID.randomUUID();
                FoodType needFood = OrderFoodTypes[new Random().nextInt(OrderFoodTypes.length)];
                Order order = Order.NewOrder(uuid, needFood);
                sendOrder(uuid, needFood);
                AppendText("new order! " + order);
                synchronized (orders) {
                    orders.add(order);
                }
            }
        };
        m.schedule(order, 0, 10000);
    }

    public void startStateTimer() {
        // 수시로 상태 동기화
        Timer m = new Timer();
        TimerTask stateUpdate = new TimerTask() {
            @Override
            public void run() {
                time--;
                score++;

                synchronized (orders) {
                    Iterator<Order> iter = orders.iterator();
                    while (iter.hasNext()) {
                        Order order = iter.next();
                        order.updateTime();
                        System.out.println("order: " + order);
                        if (order.isExpirationOrder()) {
                            score -= 1000;
                            sendRemoveOrder(order.getUuid());
                            iter.remove();
                        }
                    }
                }

                System.out.println("-------------------------");
                sendState();
            }
        };
        m.schedule(stateUpdate, 0, 1000);
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
        StatePacket statePacket = new StatePacket(gameState, time, score);
        sendToAll(statePacket);
    }

    public void sendToOther(UUID myUuid, Object ob) {
        synchronized (userServices) {
            for (UserService user : userServices) {
                if (user.canSendPacket(myUuid)) {
                    user.sendObject(ob);
                }
            }
        }
    }

    public void sendToAll(Object object) {
        synchronized (userServices) {
            for (UserService user : userServices) {
                if (user.canSendPacket(null)) {
                    user.sendObject(object);
                }
            }
        }
    }

    // 새로운 참가자 accept() 하고 user thread를 새로 생성한다.
    class AcceptServer extends Thread {
        public void run() {
            while (true) { // 사용자 접속을 계속해서 받기 위해 while문
                try {
                    AppendText("Waiting new clients ...");
                    client_socket = socket.accept(); // accept가 일어나기 전까지는 무한 대기중
                    AppendText("새로운 참가자 from " + client_socket);
                    // User 당 하나씩 Thread 생성
                    UserService new_user = new UserService(client_socket);
                    userServices.add(new_user); // 새로운 참가자 배열에 추가
                    new_user.start(); // 만든 객체의 스레드 실행
                    AppendText("현재 참가자 수 " + userServices.size());
                } catch (IOException e) {
                    AppendText("accept() error");
                    System.exit(0);
                }
            }
        }
    }

    // User 당 생성되는 Thread
    class UserService extends Thread {
        // 소켓 관련
        private ObjectInputStream ois;
        private ObjectOutputStream oos;
        private Socket client_socket;

        // 게임 관련
        private UUID uuid;
        private UserStatus status = UserStatus.OFFLINE;
        @Getter
        private ConnectPacket loginPacket;

        public UserService(Socket client_socket) {
            // 매개변수로 넘어온 자료 저장
            this.client_socket = client_socket;
            try {
                oos = new ObjectOutputStream(client_socket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(client_socket.getInputStream());
            } catch (Exception e) {
                AppendText("userService error");
            }
        }

        public void registerUser(String name, int x, int y) {
            uuid = UUID.randomUUID();
            status = UserStatus.ONLINE;
            AppendText("새로운 참가자 " + uuid + " 입장.");

            // 나에게 uuid 등록을 요청하는 패킷을 주고
            sendObject(new ConnectPacket(uuid, 100, name, x, y));

            // 보내줄 패킷을 저장
            loginPacket = new ConnectPacket(uuid, 150, name, x, y);
        }

        public void login() {
            for (UserService user : userServices) {
                if (canSendPacket(user.uuid)) {
                    sendObject(user.getLoginPacket());
                    user.sendObject(loginPacket);
                }
            }
        }

        public void logout() {
            ConnectPacket logout = new ConnectPacket(uuid, 200, null, 0, 0);
            sendToOther(uuid, logout);
            userServices.remove(this);// Logout한 현재 객체를 벡터에서 지운다
        }

        public boolean canSendPacket(UUID otherUuid) {
            return this.status == UserStatus.ONLINE && !(this.uuid.equals(otherUuid));
        }

        public void sendObject(Object ob) {
            try {
                synchronized (this) {
                    oos.writeUnshared(ob);
                }
            } catch (IOException e) {
                AppendText("oos.writeObject(ob) error");
                try {
                    ois.close();
                    oos.close();
                    client_socket.close();
                    client_socket = null;
                    ois = null;
                    oos = null;
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
                logout();
            }
        }

        public void processPacket(Object object) {
            if (object instanceof ConnectPacket packet) {
                switch (packet.getCode()) {
                    case 100 -> {
                        registerUser(packet.getName(), packet.getX(), packet.getY());
                        login();
                    }
                    case 200 -> logout();
                    default -> System.out.println("Unknown Packet");
                }
            } else if (object instanceof UserPacket packet) {
                sendToOther(uuid, packet);
            } else if (object instanceof BlockPacket packet) {
                sendToOther(uuid, packet);
            } else if (object instanceof EventPacket packet) {
                synchronized (orders) {
                    for (Order order : orders) {
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
            } else {
                System.out.println("Unknown Packet");
            }
        }

        public void run() {
            Object obcm;
            while (true) {
                try {
                    if (socket == null) break;
                    try {
                        obcm = ois.readUnshared();
                        if (obcm == null) break;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
                    processPacket(obcm);
                } catch (IOException e) {
                    AppendText("ois.readObject() error");
                    try {
                        ois.close();
                        oos.close();
                        client_socket.close();
                        logout(); // 에러가난 현재 객체를 벡터에서 지운다
                        break;
                    } catch (Exception ee) {
                        break;
                    }
                }
            }
        }
    }
}
