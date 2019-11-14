import com.sun.org.apache.xml.internal.security.utils.JDKXPathAPI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ManageProductUI {

    public JFrame view;
    public MainUI mainView;

    public JButton btnLoad = new JButton("Load Product");
    public JButton btnSave = new JButton("Save Product");
    public JButton btnCancel = new JButton("Cancel");

    public JTextField txtProductID = new JTextField(20);
    public JTextField txtName = new JTextField(20);
    public JTextField txtPrice = new JTextField(20);
    public JTextField txtQuantity = new JTextField(20);


    public ManageProductUI(MainUI mainView) {
        this.view = new JFrame();
        this.mainView = mainView;

        //View configuration
        view.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        view.setTitle("Manage Product Information");
        view.setSize(600, 400);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        //Buttons
        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnLoad);
        panelButtons.add(btnSave);
        panelButtons.add(btnCancel);
        panelButtons.setAlignmentX(JPanel.CENTER_ALIGNMENT);
        view.getContentPane().add(panelButtons);

        //Product ID line
        JPanel line1 = new JPanel(new FlowLayout());
        JLabel idLabel = new JLabel("Product ID");
        idLabel.setPreferredSize(new Dimension(100, 50));
        idLabel.setHorizontalAlignment(JLabel.RIGHT);
        line1.add(idLabel);
        line1.add(txtProductID);
        view.getContentPane().add(line1);

        //Name line
        JPanel line2 = new JPanel(new FlowLayout());
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setPreferredSize(new Dimension(100, 50));
        nameLabel.setHorizontalAlignment(JLabel.RIGHT);
        line2.add(nameLabel);
        line2.add(txtName);
        view.getContentPane().add(line2);

        //Price line
        JPanel line3 = new JPanel(new FlowLayout());
        JLabel priceLabel = new JLabel("Price");
        priceLabel.setPreferredSize(new Dimension(100, 50));
        priceLabel.setHorizontalAlignment(JLabel.RIGHT);
        line3.add(priceLabel);
        line3.add(txtPrice);
        view.getContentPane().add(line3);

        //Quantity line
        JPanel line4 = new JPanel(new FlowLayout());
        JLabel quantLabel = new JLabel("Quantity");
        quantLabel.setPreferredSize(new Dimension(100, 50));
        quantLabel.setHorizontalAlignment(JLabel.RIGHT);
        line4.add(quantLabel);
        line4.add(txtQuantity);
        view.getContentPane().add(line4);

        btnLoad.addActionListener(new LoadButtonListener());
        btnSave.addActionListener(new SaveButtonListener());
        btnCancel.addActionListener(new CancelButtonListener());
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
            ProductModel product = new ProductModel();
            String id = txtProductID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "ProductID cannot be null.");
                return;
            }

            try {
                product.mProductID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ProductID is invalid.");
                return;
            }

            try {
                Socket link = new Socket("localhost", 8080);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                output.println("GET");
                output.println(product.mProductID);

                product.mName = input.nextLine();

                if (product.mName.equals("null")) {
                    JOptionPane.showMessageDialog(null, "Product does not exist.");
                    return;
                }

                txtName.setText(product.mName);

                product.mPrice = input.nextDouble();
                txtPrice.setText(Double.toString(product.mPrice));

                product.mQuantity = input.nextDouble();
                txtQuantity.setText(Double.toString(product.mQuantity));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ProductModel product = new ProductModel();
            String id = txtProductID.getText();

            if (id.length() == 0) {
                JOptionPane.showMessageDialog(null, "ProductID cannot be null.");
                return;
            }

            try {
                product.mProductID = Integer.parseInt(id);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "ProductID is invalid.");
                return;
            }

            String name = txtName.getText();
            if (name.length() == 0) {
                JOptionPane.showMessageDialog(null, "Product name cannot be empty.");
                return;
            }

            product.mName = name;

            String price = txtPrice.getText();
            try {
                product.mPrice = Double.parseDouble(price);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Price is invalid.");
                return;
            }

            String quant = txtQuantity.getText();
            try {
                product.mQuantity = Double.parseDouble(quant);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Quantity is invalid.");
                return;
            }

            try {
                Socket link = new Socket("localhost", 8080);
                Scanner input = new Scanner(link.getInputStream());
                PrintWriter output = new PrintWriter(link.getOutputStream(), true);

                output.println("PUT");
                output.println(product.mProductID);
                output.println(product.mName);
                output.println(product.mPrice);
                output.println(product.mQuantity);

                JOptionPane.showMessageDialog(null, "Product information updated successfully.");
                turnOff();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class CancelButtonListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            turnOff();
        }
    }
}
