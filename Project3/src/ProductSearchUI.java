import com.google.gson.Gson;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class ProductSearchUI {

    public JFrame view;
    public JTable productTable;
    public UserModel user;

    public JButton btnSearch = new JButton("Search");
    public JButton btnExit = new JButton("Exit");

    public JTextField txtName = new JTextField(20);

    public ProductSearchUI(UserModel user) {
        this.view = new JFrame();
        this.user = user;

        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Search Product");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Product Search");

        title.setFont(title.getFont().deriveFont (24.0f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        view.getContentPane().add(title);


        JPanel line2 = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setPreferredSize(new Dimension(100, 50));
        nameLabel.setHorizontalAlignment(JLabel.RIGHT);
        line2.add(nameLabel);
        line2.add(txtName);
        view.getContentPane().add(line2);

        //Buttons
        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnSearch);
        panelButtons.add(btnExit);
        panelButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        view.getContentPane().add(panelButtons);

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame newView = new JFrame();

                newView.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                newView.setTitle("Search Product");
                newView.setSize(600, 400);
                newView.getContentPane().setLayout(new BoxLayout(newView.getContentPane(), BoxLayout.PAGE_AXIS));

                JLabel title = new JLabel("Search results for " + txtName.getText());

                title.setFont (title.getFont().deriveFont (24.0f));
                title.setHorizontalAlignment(SwingConstants.CENTER);
                newView.getContentPane().add(title);

                ProductModel product = new ProductModel();
                product.mName = txtName.getText();

                Gson gson = new Gson();
                MessageModel msg = new MessageModel();
                msg.code = MessageModel.GET_PRODUCT_LIST;
                msg.data = gson.toJson(product);

                SocketNetworkAdapter net = new SocketNetworkAdapter();

                try {
                    msg = net.exchange(msg, "localhost", 8080);
                } catch (Exception error) {
                    error.printStackTrace();
                }

                if (msg.code == MessageModel.OPERATION_FAILED) {
                    JOptionPane.showMessageDialog(null, "No product found.");
                    return;
                }

                ProductListModel list = gson.fromJson(msg.data, ProductListModel.class);
                DefaultTableModel tableData = new DefaultTableModel();

                tableData.addColumn("ProductID");
                tableData.addColumn("Product Name");
                tableData.addColumn("Price");
                tableData.addColumn("Quantity");

                for (ProductModel p : list.products) {
                    Object[] row = new Object[tableData.getColumnCount()];

                    row[0] = p.mProductID;
                    row[1] = p.mName;
                    row[2] = p.mPrice;
                    row[3] = p.mQuantity;
                    tableData.addRow(row);
                }

                productTable = new JTable(tableData);

                JScrollPane scrollableList = new JScrollPane(productTable);

                newView.getContentPane().add(scrollableList);
                newView.setVisible(true);
            }
        });

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                turnOff();
            }
        });
    }

    public void turnOff() {
        view.dispose();
    }
}
