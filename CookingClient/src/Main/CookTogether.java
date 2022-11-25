package Main;

import Component.DTO.RenderData;
import Component.Packet.*;
import Component.Static.Assets;
import Component.Static.Config;
import Component.Type.BlockType;
import Component.Type.FoodType;
import Component.Type.WorkState;

import java.util.UUID;

public class CookTogether implements Runnable {
    private Display display;
    private Network network;
    private boolean running;
    private PlayManager playManager;
    private UserManager userManager;

    public CookTogether() {
        Assets.init();
        this.init();
    }

    public void addRenderData(RenderData renderData) {
        display.addRenderData(renderData);
    }

    public void sendUserPacket(int x, int y, FoodType foodType) {
        network.sendUserPacket(x, y, foodType);
    }

    public void sendBlockPacket(BlockType blockType, int y, int x, FoodType[] foodTypes, WorkState workState, double progress) {
        network.sendBlockPacket(blockType, y, x, foodTypes, workState, progress);
    }

    public void sendEventPacket(UUID uuid, FoodType foodType) {
        network.sendEventPacket(20, uuid, foodType);
    }

    public void recvConnectPacket(ConnectPacket connectPacket) {
        userManager.recvConnectPacket(connectPacket);
    }

    public void recvUserPacket(UserPacket userPacket) {
        userManager.recvUserPacket(userPacket);
    }

    public void recvBlockPacket(BlockPacket blockPacket) {
        playManager.recvBlockPacket(blockPacket);
    }

    public void recvEventPacket(EventPacket eventPacket) {
        playManager.recvEventPacket(eventPacket);
    }

    public void recvStatePacket(StatePacket statePacket) {
        playManager.recvStatePacket(statePacket);
    }

    private void init() {
        this.display = new Display(this);
        this.playManager = new PlayManager(this);
        this.userManager = new UserManager(this);
        this.network = new Network(this, "Test", "127.0.0.1", "30000");
    }

    private void update() {
        // 키 데이터 전송 및 데이터 가공
        playManager.update(display.getKeyEventData());

        // 렌더링 데이터 추출
        playManager.updatePlayerRender();
        playManager.updateMapRender();
        playManager.updateUiRender();
        userManager.updateRender();

        // 렌더링 진행
        display.render();
    }

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / Config.FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (true) {
            try {
                currentTime = System.nanoTime();
                delta += (currentTime - lastTime) / drawInterval;
                lastTime = currentTime;
                if (delta >= 1) {
                    update();
                    delta--;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public void start() {
        if (!running) {
            running = true;
            Thread frameThread = new Thread(this);
            frameThread.start();
        }
    }
}