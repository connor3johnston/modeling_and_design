public class StoreManager {
    public static StoreManager getInstance() {
        return new StoreManager();
    }

    public void run() {
        MainUI ui = new MainUI();
        ui.run();
    }

    public static void main(String[] args) {
        StoreManager.getInstance().run();
    }
}
