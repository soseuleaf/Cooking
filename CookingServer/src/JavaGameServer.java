//JavaObjServer.java ObjectStream 기반 채팅 Server

import Data.EventEnum;
import Data.EventPacket;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;
import java.util.UUID;
import java.util.Vector;

public class JavaGameServer extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final int BUF_LEN = 128; // Windows 처럼 BUF_LEN 을 정의
    JTextArea textArea;
    private final JPanel contentPane;
    private final JTextField txtPortNumber;
    private ServerSocket socket; // 서버소켓
    private Socket client_socket; // accept() 에서 생성된 client 소켓
    private final Vector UserVec = new Vector(); // 연결된 사용자를 저장할 벡터

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
        });
        btnServerStart.setBounds(12, 356, 300, 35);
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

    public void AppendObject(EventPacket msg) {
        textArea.append("code = " + msg.code + "id = " + msg.uuid.toString() + "data = " + msg.data + " x: " + msg.x + " y: " + msg.y);
        textArea.append("\n");
        textArea.setCaretPosition(textArea.getText().length());
    }

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

        public void WriteAllObject(Object ob) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (Objects.equals(user.UserStatus, "O"))
                    user.WriteOneObject(ob);
            }
        }

        public void WriteOtherObject(Object ob) {
            for (int i = 0; i < user_vc.size(); i++) {
                UserService user = (UserService) user_vc.elementAt(i);
                if (Objects.equals(user.UserStatus, "O") && user != this) {
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
                    Object obcm = null;
                    String msg = null;
                    EventPacket cm = null;
                    if (socket == null)
                        break;
                    try {
                        obcm = ois.readObject();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
                    if (obcm == null)
                        break;
                    if (obcm instanceof EventPacket) {
                        cm = (EventPacket) obcm;
                        AppendObject(cm);
                    } else
                        continue;
                    if (cm.code == EventEnum.CONNECT) {
                        uuid = cm.uuid;
                        UserStatus = "O"; // Online 상태
                        Login();
                    } else if (cm.code == EventEnum.MOVE) {
                        msg = String.format("[%s] %s", cm.uuid.toString(), cm.data);
                        WriteOtherObject(cm);
                    } else if (cm.code == EventEnum.ACTION) {
                        Logout();
                        break;
                    } else {
                        WriteAllObject(cm);
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
