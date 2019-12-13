import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PurchaseHistoryUI {

    public JFrame view;
    public JTable purchaseTable;

    public UserModel user = null;

    public PurchaseHistoryUI(UserModel user) {
        this.view = new JFrame();
        this.user = user;

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Purchase History");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Purchase history for " + user.mFullname);

        if (user.mUserType == UserModel.MANAGER) {
            title = new JLabel("Sales Summary");
        }

        title.setFont (title.getFont().deriveFont (24.0f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        view.getContentPane().add(title);


        Gson gson = new Gson();
        MessageModel msg = new MessageModel();

        if (user.mUserType == UserModel.CUSTOMER) {
            msg.code = MessageModel.GET_PURCHASE_HISTORY;
        } else {
            msg.code = MessageModel.GET_FULL_PURCHASE_HISTORY;
        }
        msg.data = gson.toJson(user);

        SocketNetworkAdapter net = new SocketNetworkAdapter();

        try {
            msg = net.exchange(msg, "localhost", 8080);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (msg.code == MessageModel.OPERATION_FAILED) {
            JOptionPane.showMessageDialog(null, "No purchase history.");
            view.dispose();
            return;
        }

        PurchaseListModel list = gson.fromJson(msg.data, PurchaseListModel.class);
        DefaultTableModel tableData = new DefaultTableModel();

        tableData.addColumn("PurchaseID");
        tableData.addColumn("ProductID");
        tableData.addColumn("Product Name");
        tableData.addColumn("Total");

        for (PurchaseModel purchase : list.purchases) {
            Object[] row = new Object[tableData.getColumnCount()];
            row[0] = purchase.mPurchaseID;
            row[1] = purchase.mProductID;
            ProductModel product = new ProductModel();
            product.mProductID = purchase.mProductID;

            gson = new Gson();
            msg = new MessageModel();
            msg.code = MessageModel.GET_PRODUCT;
            msg.data = gson.toJson(product);

            net = new SocketNetworkAdapter();

            try {
                msg = net.exchange(msg, "localhost", 8080);
            } catch (Exception e) {
                e.printStackTrace();
            }

            product = gson.fromJson(msg.data, ProductModel.class);
            row[2] = product.mName;
            row[3] = purchase.mTotal;
            tableData.addRow(row);
        }

        purchaseTable = new JTable(tableData);

        JScrollPane scrollableList = new JScrollPane(purchaseTable);

        view.getContentPane().add(scrollableList);
    }
}
