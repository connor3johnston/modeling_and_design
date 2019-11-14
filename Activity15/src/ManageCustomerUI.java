import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ManageCustomerUI {

    public JFrame view;
    public MainUI mainView;

    public JButton btnLoad = new JButton("Load Customer");
    public JButton btnSave = new JButton("Save Customer");
    public JButton btnExit = new JButton("Exit");

    public JTextField txtCustomerID = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtPhone = new JTextField(20);
    public JTextField txtAddress = new JTextField(20);
    public JTextField txtPaymentInfo = new JTextField(20);


    public ManageCustomerUI(MainUI mainView) {
        this.view = new JFrame();
        this.mainView = mainView;

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
        btnExit.addActionListener(new ExitButtonListener());
    }

    public void run() {
        view.setVisible(true);
    }

    public void turnOff() {
        view.dispose();
        mainView.run();
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

            try {
                Socket link = new Socket("localhost", 8081);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                output.println("GET");
                output.println(customer.mCustomerID);

                customer.mName = input.nextLine();

                if (customer.mName.equals("null")) {
                    JOptionPane.showMessageDialog(null, "Customer does not exist.");
                    return;
                }

                txtName.setText(customer.mName);

                customer.mPhone = input.nextLine();
                txtPhone.setText(customer.mPhone);

                customer.mAddress = input.nextLine();
                txtAddress.setText(customer.mAddress);

                customer.mPaymentInfo = input.nextLine();
                txtPaymentInfo.setText(customer.mPaymentInfo);

            } catch (Exception e) {
                e.printStackTrace();
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

            String phone = txtPhone.getText();
            customer.mPhone = phone;
            String address = txtAddress.getText();
            customer.mAddress = address;
            String paymentInfo = txtPaymentInfo.getText();
            customer.mPaymentInfo = paymentInfo;

            try {
                Socket link = new Socket("localhost", 8081);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                output.println("PUT");
                output.println(customer.mCustomerID);
                output.println(customer.mName);
                output.println(customer.mPhone);
                output.println(customer.mAddress);
                output.println(customer.mPaymentInfo);

                JOptionPane.showMessageDialog(null, "Customer information updated successfully.");
                turnOff();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class ExitButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            turnOff();
        }
    }
}
