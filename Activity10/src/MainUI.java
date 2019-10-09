import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainUI {
    JFrame view;

    public JButton btnAddProduct = new JButton("Add Product");
    public JButton btnAddCustomer = new JButton("Add Customer");
    public JButton btnExit = new JButton("Exit");

    IDataAdapter adapter;

    public MainUI(IDataAdapter adapter) {
        this.view = new JFrame();
        this.adapter = adapter;

        view.setTitle("Store Management System");
        view.setSize(1000, 600);
        view.getContentPane().setLayout(new BoxLayout(view.getContentPane(), BoxLayout.PAGE_AXIS));

        JLabel title = new JLabel("Welcome to the Store Manager!");

        title.setFont(title.getFont().deriveFont(24.0f));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setVerticalAlignment(SwingConstants.CENTER);
        view.getContentPane().add(title);

        JPanel panelButtons = new JPanel(new FlowLayout());
        panelButtons.add(btnAddProduct);
        panelButtons.add(btnAddCustomer);
        panelButtons.add(btnExit);
        view.getContentPane().add(panelButtons);

        btnAddProduct.addActionListener(new AddProductListener());
        btnAddCustomer.addActionListener(new AddCustomerListener());
        btnExit.addActionListener(new ExitListener());
    }

    class AddProductListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            AddProductUI apUI = new AddProductUI(adapter, new MainUI(adapter));

            turnOff();
            apUI.run();
        }
    }

    class AddCustomerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            AddCustomerUI acUI = new AddCustomerUI(adapter, new MainUI(adapter));

            turnOff();
            acUI.run();
        }
    }

    class ExitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            System.exit(1);
        }
    }

    public void run() {
        view.setVisible(true);
    }

    public void turnOff() {
        view.setVisible(false);
    }
}
