package Main;

import Component.*;
import Component.Type.EventType;
import Component.Packet.EventPacket;
import Component.Packet.FoodPacket;
import Component.Static.Config;
import Component.Static.Assets;
import Component.DTO.RenderData;

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

    public void sendEventPacket(int x, int y) {
        network.sendMovePacket("Move", x, y);
    }

    public void sendFoodPacket(EventType code, int y, int x) {
        network.sendFoodPacket(code, y, x);
    }

    public void recvPacket(EventPacket eventPacket) {
        userManager.recvEventPacket(eventPacket);
    }

    public void recvPacket(FoodPacket foodPacket) {
        Food food;
        if (foodPacket.code == EventType.FOOD_PUT) {
            food = playManager.popFoodInMap(foodPacket.y, foodPacket.x);
            userManager.addFoodByUser(foodPacket.uuid, food);
        } else if (foodPacket.code == EventType.FOOD_DOWN) {
            food = userManager.popFoodByUser(foodPacket.uuid);
            playManager.addFoodInMap(foodPacket.y, foodPacket.x, food);
        } else if (foodPacket.code == EventType.FOOD_GET) {
            food = playManager.getFoodInMap(foodPacket.y, foodPacket.x);
            userManager.addFoodByUser(foodPacket.uuid, food);
        } else if (foodPacket.code == EventType.FOOD_GET_SLICED) {
            food = playManager.getSlicedFoodInMap(foodPacket.y, foodPacket.x);
            userManager.addFoodByUser(foodPacket.uuid, food);
        } else if (foodPacket.code == EventType.ACTION) {
            playManager.actionBlockInMap(foodPacket.y, foodPacket.x);
        }
    }

    private void init() {
        this.display = new Display(this);
        this.playManager = new PlayManager(this);
        this.network = new Network(this, "Test", "127.0.0.1", "30000");
        this.userManager = new UserManager(this);
    }

    private void update() {
        // 키 데이터 전송 및 데이터 가공
        playManager.updateData(display.getKeyEventData());

        // 렌더링 데이터 추출
        playManager.updatePlayerRender();
        playManager.updateMapRender();
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