import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateInformationUI {
    public JFrame view;

    public JButton btnUpdate = new JButton("Update");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtNewPassword = new JTextField(20);
    public JTextField txtNewFullName = new JTextField(20);

    UserModel user;
    public UpdateInformationUI(UserModel user) {
        this.view = new JFrame();
        this.user = user;

        //View configuration
        view.setTitle("Update Information");
        view.setSize(600, 300);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Update Password and/or Full Name for User \"" + user.mUsername + "\"", JLabel.CENTER);

        title.setFont(title.getFont().deriveFont(18.0f));
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        view.getContentPane().add(title);

        //New Password line
        JPanel line1 = new JPanel(new FlowLayout());
        JLabel idLabel = new JLabel("New Password");

        idLabel.setPreferredSize(new Dimension(100, 50));
        idLabel.setHorizontalAlignment(JLabel.RIGHT);

        line1.add(idLabel);
        line1.add(txtNewPassword);
        line1.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        view.getContentPane().add(line1);

        //New Full Name line
        JPanel line2 = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel("New Full Name");

        nameLabel.setPreferredSize(new Dimension(100, 50));
        nameLabel.setHorizontalAlignment(JLabel.RIGHT);

        line2.add(nameLabel);
        line2.add(txtNewFullName);
        line2.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        view.getContentPane().add(line2);

        //Buttons
        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnUpdate);
        panelButtons.add(btnCancel);
        panelButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        view.getContentPane().add(panelButtons);

        btnUpdate.addActionListener(new UpdateButtonListener());
        btnCancel.addActionListener(new CancelButtonListener());
    }

    class UpdateButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String newPassword = txtNewPassword.getText();
            String newFullName = txtNewFullName.getText();

            if (newPassword.length() != 0) {
                user.mPassword = newPassword;
            }

            if (newFullName.length() != 0) {
                user.mFullname = newFullName;
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
                JOptionPane.showMessageDialog(null, "Not updated successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Updated successfully.");
            }

            turnOff();
        }
    }

    public void run() {
        view.setVisible(true);
    }

    public void turnOff() {
        view.dispose();
    }

    class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            turnOff();
        }
    }
}
