import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI {
    public JFrame view;

    public JButton btnLogin = new JButton("Login");
    public JButton btnLogout = new JButton("Logout");

    public JTextField txtUsername = new JTextField(20);
    public JTextField txtPassword = new JPasswordField(20);

    public LoginUI() {
        this.view = new JFrame();
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Login");
        view.setSize(500, 300);

        Container pane = view.getContentPane();
        pane.setLayout(new BoxLayout(pane, BoxLayout.PAGE_AXIS));

        JPanel line = new JPanel();
        line.add(new JLabel("Username"));
        line.add(txtUsername);
        pane.add(line);

        line = new JPanel();
        line.add(new JLabel("Password"));
        line.add(txtPassword);
        pane.add(line);

        //Buttons
        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnLogin);
        panelButtons.add(btnLogout);
        pane.add(panelButtons);

        btnLogin.addActionListener(new LoginActionListener());
        btnLogout.addActionListener(new LogoutActionListener());
    }

    public void run() {
        view.setVisible(true);
    }

    private class LogoutActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            view.dispose();
            System.exit(1);
        }
    }

    private class LoginActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserModel user = new UserModel();

            user.mUsername = txtUsername.getText();
            user.mPassword = txtPassword.getText();

            if (user.mUsername.length() == 0 || user.mPassword.length() == 0) {
                JOptionPane.showMessageDialog(null, "Username or password cannot be null.");
                return;
            }

            Gson gson = new Gson();

            MessageModel msg = new MessageModel();
            msg.code = MessageModel.LOGIN;
            msg.data = gson.toJson(user);

            SocketNetworkAdapter net = new SocketNetworkAdapter();

            try {
                msg = net.exchange(msg, "localhost", 8080);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            if (msg.code == MessageModel.OPERATION_FAILED)
                JOptionPane.showMessageDialog(null, "Invalid username or password. Access denied.");
            else {
                view.setVisible(false);

                user = gson.fromJson(msg.data, UserModel.class);

                System.out.println("User = " + user);
                if (user.mUserType == UserModel.MANAGER) {
                    ManagerUI ui = new ManagerUI(user);
                    ui.run();
                } else if (user.mUserType == UserModel.CASHIER) {
                    CashierUI ui = new CashierUI(user);
                    ui.run();
                } else if (user.mUserType == UserModel.CUSTOMER) {
                    CustomerUI ui = new CustomerUI(user);
                    ui.view.setVisible(true);
                } else if (user.mUserType == UserModel.ADMIN) {
                    AdminUI ui = new AdminUI(user);
                    ui.run();
                } else {
                    JOptionPane.showMessageDialog(null, "User type not supported.");
                    view.setVisible(true);
                }
            }
        }
    }
}
