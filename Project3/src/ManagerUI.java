import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManagerUI {
    public JFrame view;

    public JButton btnManageProduct = new JButton("Manage Products");
    public JButton btnChangePassword = new JButton("Update Information");
    public JButton btnViewPurchases = new JButton("View Sales Summary");
    public JButton btnExit = new JButton("Exit");

    UserModel user;
    public ManagerUI(UserModel user) {
        this.view = new JFrame();
        this.user = user;

        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setTitle("Store Management System - Manager View");
        view.setSize(1000, 600);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Store Management System - Manager View", JLabel.CENTER);

        title.setFont(title.getFont().deriveFont(24.0f));
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        view.getContentPane().add(title);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnManageProduct);
        panelButtons.add(btnViewPurchases);
        panelButtons.add(btnChangePassword);
        panelButtons.add(btnExit);
        panelButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        view.getContentPane().add(panelButtons);


        btnManageProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ManageProductUI ui = new ManageProductUI();
                ui.run();
            }
        });

        btnViewPurchases.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PurchaseHistoryUI ui = new PurchaseHistoryUI(user);
                ui.view.setVisible(true);
            }
        });

        btnChangePassword.addActionListener(new ChangePasswordButtonListener());

        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LoginUI ui = new LoginUI();

                turnOff();
                ui.run();
            }
        });
    }


    class ChangePasswordButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UpdateInformationUI ui = new UpdateInformationUI(user);

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
