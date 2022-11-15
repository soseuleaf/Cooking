import Data.*;
import Data.EventEnum;
import Data.KeyEventData;
import Render.Assets;

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

    public void sendFoodPacket(EventEnum code, int y, int x) {
        network.sendFoodPacket(code, y, x);
    }

    public void recvPacket(EventPacket eventPacket) {
        userManager.recvEventPacket(eventPacket);
    }

    public void recvPacket(FoodPacket foodPacket) {
        Food food;
        if (foodPacket.code == EventEnum.FOOD_PUT) {
            food = playManager.popFoodInMap(foodPacket.y, foodPacket.x);
            userManager.addFoodByUser(foodPacket.uuid, food);
        } else if (foodPacket.code == EventEnum.FOOD_DOWN) {
            food = userManager.popFoodByUser(foodPacket.uuid);
            playManager.addFoodInMap(foodPacket.y, foodPacket.x, food);
        } else if (foodPacket.code == EventEnum.FOOD_GET) {
            food = playManager.getFoodByMap(foodPacket.y, foodPacket.x);
            userManager.addFoodByUser(foodPacket.uuid, food);
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
        KeyEventData keyEventData = display.getKeyEventData();
        if (keyEventData == null) keyEventData = new KeyEventData(false, false, false, false, false);
        playManager.updateData(keyEventData);

        // 렌더링 데이터 추출
        playManager.updateRender();
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