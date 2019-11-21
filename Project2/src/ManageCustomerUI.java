import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageCustomerUI {

    public JFrame view;

    public JButton btnLoad = new JButton("Load Customer");
    public JButton btnSave = new JButton("Save Customer");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtCustomerID = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtPhone = new JTextField(20);
    public JTextField txtAddress = new JTextField(20);
    public JTextField txtPaymentInfo = new JTextField(20);

    public ManageCustomerUI() {
        this.view = new JFrame();

        //View configuration
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Manage Customer Information");
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
        JLabel idLabel = new JLabel("Customer ID");

        idLabel.setPreferredSize(new Dimension(100, 50));
        idLabel.setHorizontalAlignment(JLabel.RIGHT);

        line1.add(idLabel);
        line1.add(txtCustomerID);
        view.getContentPane().add(line1);

        //Name line
        JPanel line2 = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel("Name");

        nameLabel.setPreferredSize(new Dimension(100, 50));
        nameLabel.setHorizontalAlignment(JLabel.RIGHT);

        line2.add(nameLabel);
        line2.add(txtName);
        view.getContentPane().add(line2);

        //Phone line
        JPanel line3 = new JPanel(new FlowLayout());
        JLabel phoneLabel = new JLabel("Phone");

        phoneLabel.setPreferredSize(new Dimension(100, 50));
        phoneLabel.setHorizontalAlignment(JLabel.RIGHT);

        line3.add(phoneLabel);
        line3.add(txtPhone);
        view.getContentPane().add(line3);

        //Address line
        JPanel line4 = new JPanel(new FlowLayout());
        JLabel addressLabel = new JLabel("Address");

        addressLabel.setPreferredSize(new Dimension(100, 50));
        addressLabel.setHorizontalAlignment(JLabel.RIGHT);

        line4.add(addressLabel);
        line4.add(txtAddress);
        view.getContentPane().add(line4);

        //Payment line
        JPanel line5 = new JPanel(new FlowLayout());
        JLabel paymentLabel = new JLabel("Payment Info");

        paymentLabel.setPreferredSize(new Dimension(100, 50));
        paymentLabel.setHorizontalAlignment(JLabel.RIGHT);

        line5.add(paymentLabel);
        line5.add(txtPaymentInfo);
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
        MainUI m = new MainUI();
        m.run();
    }

    class LoadButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            CustomerModel customer = new CustomerModel();
            String id = txtCustomerID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "CustomerID cannot be null.");
                return;
            }

            try {
                customer.mCustomerID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "CustomerID is invalid.");
                return;
            }

            Gson gson = new Gson();
            MessageModel msg = new MessageModel();
            msg.code = MessageModel.GET_CUSTOMER;
            msg.data = gson.toJson(customer);

            SocketNetworkAdapter net = new SocketNetworkAdapter();

            try {
                msg = net.exchange(msg, "localhost", 8080);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (msg.code == MessageModel.OPERATION_FAILED) {
                JOptionPane.showMessageDialog(null, "Customer does not exist.");
            } else {
                customer = gson.fromJson(msg.data, CustomerModel.class);
                if (customer.mName.equals("null")) {
                    JOptionPane.showMessageDialog(null, "Customer does not exist.");
                    return;
                }

                txtName.setText(customer.mName);
                txtPhone.setText(customer.mPhone);
                txtAddress.setText(customer.mAddress);
                txtPaymentInfo.setText(customer.mPaymentInfo);
            }
        }
    }

    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            CustomerModel customer = new CustomerModel();
            String id = txtCustomerID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "CustomerID cannot be null.");
                return;
            }

            try {
                customer.mCustomerID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "CustomerID is invalid.");
                return;
            }

            String name = txtName.getText();
            if (name.length() == 0) {
                JOptionPane.showMessageDialog(null, "Customer name cannot be empty.");
                return;
            }

            customer.mName = name;

            customer.mPhone = txtPhone.getText();
            customer.mAddress = txtAddress.getText();
            customer.mPaymentInfo = txtPaymentInfo.getText();

            Gson gson = new Gson();
            MessageModel msg = new MessageModel();
            msg.code = MessageModel.PUT_CUSTOMER;
            msg.data = gson.toJson(customer);

            SocketNetworkAdapter net = new SocketNetworkAdapter();

            try {
                msg = net.exchange(msg, "localhost", 8080);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (msg.code == MessageModel.OPERATION_FAILED) {
                JOptionPane.showMessageDialog(null, "Customer not added successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Customer added successfully.");
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
