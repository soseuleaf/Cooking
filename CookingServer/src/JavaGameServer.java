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
    private final JList<Object> userList;
    private final JList<Object> orderList;

    /* 서버 관련 */
    private ServerSocket socket; // 서버소켓
    private Socket client_socket; // accept() 에서 생성된 client 소켓
    private final ArrayList<UserService> userServices = new ArrayList<>(); // 연결된 사용자를 저장할 벡터

    /* 게임 서비스 관련 */
    private final String[] playerIndex = {"X", "X", "X", "X", "X"};
    private StateType gameState = StateType.WAIT;
    private double timeMax = 243;
    private double time = 243;
    private int score = 0;
    private final ArrayList<Order> orders = new ArrayList<>();
    private long lastTime;
    private int successFood = 0;
    private int failedFood = 0;
    private double averageTime = 0;


    /* 커스텀 옵션 */
    private int customOrderTime = 120;
    private int customOrderAddTime = 10000;
    private JTextField orderTimeField;
    private JTextField orderAddTimeField;

    /* Timer Task*/
    private final Timer orderTimer = new Timer();
    private TimerTask orderTask;
    private final Timer stateTimer = new Timer();
    private TimerTask stateTask;

    private boolean taskStarted = false;

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
        setBounds(100, 100, 800, 500);
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
        txtPortNumber.setText("42021");
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

            newStateTask();
            stateTimer.schedule(stateTask, 0, 1000);
        });

        JButton mapChange = new JButton("맵 변경");
        mapChange.addActionListener(e -> {
            if (gameState == StateType.WAIT) {
                startGame();
            } else if (gameState == StateType.GAME) {
                endGame();
            } else if (gameState == StateType.END) {
                waitGame();
            }
            sendState();
        });

        btnServerStart.setBounds(12, 356, 150, 35);
        mapChange.setBounds(162, 356, 150, 35);

        userList = new JList<>(
                userServices.stream().map(UserService::toString).toArray()
        );
        userList.setBounds(312, 12, 200, 500);

        orderList = new JList<>(
                orders.stream().map(Order::toString).toArray()
        );
        orderList.setBounds(512, 12, 300, 500);

        JLabel orderTimeLabel = new JLabel("주문 만료 시간");
        orderTimeLabel.setBounds(13, 400, 150, 20);

        orderTimeField = new JTextField(Integer.toString(customOrderTime));
        orderTimeField.setBounds(13, 420, 150, 30);
        orderTimeField.addActionListener(
                e -> {
                    try {
                        customOrderTime = Integer.parseInt(orderTimeField.getText());
                    } catch (Exception ignored) {

                    }
                }
        );

        JLabel orderAddTimeLabel = new JLabel("주문 추가 시간");
        orderAddTimeLabel.setBounds(163, 400, 150, 20);

        orderAddTimeField = new JTextField(Integer.toString(customOrderAddTime));
        orderAddTimeField.setBounds(163, 420, 150, 30);
        orderAddTimeField.addActionListener(
                e -> {
                    try {
                        customOrderAddTime = Integer.parseInt(orderAddTimeField.getText());
                    } catch (Exception ignored) {

                    }
                }
        );

        contentPane.add(orderTimeLabel);
        contentPane.add(orderTimeField);
        contentPane.add(orderAddTimeLabel);
        contentPane.add(orderAddTimeField);
        contentPane.add(userList);
        contentPane.add(orderList);
        contentPane.add(btnServerStart);
        contentPane.add(mapChange);
    }

    public void newOrderTask() {
        //주문을 수시로 전송
        orderTask = new TimerTask() {
            @Override
            public void run() {
                if (gameState == StateType.GAME && time > 30) {
                    UUID uuid = UUID.randomUUID();
                    FoodType needFood = OrderFoodTypes[new Random().nextInt(OrderFoodTypes.length)];
                    Order order = Order.NewOrder(uuid, needFood, customOrderTime);
                    sendOrder(uuid, needFood);
                    synchronized (orders) {
                        orders.add(order);
                    }
                }
            }
        };
    }

    public void newStateTask() {
        // 수시로 상태 동기화
        stateTask = new TimerTask() {
            @Override
            public void run() {
                AppendText("만료 시간: " + customOrderTime + " 오더 딜레이: " + customOrderAddTime);
                if (gameState == StateType.GAME) {
                    synchronized (orders) {
                        Iterator<Order> iter = orders.iterator();
                        while (iter.hasNext()) {
                            Order order = iter.next();
                            order.updateTime();
                            System.out.println("order: " + order);
                            if (order.isExpirationOrder()) {
                                // 여유생기면 로직 변경...
                                if (order.getNowTime() < -100) {
                                    score += 1000;
                                    successFood++;
                                    sendRemoveOrder(order.getUuid(), true);
                                } else {
                                    score -= 1000;
                                    failedFood++;
                                    sendRemoveOrder(order.getUuid(), false);
                                }
                                iter.remove();
                            }
                        }
                    }

                    time--;
                    score++;

                    if (time < 0) {
                        endGame();
                    }
                    System.out.println(score);
                } else if (gameState == StateType.WAIT) {
                    int isReadyCount = (int) userServices.stream().filter(UserService::isReady).count();
                    int userCount = userServices.size();
                    if (isReadyCount == userCount && userCount > 0) {
                        startGame();
                    }
                } else if (gameState == StateType.END) {
                    double second = 1_000_000_000.0;
                    System.out.println(lastTime / second);
                    System.out.println(System.nanoTime() / second);
                    if ((System.nanoTime()) / second > (lastTime / second) + 5) {
                        waitGame();
                    }
                }

                userList.setListData(
                        userServices.stream().map(UserService::toString).toArray()
                );

                orderList.setListData(
                        orders.stream().map(Order::toString).toArray()
                );

                sendState();
            }
        };
    }

    public void waitGame() {
        gameState = StateType.WAIT;
        time = timeMax;
    }

    public void startGame() {
        gameState = StateType.GAME;
        userServices.forEach(userService -> userService.setStatus(UserStatus.GAME));

        if (taskStarted) {
            orderTask.cancel();
        }
        newOrderTask();
        orderTimer.schedule(orderTask, 4000, customOrderAddTime);

        score = 0;
        successFood = 0;
        failedFood = 0;
        averageTime = 0;
        taskStarted = true;
    }

    public void endGame() {
        lastTime = System.nanoTime();
        gameState = StateType.END;
        userServices.forEach(userService -> userService.setStatus(UserStatus.ONLINE));
        score -= orders.size() * 1000;
        orders.clear();
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
        EventPacket eventPacket = new EventPacket(10, uuid, foodType, customOrderTime);
        sendToAll(eventPacket);
    }

    public void sendRemoveOrder(UUID uuid, boolean isSuccess) {
        int code = isSuccess ? 30 : 40;
        EventPacket eventPacket = new EventPacket(code, uuid, null, 0);
        sendToAll(eventPacket);
    }

    public void sendState() {
        StatePacket statePacket = new StatePacket(gameState, time, successFood, failedFood, (int) (averageTime / successFood), score, false);
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
                if (object instanceof StatePacket packet && user.status == UserStatus.WAIT) {
                    packet.setWatch(true);
                    user.sendObject(packet);
                } else if (user.canSendPacket(null)) {
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
        private boolean threadflag = true;

        // 게임 관련
        private UUID uuid;
        private int index;
        private String name;
        private UserStatus status = UserStatus.OFFLINE;
        private FoodType holdFoodType = null;
        @Getter
        private ConnectPacket loginPacket;

        public UserService(Socket client_socket) {
            // 매개변수로 넘어온 자료 저장
            this.client_socket = client_socket;
            try {
                oos = new ObjectOutputStream(client_socket.getOutputStream());
                oos.flush();
                ois = new ObjectInputStream(client_socket.getInputStream());
                sendState();
            } catch (Exception e) {
                AppendText("userService error");
            }
        }

        public void registerUser(String name, int x, int y) {
            this.uuid = UUID.randomUUID();
            this.index = Arrays.asList(playerIndex).indexOf("X");
            this.name = name;

            playerIndex[index] = "O";
            playerIndex[4] = "X";

            status = gameState == StateType.GAME ? UserStatus.WAIT : UserStatus.ONLINE;
            AppendText("새로운 참가자 " + uuid + " 입장.");

            // 나에게 uuid 등록을 요청하는 패킷을 주고
            sendObject(new ConnectPacket(100, uuid, index, name, x, y));

            // 보내줄 패킷을 저장
            loginPacket = new ConnectPacket(150, uuid, index, name, x, y);
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
            playerIndex[index] = "X";
            ConnectPacket logout = new ConnectPacket(200, uuid, 0, null, 0, 0);
            sendToOther(uuid, logout);
            threadflag = false;
        }

        public void setStatus(UserStatus status) {
            this.status = status;
        }

        public boolean canSendPacket(UUID otherUuid) {
            return this.status != UserStatus.OFFLINE && !(this.uuid.equals(otherUuid));
        }

        public boolean isReady() {
            return holdFoodType == FoodType.READY;
        }

        public void sendObject(Object ob) {
            try {
                synchronized (this) {
                    oos.writeUnshared(ob);
                }
            } catch (IOException e) {
                AppendText("oos.writeObject(ob) error");
                try {
                    userServices.remove(this);
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
                        registerUser(packet.getName(), -100, -100);
                        login();
                    }
                    case 200 -> logout();
                    default -> System.out.println("Unknown Packet");
                }
            }

            if (status == UserStatus.WAIT) return;

            if (object instanceof UserPacket packet) {
                holdFoodType = packet.getFoodType();
                sendToOther(uuid, packet);
            } else if (object instanceof BlockPacket packet) {
                sendToOther(uuid, packet);
            } else if (object instanceof EventPacket packet) {
                synchronized (orders) {
                    Order findOrder = orders.stream()
                            .filter(order -> packet.getOrderUuid().equals(order.getUuid()))
                            .findAny()
                            .orElse(null);

                    assert findOrder != null;
                    averageTime += findOrder.getMaxTime() - findOrder.getNowTime();
                    findOrder.updateTime(9999);
                }
            } else {
                System.out.println("Unknown Packet");
            }
        }

        @Override
        public String toString() {
            return "[" + index + "] " + name + " | " + status;
        }

        public void run() {
            Object obcm;
            while (threadflag) {
                try {
                    if (ois == null || socket == null) {
                        userServices.remove(this);
                        threadflag = false;
                        break;
                    }

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
                    userServices.remove(this);
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
