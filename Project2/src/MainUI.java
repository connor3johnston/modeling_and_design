import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI {
    JFrame view;

    public JButton btnManageProduct = new JButton("Manage Product");
    public JButton btnManageCustomer = new JButton("Manage Customer");
    public JButton btnManagePurchase = new JButton("Manage Purchase");
    public JButton btnExit = new JButton("Exit");

    public MainUI() {
        this.view = new JFrame();

        //View configuration
        view.setTitle("Store Management System");
        view.setSize(900, 300);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        //Title configuration
        JLabel title = new JLabel("Store Management System", JLabel.CENTER);

        title.setFont(title.getFont().deriveFont(24.0f));
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        view.getContentPane().add(title);

        //Buttons
        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnManageProduct);
        panelButtons.add(btnManageCustomer);
        panelButtons.add(btnManagePurchase);
        panelButtons.add(btnExit);
        panelButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        view.getContentPane().add(panelButtons);

        btnManageProduct.addActionListener(new ManageProductListener());
        btnManageCustomer.addActionListener(new ManageCustomerListener());
        btnManagePurchase.addActionListener(new AddPurchaseListener());
        btnExit.addActionListener(new ExitListener());
    }

    class ManageProductListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ManageProductUI manageProductUI = new ManageProductUI();

            turnOff();
            manageProductUI.run();
        }
    }

    class ManageCustomerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ManageCustomerUI manageCustomerUI = new ManageCustomerUI();

            turnOff();
            manageCustomerUI.run();
        }
    }

    class AddPurchaseListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ManagePurchaseUI apUI = new ManagePurchaseUI();

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
