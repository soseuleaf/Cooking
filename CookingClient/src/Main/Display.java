package Main;

import Component.DTO.ImageRenderData;
import Component.DTO.KeyEventData;
import Component.DTO.RenderData;
import Component.DTO.StringRenderData;
import Component.Render.AssetLoader;
import Component.Static.Config;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.InputStream;
import java.net.Socket;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;
import java.util.Vector;

public class Display {
    private final Vector<RenderData> renderDataVector = new Vector<>();
    private JFrame frame;
    private Canvas canvas;
    private Font font;
    private KeyManager keyManager;
    private final int randomNum;
    private final CookTogether cookTogether;

    public Display(CookTogether cookTogether) {
        this.cookTogether = cookTogether;
        randomNum = new Random().nextInt(100);

        frame = new JFrame(Config.Title + " | " + randomNum);
        frame.setSize(Config.DisplayWidth, Config.DisplayHeight + 40);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setFocusable(true);

        try {
            InputStream inputStream = AssetLoader.loadText("/DNFBitBitTTF.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        createLoginDisplay();
    }

    private void createLoginDisplay() {
        Container container = frame.getContentPane();
        container.setLayout(null);

        JLabel hintLabel = new JLabel("");
        hintLabel.setSize(250, 30);
        hintLabel.setLocation(500, 450);
        hintLabel.setFont(font.deriveFont(Font.PLAIN, 18f));
        hintLabel.setForeground(Color.GRAY);
        hintLabel.setHorizontalAlignment(JTextField.CENTER);
        container.add(hintLabel);

        JLabel ipLabel = new JLabel("아이피");
        ipLabel.setSize(50, 30);
        ipLabel.setLocation(500, 500);
        ipLabel.setFont(font.deriveFont(Font.PLAIN, 18f));
        container.add(ipLabel);

        JTextField ipField = new JTextField("127.0.0.1");
        ipField.setSize(200, 30);
        ipField.setLocation(550, 500);
        ipField.setFont(font.deriveFont(Font.PLAIN, 18f));
        ipField.setHorizontalAlignment(JTextField.CENTER);
        container.add(ipField);

        JLabel nickLabel = new JLabel("닉네임");
        nickLabel.setSize(50, 30);
        nickLabel.setLocation(500, 550);
        nickLabel.setFont(font.deriveFont(Font.PLAIN, 18f));
        container.add(nickLabel);

        JTextField nickField = new JTextField("유저" + randomNum);
        nickField.setSize(200, 30);
        nickField.setLocation(550, 550);
        nickField.setFont(font.deriveFont(Font.PLAIN, 18f));
        nickField.setHorizontalAlignment(JTextField.CENTER);
        container.add(nickField);

        JButton enterButton = new JButton("입장하기");
        enterButton.setSize(250, 50);
        enterButton.setLocation(500, 600);
        enterButton.setFont(font.deriveFont(Font.PLAIN, 18f));
        enterButton.setBorder(new LineBorder(Color.BLACK));
        enterButton.setBackground(Color.WHITE);
        enterButton.addActionListener(e -> {
            if (availablePort(ipField.getText(), Config.PORT)) {
                createDisplay();
                cookTogether.enterMainGame(ipField.getText(), nickField.getText());
            } else {
                hintLabel.setText("서버가 닫혀 있습니다.");
            }
        });
        container.add(enterButton);

        JPanel titleImage = new JPanel() {
            final Image background = new ImageIcon(Objects.requireNonNull(Main.class.getResource("/textures/single/title.png"))).getImage();

            public void paint(Graphics g) {//그리는 함수
                g.drawImage(background, 0, 0, null);
            }
        };
        titleImage.setSize(Config.DisplayWidth, Config.DisplayHeight);
        titleImage.setLocation(0, 0);
        container.add(titleImage);
        frame.setVisible(true);
    }

    public boolean availablePort(String host, int port) {
        try {
            (new Socket(host, port)).close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void createDisplay() {
        frame.getContentPane().removeAll();

        keyManager = new KeyManager();
        frame.addKeyListener(keyManager);

        canvas = new Canvas();
        canvas.setFocusable(false);
        canvas.setLocation(0, 0);
        canvas.setSize(Config.DisplayWidth, Config.DisplayHeight);
        canvas.setMaximumSize(new Dimension(Config.DisplayWidth, Config.DisplayHeight));
        canvas.setFocusable(false);

        frame.add(canvas);
    }

    private void updateDisplay(Graphics graphics) {
        Collections.sort(renderDataVector);
        for (RenderData renderDataItem : renderDataVector) {
            if (renderDataItem instanceof ImageRenderData data) {
                graphics.drawImage(
                        data.getSprite(),
                        data.getX(),
                        data.getY(),
                        data.getWidth(),
                        data.getHeight(),
                        null
                );
            } else if (renderDataItem instanceof StringRenderData data) {
                graphics.setFont(font.deriveFont(Font.PLAIN, data.getFontSize()));
                graphics.drawString(
                        data.getMessage(),
                        data.getX(),
                        data.getY()
                );
            }
        }
        renderDataVector.clear();
        keyManager.keyEventUpdate();
    }

    public void render() {
        BufferStrategy bufferStrategy = canvas.getBufferStrategy();

        if (bufferStrategy == null) {
            canvas.createBufferStrategy(3);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();
        graphics.clearRect(0, 0, Config.DisplayWidth, Config.DisplayHeight);
        graphics.setFont(font.deriveFont(Font.PLAIN, 18f));
        updateDisplay(graphics);

        bufferStrategy.show();
        graphics.dispose();
    }

    public void addRenderData(RenderData renderData) {
        renderDataVector.addElement(renderData);
    }

    public KeyEventData getKeyEventData() {
        return keyManager.getKetEventData();
    }
}
