import javax.swing.*;

public class StoreManager {
    public static String dbms = "SQLite";
    public static String path = "/Users/Connor/Desktop/current-classes/modeling_and_design/Assignment4/store-db.db";

    IDataAdapter adapter = null;
    private static StoreManager instance = null;

    public static StoreManager getInstance() {
        if (instance == null) {
            instance = new StoreManager(dbms, path);
        }

        return instance;
    }

    private StoreManager(String dbms, String dbfile) {
        if (dbms.equals("Oracle")) {
            adapter = new OracleDataAdapter();
        } else if (dbms.equals("SQLite")) {
            adapter = new SQLiteDataAdapter();
        } else if (dbms.equals("Network")) {
            adapter = new NetworkDataAdapter();
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
        LoginUI ui = new LoginUI(adapter);
        ui.run();
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            dbms = args[0];

            if (args.length == 1) {
                if (dbms.equals("SQLite")) {
                    JFileChooser fc = new JFileChooser();
                    if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                        path = fc.getSelectedFile().getAbsolutePath();
                    }
                } else {
                    path = JOptionPane.showInputDialog("Enter address of database server as host:port");
                }
            } else {
                path = args[1];
            }
        }
        StoreManager.getInstance().run();
    }
}
