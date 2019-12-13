import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

public class AddPurchaseUI {
    public JFrame view;

    public JButton btnSave = new JButton("Save Purchase");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtPurchaseID = new JTextField(20);
    public JTextField txtProductID = new JTextField(20);
    public JTextField txtCustomerID = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);

    public JLabel labDate = new JLabel("Purchase Date: ");
    public JLabel labProductName = new JLabel("Product Name: [not specified]");
    public JLabel labCustomerName = new JLabel("Customer Name: [not specified]");
    public JLabel labPrice = new JLabel("Product Price: [not specified]");
    public JLabel labCost = new JLabel("Cost: $0.00");
    public JLabel labTax = new JLabel("Tax: $0.00");
    public JLabel labTotalCost = new JLabel("Total Cost: $0.00");

    ProductModel product;
    CustomerModel customer;
    PurchaseModel purchase;

    public AddPurchaseUI() {
        this.view = new JFrame();

        //Buttons
        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);
        panelButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        view.getContentPane().add(panelButtons);

        //View configuration
        view.setTitle("Add Purchase");
        view.setSize(800, 600);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        //Purchase ID line
        JPanel line1 = new JPanel(new FlowLayout());
        JLabel idLabel = new JLabel("Purchase ID");

        idLabel.setPreferredSize(new Dimension(100, 50));
        idLabel.setHorizontalAlignment(JLabel.RIGHT);

        labDate.setPreferredSize(new Dimension(350, 50));
        labDate.setHorizontalAlignment(JLabel.LEFT);

        line1.add(idLabel);
        line1.add(txtPurchaseID);
        line1.add(labDate);
        line1.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        view.getContentPane().add(line1);

        //Product ID line
        JPanel line2 = new JPanel(new FlowLayout());
        JLabel productLabel = new JLabel("Product ID");

        productLabel.setPreferredSize(new Dimension(100, 50));
        productLabel.setHorizontalAlignment(JLabel.RIGHT);

        labProductName.setPreferredSize(new Dimension(350, 50));
        labProductName.setHorizontalAlignment(JLabel.LEFT);

        line2.add(productLabel);
        line2.add(txtProductID);
        line2.add(labProductName);
        line2.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        view.getContentPane().add(line2);

        //Customer ID line
        JPanel line3 = new JPanel(new FlowLayout());
        JLabel customerLabel = new JLabel("Customer ID");

        customerLabel.setPreferredSize(new Dimension(100, 50));
        customerLabel.setHorizontalAlignment(JLabel.RIGHT);

        labCustomerName.setPreferredSize(new Dimension(350, 50));
        labCustomerName.setHorizontalAlignment(JLabel.LEFT);

        line3.add(customerLabel);
        line3.add(txtCustomerID);
        line3.add(labCustomerName);
        line3.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        view.getContentPane().add(line3);

        //Quantity line
        JPanel line4 = new JPanel(new FlowLayout());
        JLabel quantLabel = new JLabel("Quantity");

        quantLabel.setPreferredSize(new Dimension(100, 50));
        quantLabel.setHorizontalAlignment(JLabel.RIGHT);

        labPrice.setPreferredSize(new Dimension(350, 50));
        labPrice.setHorizontalAlignment(JLabel.LEFT);

        line4.add(quantLabel);
        line4.add(txtQuantity);
        line4.add(labPrice);
        line4.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        view.getContentPane().add(line4);

        //Cost and tax line
        JPanel line5 = new JPanel(new FlowLayout());
        line5.add(labCost);
        line5.add(labTax);
        line5.add(labTotalCost);
        line5.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        view.getContentPane().add(line5);

        //Check if field has been filled
        txtProductID.addFocusListener(new ProductIDFocusListener());
        txtCustomerID.addFocusListener(new CustomerIDFocusListener());
        txtQuantity.addFocusListener(new QuantityChangeListener());

        btnSave.addActionListener(new SaveButtonListener());
        btnCancel.addActionListener(new CancelButtonListener());
    }

    class SaveButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String id  = txtPurchaseID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "Purchase ID cannot be null.");
                return;
            }

            try {
                purchase.mPurchaseID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Purchase ID is invalid.");
                return;
            }

            Gson gson = new Gson();
            MessageModel msg = new MessageModel();
            msg.code = MessageModel.PUT_PURCHASE;
            msg.data = gson.toJson(purchase);

            SocketNetworkAdapter net = new SocketNetworkAdapter();

            try {
                msg = net.exchange(msg, "localhost", 8080);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (msg.code == MessageModel.OPERATION_FAILED) {
                JOptionPane.showMessageDialog(null, "Purchase not added successfully.");
            } else {
                JOptionPane.showMessageDialog(null, "Purchase added successfully.");
                TxtReceiptBuilder receipt = new TxtReceiptBuilder();

                receipt.appendHeader("Welcome to the Store!");
                receipt.appendCustomer(customer);
                receipt.appendProduct(product);
                receipt.appendPurchase(purchase);
                receipt.appendFooter("Thanks for shopping with us!");

                try {
                    BufferedWriter file = new BufferedWriter(new FileWriter("/Users/Connor/Desktop/current-classes/modeling_and_design/Project2/Receipt.txt"));
                    file.write(receipt.getReceipt());
                    JOptionPane.showMessageDialog(null, "Receipt sent.");
                    file.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Receipt error.");
                }
            }

            turnOff();
        }
    }

    public void run() {
        purchase = new PurchaseModel();
        purchase.mDate = Calendar.getInstance().getTime().toString();
        labDate.setText(String.format("Purchase Date: %s", purchase.mDate));
        view.setVisible(true);
    }

    public void turnOff() {
        view.dispose();
    }

    private class ProductIDFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent focusEvent) {

        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            process();
        }

        private void process() {
            String s = txtProductID.getText();
            product = new ProductModel();

            if (s.length() == 0) {
                labProductName.setText("Product Name: [not specified]");
                return;
            }

            System.out.println(String.format("Product ID = %s", s));

            try {
                purchase.mProductID = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Invalid Product ID", "Error Message", JOptionPane.ERROR_MESSAGE);
                return;
            }

            product.mProductID = Integer.parseInt(s);
            Gson gson = new Gson();
            MessageModel msg = new MessageModel();
            msg.code = MessageModel.GET_PRODUCT;
            msg.data = gson.toJson(product);

            SocketNetworkAdapter net = new SocketNetworkAdapter();

            try {
                msg = net.exchange(msg, "localhost", 8080);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (msg.code == MessageModel.OPERATION_FAILED) {
                labProductName.setText("Product Name: [does not exist]");
            } else {
                product = gson.fromJson(msg.data, ProductModel.class);
                if (product.mName.equals("null")) {
                    labProductName.setText("Product Name: [does not exist]");
                    return;
                }
                labProductName.setText(String.format("Product Name: %s", product.mName));
                purchase.mPrice = product.mPrice;
                labPrice.setText(String.format("Product Price: %s", product.mPrice));
            }
        }
    }

    private class CustomerIDFocusListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent focusEvent) {

        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            process();
        }

        private void process() {
            String s = txtCustomerID.getText();
            customer = new CustomerModel();

            if (s.length() == 0) {
                labCustomerName.setText("Customer Name: [not specified]");
                return;
            }

            System.out.println(String.format("CustomerID = %s", s));

            try {
                purchase.mCustomerID = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Invalid Customer ID", "Error Message", JOptionPane.ERROR_MESSAGE);
                return;
            }

            customer.mCustomerID = Integer.parseInt(s);
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
                labCustomerName.setText("Customer Name: [does not exist]");
            } else {
                customer = gson.fromJson(msg.data, CustomerModel.class);
                if (customer.mName.equals("null")) {
                    labCustomerName.setText("Customer Name: [does not exist]");
                    return;
                }

                labCustomerName.setText(String.format("Customer Name: %s", customer.mName));
            }
        }
    }

    private class QuantityChangeListener implements FocusListener {
        @Override
        public void focusGained(FocusEvent focusEvent) {

        }

        @Override
        public void focusLost(FocusEvent focusEvent) {
            process();
        }

        private void process() {
            String s = txtQuantity.getText();

            System.out.println(String.format("Quantity = %s", s));

            try {
                purchase.mQuantity = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Invalid Quantity", "Error Message", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (purchase.mQuantity <= 0) {
                JOptionPane.showMessageDialog(null, "Error: Invalid Quantity", "Error Message", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (purchase.mQuantity > product.mQuantity) {
                JOptionPane.showMessageDialog(null, "Error: Not Enough Available Products", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            purchase.mCost = purchase.mQuantity * product.mPrice;
            purchase.mTax = purchase.mCost * 0.09;
            purchase.mTotal = purchase.mCost + purchase.mTax;

            labCost.setText(String.format("Cost: $%.2f", purchase.mCost).trim());
            labTax.setText(String.format("Tax: $%.2f", purchase.mTax).trim());
            labTotalCost.setText(String.format("Total: $%.2f", purchase.mTotal).trim());
        }
    }

    class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            turnOff();
        }
    }
}
