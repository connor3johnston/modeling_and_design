import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CashierUI {
    public JFrame view;

    public JButton btnManageCustomer = new JButton("Manage Customer");
    public JButton btnManagePurchase = new JButton("Manage Purchase");
    public JButton btnChangePassword = new JButton("Update Information");
    public JButton btnExit = new JButton("Exit");

    UserModel user;
    public CashierUI(UserModel user) {
        this.view = new JFrame();
        this.user = user;

        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setTitle("Store Management System - Cashier View");
        view.setSize(1000, 600);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Store Management System - Cashier View", JLabel.CENTER);

        title.setFont(title.getFont().deriveFont(24.0f));
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        view.getContentPane().add(title);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnManageCustomer);
        panelButtons.add(btnManagePurchase);
        panelButtons.add(btnChangePassword);
        panelButtons.add(btnExit);
        panelButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        view.getContentPane().add(panelButtons);

        btnManageCustomer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManageCustomerUI ui = new ManageCustomerUI();
                ui.run();
            }
        });

        btnManagePurchase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManagePurchaseUI ui = new ManagePurchaseUI();
                ui.run();
            }
        });

        btnChangePassword.addActionListener(new ChangePasswordButtonListener());
        btnExit.addActionListener(new ExitButtonListener());
    }

    class ChangePasswordButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UpdateInformationUI ui = new UpdateInformationUI(user);

            ui.run();
        }
    }

    class ExitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            LoginUI ui = new LoginUI();

            turnOff();
            ui.run();
        }
    }

    public void run() {
        view.setVisible(true);
    }

    public void turnOff() {
        view.setVisible(false);
    }
}
