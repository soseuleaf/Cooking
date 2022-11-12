import Data.*;

public class CookTogether implements Runnable {
    private Display display;
    private Network network;
    private boolean running;
    private KeyEventData keyEventData;
    private PlayerManager playerManager;
    private UserManager userManager;

    public CookTogether() {
        init();
    }

    public KeyEventData getKeyEventData() {
        return keyEventData;
    }

    public void addRenderData(RenderData renderData) {
        display.addRenderData(renderData);
    }

    public void sendEventPacket(int x, int y){
        network.sendMovePacket(EventEnum.MOVE, "Move", x, y);
    }

    public void recvEventPacket(EventPacket eventPacket) {
        userManager.recvEventPacket(eventPacket);
    }

    private void init() {
        this.display = new Display(this);
        this.playerManager = new PlayerManager(this);
        this.network = new Network(this,"Test", "127.0.0.1", "30000");
        this.userManager = new UserManager(this);
    }

    private void update() {
        // 키 데이터 전송
        keyEventData = display.getKeyEventData();
        playerManager.setKeyEventData(keyEventData);

        // 데이터 가공
        playerManager.update();
        userManager.update();
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