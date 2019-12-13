public class StoreManager {
    public static StoreManager getInstance() {
        return new StoreManager();
    }

    public void run() {
        LoginUI ui = new LoginUI();
        ui.run();
    }

    public static void main(String[] args) {
        StoreManager.getInstance().run();
    }
}
