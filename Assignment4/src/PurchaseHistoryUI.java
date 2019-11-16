import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PurchaseHistoryUI {

    public JFrame view;
    public JTable purchaseTable;

    public UserModel user = null;

    IDataAdapter adapter;
    public PurchaseHistoryUI(IDataAdapter adapter, UserModel user) {
        this.view = new JFrame();
        this.user = user;
        this.adapter = adapter;

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("View Purchase History - Customer View");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Purchase history for " + user.mFullname);

        title.setFont (title.getFont().deriveFont (24.0f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        view.getContentPane().add(title);

        PurchaseListModel list = this.adapter.loadPurchaseHistory(user.mCustomerID);
        DefaultTableModel tableData = new DefaultTableModel();

        tableData.addColumn("PurchaseID");
        tableData.addColumn("ProductID");
        tableData.addColumn("Product Name");
        tableData.addColumn("Total");

        for (PurchaseModel purchase : list.purchases) {
            Object[] row = new Object[tableData.getColumnCount()];
            row[0] = purchase.mPurchaseID;
            row[1] = purchase.mProductID;
            ProductModel product = this.adapter.loadProduct(purchase.mProductID);
            row[2] = product.mName;
            row[3] = purchase.mTotal;
            tableData.addRow(row);
        }

        purchaseTable = new JTable(tableData);

        JScrollPane scrollableList = new JScrollPane(purchaseTable);

        view.getContentPane().add(scrollableList);
    }
}
