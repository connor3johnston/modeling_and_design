import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManageCustomerUI {

    public JFrame view;

    public JButton btnLoad = new JButton("Load Customer");
    public JButton btnSave = new JButton("Save Customer");
    public JButton btnExit = new JButton("Exit");

    public JTextField txtCustomerID = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtPhone = new JTextField(20);
    public JTextField txtAddress = new JTextField(20);

    IDataAdapter adapter;
    public ManageCustomerUI(IDataAdapter adapter) {
        this.view = new JFrame();
        this.adapter = adapter;

        //View configuration
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Manage Customer Information");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        //Buttons
        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnLoad);
        panelButtons.add(btnSave);
        panelButtons.add(btnExit);
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

        btnLoad.addActionListener(new LoadButtonListener());
        btnSave.addActionListener(new SaveButtonListener());
        btnExit.addActionListener(new ExitButtonListener());
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

            // call data access!

            customer = adapter.loadCustomer(customer.mCustomerID);

            if (customer == null) {
                JOptionPane.showMessageDialog(null, "Customer does not exist.");
            } else {
                txtName.setText(customer.mName);
                txtAddress.setText(customer.mAddress);
                txtPhone.setText(customer.mPhone);
            }
        }
    }

    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            CustomerModel customer = new CustomerModel();
            Gson gson = new Gson();
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

            String phone = txtPhone.getText();
            customer.mPhone = phone;

            String address = txtAddress.getText();
            customer.mAddress = address;

            int res = adapter.saveCustomer(customer);

            if (res == IDataAdapter.CUSTOMER_SAVE_FAILED) {
                JOptionPane.showMessageDialog(null, "Customer not saved successfully.");
            } else
                JOptionPane.showMessageDialog(null, "Customer saved successfully.");
            }
        }

    class ExitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            turnOff();
        }
    }
}
