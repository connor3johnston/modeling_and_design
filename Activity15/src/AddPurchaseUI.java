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
    public MainUI mainView;

    public JButton btnAdd = new JButton("Add");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtPurchaseID = new JTextField(20);
    public JTextField txtProductID = new JTextField(20);
    public JTextField txtCustomerID = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);

    public JLabel labPrice = new JLabel("Product Price: ");
    public JLabel labDate = new JLabel("Date of Purchase");
    public JLabel labCustomerName = new JLabel("Customer Name: ");
    public JLabel labProductName = new JLabel("Product Name: ");
    public JLabel labCost = new JLabel("Cost: $0.00");
    public JLabel labTax = new JLabel("Tax: $0.00");
    public JLabel labTotalCost = new JLabel("Total Cost: $0.00");

    ProductModel product;
    CustomerModel customer;
    PurchaseModel purchase;

    public IDataAdapter adapter;

    public AddPurchaseUI(IDataAdapter adapter, MainUI mainView) {
        this.view = new JFrame();
        this.adapter = adapter;
        this.mainView = mainView;

        view.setTitle("Add Purchase");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JPanel line1 = new JPanel(new FlowLayout());
        line1.add(new JLabel("Purchase ID "));
        line1.add(txtPurchaseID);
        line1.add(labDate);
        view.getContentPane().add(line1);

        JPanel line2 = new JPanel(new FlowLayout());
        line2.add(new JLabel("Product ID "));
        line2.add(txtProductID);
        line2.add(labProductName);
        view.getContentPane().add(line2);

        JPanel line3 = new JPanel(new FlowLayout());
        line3.add(new JLabel("Customer ID "));
        line3.add(txtCustomerID);
        line3.add(labCustomerName);
        view.getContentPane().add(line3);

        JPanel line4 = new JPanel(new FlowLayout());
        line4.add(new JLabel("Quantity "));
        line4.add(txtQuantity);
        line4.add(labPrice);
        view.getContentPane().add(line4);

        JPanel line5 = new JPanel(new FlowLayout());
        line5.add(labCost);
        line5.add(labTax);
        line5.add(labTotalCost);
        view.getContentPane().add(line5);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnAdd);
        panelButtons.add(btnCancel);
        view.getContentPane().add(panelButtons);

        txtProductID.addFocusListener(new ProductIDFocusListener());
        txtCustomerID.addFocusListener(new CustomerIDFocusListener());
        txtQuantity.addFocusListener(new QuantityChangeListener());

        btnAdd.addActionListener(new AddButtonListener());
        btnCancel.addActionListener(new CancelButtonListener());
    }

    class AddButtonListener implements ActionListener {

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

            switch (adapter.savePurchase(purchase)) {
                case SQLiteDataAdapter.DUPLICATE_ERROR:
                    JOptionPane.showMessageDialog(null, "Purchase NOT added successfully. Duplicate Purchase ID.");
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Purchase added successfully.");
                    TxtReceiptBuilder receipt = new TxtReceiptBuilder();

                    receipt.appendHeader("Welcome to the Store!");
                    receipt.appendCustomer(customer);
                    receipt.appendProduct(product);
                    receipt.appendPurchase(purchase);
                    receipt.appendFooter("Thanks for shopping with us!");

                    try {
                        BufferedWriter file = new BufferedWriter(new FileWriter("/Users/Connor/Desktop/modeling_and_design/Activity12/Receipt.txt"));
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

    class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            turnOff();
        }
    }

    public void run() {
        purchase = new PurchaseModel();
        purchase.mDate = Calendar.getInstance().getTime().toString();
        labDate.setText(String.format("Date of Purchase: %s", purchase.mDate));
        view.setVisible(true);
    }

    public void turnOff() {
        view.dispose();
        mainView.run();
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

            product = adapter.loadProduct(purchase.mProductID);

            if (product == null) {
                JOptionPane.showMessageDialog(null, String.format("Error: No Corresponding Product [id = %s]", purchase.mProductID), "Error Message", JOptionPane.ERROR_MESSAGE);
                labProductName.setText("Product Name: ");
                return;
            }

            labProductName.setText(String.format("Product Name: %s", product.mName));
            purchase.mPrice = product.mPrice;
            labPrice.setText(String.format("Product Price: %s", product.mPrice));
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

            customer = adapter.loadCustomer(purchase.mCustomerID);

            if (customer == null) {
                JOptionPane.showMessageDialog(null, String.format("Error: No Corresponding Customer [id = %s]", purchase.mCustomerID), "Error Message", JOptionPane.ERROR_MESSAGE);
                labCustomerName.setText("Customer Name: ");
                return;
            }

            labCustomerName.setText(String.format("Product Name: %s", customer.mName));
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
}