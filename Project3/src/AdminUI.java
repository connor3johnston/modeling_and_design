import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminUI {
    public JFrame view;

    public JButton btnManageUser = new JButton("Manage User");
    public JButton btnChangePassword = new JButton("Update Information");
    public JButton btnExit = new JButton("Exit");

    UserModel user;
    public AdminUI(UserModel user) {
        this.view = new JFrame();
        this.user = user;

        view.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        view.setTitle("Store Management System - Admin View");
        view.setSize(1000, 600);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Store Management System - Admin View", JLabel.CENTER);

        title.setFont(title.getFont().deriveFont(24.0f));
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        view.getContentPane().add(title);

        JPanel panelButtons = new JPanel(new FlowLayout());

        panelButtons.add(btnManageUser);
        panelButtons.add(btnChangePassword);
        panelButtons.add(btnExit);
        panelButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);

        view.getContentPane().add(panelButtons);

        btnManageUser.addActionListener(new ManageUserButtonListener());
        btnChangePassword.addActionListener(new ChangePasswordButtonListener());
        btnExit.addActionListener(new ExitButtonListener());
    }

    class ManageUserButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ManageUserUI ui = new ManageUserUI();

            ui.run();
        }
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
