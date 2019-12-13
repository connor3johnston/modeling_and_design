import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageUserUI {

    public JFrame view;

    public JButton btnLoad = new JButton("Load User");
    public JButton btnSave = new JButton("Save User");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtUserName = new JTextField(20);
    public JTextField txtPassword = new JTextField(20);
    public JTextField txtFullName = new JTextField(20);
    public JTextField txtUserType = new JTextField(20);
    public JTextField txtCustomerID = new JTextField(20);

    public ManageUserUI() {
        this.view = new JFrame();

        //View configuration
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Manage User Information");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        //Buttons
        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnLoad);
        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);
        view.getContentPane().add(panelButtons);

        //Customer ID line
        JPanel line1 = new JPanel(new FlowLayout());
        JLabel idLabel = new JLabel("User Name");

        idLabel.setPreferredSize(new Dimension(100, 50));
        idLabel.setHorizontalAlignment(JLabel.RIGHT);

        line1.add(idLabel);
        line1.add(txtUserName);
        view.getContentPane().add(line1);

        //Name line
        JPanel line2 = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel( "Password");

        nameLabel.setPreferredSize(new Dimension(100, 50));
        nameLabel.setHorizontalAlignment(JLabel.RIGHT);

        line2.add(nameLabel);
        line2.add(txtPassword);
        view.getContentPane().add(line2);

        //Phone line
        JPanel line3 = new JPanel(new FlowLayout());
        JLabel phoneLabel = new JLabel("Full Name");

        phoneLabel.setPreferredSize(new Dimension(100, 50));
        phoneLabel.setHorizontalAlignment(JLabel.RIGHT);

        line3.add(phoneLabel);
        line3.add(txtFullName);
        view.getContentPane().add(line3);

        //Address line
        JPanel line4 = new JPanel(new FlowLayout());
        JLabel addressLabel = new JLabel("User Type");

        addressLabel.setPreferredSize(new Dimension(100, 50));
        addressLabel.setHorizontalAlignment(JLabel.RIGHT);

        line4.add(addressLabel);
        line4.add(txtUserType);
        view.getContentPane().add(line4);

        //Payment line
        JPanel line5 = new JPanel(new FlowLayout());
        JLabel paymentLabel = new JLabel("Customer ID");

        paymentLabel.setPreferredSize(new Dimension(100, 50));
        paymentLabel.setHorizontalAlignment(JLabel.RIGHT);

        line5.add(paymentLabel);
        line5.add(txtCustomerID);
        view.getContentPane().add(line5);

        btnLoad.addActionListener(new LoadButtonListener());
        btnSave.addActionListener(new SaveButtonListener());
        btnCancel.addActionListener(new CancelButtonListener());
    }

    public void run() {
        view.setVisible(true);
    }

    public void turnOff() {
        view.dispose();
    }

    class LoadButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserModel user = new UserModel();
            String id = txtUserName.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "User Name cannot be null.");
                return;
            }

            user.mUsername = id.toLowerCase();

            Gson gson = new Gson();
            MessageModel msg = new MessageModel();
            msg.code = MessageModel.GET_USER;
            msg.data = gson.toJson(user);

            SocketNetworkAdapter net = new SocketNetworkAdapter();

            try {
                msg = net.exchange(msg, "localhost", 8080);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (msg.code == MessageModel.OPERATION_FAILED) {
                JOptionPane.showMessageDialog(null, "User does not exist.");
            } else {
                user = gson.fromJson(msg.data, UserModel.class);
                if (user.mPassword.equals("null")) {
                    JOptionPane.showMessageDialog(null, "User does not exist.");
                    return;
                }

                txtPassword.setText(user.mPassword);
                txtFullName.setText(user.mFullname);
                txtUserType.setText(Integer.toString(user.mUserType));
                txtCustomerID.setText(Integer.toString(user.mCustomerID));
            }
        }
    }

    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            UserModel user = new UserModel();
            String username = txtUserName.getText();

            if (username.length() == 0) {
                JOptionPane.showMessageDialog(null, "User Name cannot be null.");
                return;
            }

            user.mUsername = username;

            String password = txtPassword.getText();

            if (password.length() == 0) {
                JOptionPane.showMessageDialog(null, "Password cannot be null.");
                return;
            }

            user.mPassword = password;

            String fullName = txtFullName.getText();

            if (fullName.length() == 0) {
                JOptionPane.showMessageDialog(null, "Full Name cannot be null.");
                return;
            }

            user.mFullname = fullName;

            int userType = -1;

            try {
                System.out.println(txtUserType.getText());
                userType = Integer.parseInt(txtUserType.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "User Type is invalid.");
                return;
            }

            if (userType < 0 || userType > 3) {
                JOptionPane.showMessageDialog(null, "User Type is invalid.");
                return;
            }

            user.mUserType = userType;

            int customerID = -1;

            try {
                customerID = Integer.parseInt(txtCustomerID.getText());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Customer ID is invalid.");
                return;
            }

            if (user.mUserType == UserModel.CUSTOMER) {
                user.mCustomerID = customerID;
            }

            Gson gson = new Gson();
            MessageModel msg = new MessageModel();
            msg.code = MessageModel.PUT_USER;
            msg.data = gson.toJson(user);

            SocketNetworkAdapter net = new SocketNetworkAdapter();

            try {
                msg = net.exchange(msg, "localhost", 8080);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (msg.code == MessageModel.OPERATION_FAILED) {
                JOptionPane.showMessageDialog(null, "User not added successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "User added successfully.");
            }

            turnOff();
        }
    }

    class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            turnOff();
        }
    }
}
