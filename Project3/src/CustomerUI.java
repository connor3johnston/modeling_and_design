import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerUI {

    public JFrame view;

    public JButton btnAddPurchase = new JButton("Add a Purchase");
    public JButton btnViewPurchases = new JButton("View Purchase History");
    public JButton btnSearchProduct = new JButton("Search Product");
    public JButton btnChangePassword = new JButton("Update Information");
    public JButton btnExit = new JButton("Exit");

    public UserModel user;
    public CustomerUI(UserModel user) {
        this.view = new JFrame();
        this.user = user;

        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setTitle("Store Management System - Customer View");
        view.setSize(1000, 600);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Store Management System - Customer View", JLabel.CENTER);

        title.setFont(title.getFont().deriveFont(24.0f));
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        view.getContentPane().add(title);

        JPanel panelButtons = new JPanel(new FlowLayout());

        panelButtons.add(btnAddPurchase);
        panelButtons.add(btnViewPurchases);
        panelButtons.add(btnSearchProduct);
        panelButtons.add(btnChangePassword);
        panelButtons.add(btnExit);
        panelButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        view.getContentPane().add(panelButtons);


        btnViewPurchases.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                PurchaseHistoryUI ui = new PurchaseHistoryUI(user);
                ui.view.setVisible(true);
            }
        });

        btnAddPurchase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                AddPurchaseUI ui = new AddPurchaseUI();
                ui.run();
            }
        });

        btnSearchProduct.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ProductSearchUI ui = new ProductSearchUI(user);
                ui.view.setVisible(true);
            }
        } );

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
