import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI {
    JFrame view;

    public JButton btnUpdateProduct = new JButton("Manage Product Information");
    public JButton btnUpdateCustomer = new JButton("Manage Customer Information");
    public JButton btnAddPurchase = new JButton("Add Purchase");
    public JButton btnExit = new JButton("Exit");

    IDataAdapter adapter;

    public MainUI(IDataAdapter adapter) {
        this.view = new JFrame();
        this.adapter = adapter;

        view.setTitle("Store Management System");
        view.setSize(1000, 600);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Welcome to the Store Manager!");

        title.setFont(title.getFont().deriveFont(24.0f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        view.getContentPane().add(title);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnUpdateProduct);
        panelButtons.add(btnUpdateCustomer);
        panelButtons.add(btnAddPurchase);
        panelButtons.add(btnExit);
        view.getContentPane().add(panelButtons);

        btnUpdateProduct.addActionListener(new UpdateProductListener());
        btnUpdateCustomer.addActionListener(new UpdateCustomerListener());
        btnAddPurchase.addActionListener(new AddPurchaseListener());
        btnExit.addActionListener(new ExitListener());
    }

    class UpdateProductListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ManageProductUI manageProductUI = new ManageProductUI(new MainUI(adapter));

            turnOff();
            manageProductUI.run();
        }
    }

    class UpdateCustomerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ManageCustomerUI manageCustomerUI = new ManageCustomerUI(new MainUI(adapter));

            turnOff();
            manageCustomerUI.run();
        }
    }

    class AddPurchaseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            AddPurchaseUI apUI = new AddPurchaseUI(adapter, new MainUI(adapter));

            turnOff();
            apUI.run();
        }
    }

    class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.exit(1);
        }
    }

    public void run() {
        view.setVisible(true);
    }

    public void turnOff() {
        view.setVisible(false);
    }
}
