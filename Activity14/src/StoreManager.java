import javax.swing.*;

public class StoreManager {
    public static final String DBMS_SQLITE = "SQLite";
    public static final String DB_File = "/Users/Connor/Desktop/current-classes/modeling_and_design/Activity14/store.db";

    IDataAdapter adapter = null;
    private static StoreManager instance = null;

    public static StoreManager getInstance() {
        if (instance == null) {
            instance = new StoreManager(DBMS_SQLITE, DB_File);
        }

        return instance;
    }

    private StoreManager(String dbms, String dbfile) {
        if (dbms.equals("Oracle")) {
            adapter = new OracleDataAdapter();
        } else if (dbms.equals("SQLite")) {
            adapter = new SQLiteDataAdapter();
        }

        adapter.connect(dbfile);
    }

    public IDataAdapter getDataAdapter() {
        return adapter;
    }

    public void setDataAdapter(IDataAdapter a) {
        adapter = a;
    }

    public void run() {
        MainUI ui = new MainUI(adapter);
        ui.run();
    }

    public static void main(String[] args) {
        StoreManager.getInstance().run();
    }
}
