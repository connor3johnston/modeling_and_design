import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCustomerUI {
    public JFrame view;
    public MainUI mainView;

    public JButton btnAdd = new JButton("Add");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtCustomerID = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtAddress = new JTextField(20);
    public JTextField txtPhone = new JTextField(20);
    public JTextField txtPaymentInfo = new JTextField(20);

    IDataAdapter adapter;

    public AddCustomerUI(IDataAdapter adapter, MainUI mainView) {
        this.view = new JFrame();
        this.adapter = adapter;
        this.mainView = mainView;

        view.setTitle("Add Customer");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        String[] labels = {"CustomerID", "Name", "Address", "Phone", "Payment Info"};

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("Customer ID "));
        line1.add(txtCustomerID);
        view.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Name "));
        line2.add(txtName);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("Address "));
        line3.add(txtAddress);
        view.getContentPane().add(line3);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Phone "));
        line4.add(txtPhone);
        view.getContentPane().add(line4);

        JPanel line5 = new JPanel(new FlowLayout());
        line5.add(new JLabel("Payment Info"));
        line5.add(txtPaymentInfo);
        view.getContentPane().add(line5);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnAdd);
        panelButtons.add(btnCancel);
        view.getContentPane().add(panelButtons);

        btnAdd.addActionListener(new AddButtonListener());
        btnCancel.addActionListener(new CancelButtonListener());
    }

    class AddButtonListener implements ActionListener {
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

            String address = txtAddress.getText();

            if (address.length() == 0) {
                JOptionPane.showMessageDialog(null, "Customer address cannot be empty.");
                return;
            }

            customer.mAddress = address;

            String phone = txtPhone.getText();

            if (phone.length() == 0) {
                JOptionPane.showMessageDialog(null, "Customer phone cannot be empty.");
                return;
            }

            customer.mPhone = phone;

            String paymentInfo = txtPaymentInfo.getText();

            if (paymentInfo.length() == 0) {
                JOptionPane.showMessageDialog(null, "Customer payment info cannot be empty.");
                return;
            }

            customer.mPaymentInfo = paymentInfo;

            switch (adapter.saveCustomer(customer)) {
                case SQLiteDataAdapter.DUPLICATE_ERROR:
                    JOptionPane.showMessageDialog(null, "Customer NOT added successfully. Duplicate customer ID");
                    break;
                default:
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

    public void run() {
        view.setVisible(true);
    }

    public void turnOff() {
        view.dispose();
        mainView.run();
    }
}
