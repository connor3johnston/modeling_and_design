import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteDataAdapter implements IDataAdapter {

    Connection connection = null;

    @Override
    public int connect(String dbfile) {
        try {
            String url = "jdbc:sqlite:" + dbfile;
            connection = DriverManager.getConnection(url);

            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_OPEN_FAIL;
        }

        return CONNECTION_OPEN_OK;
    }

    @Override
    public int disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return CONNECTION_CLOSE_FAIL;
        }

        return CONNECTION_CLOSE_OK;
    }

    public CustomerModel loadCustomer(int customerID) {
        CustomerModel product = new CustomerModel();

        try {
            String sql = "SELECT CustomerID, Name, Address, Phone, PaymentInfo FROM Customers WHERE CustomerID = " + customerID;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            product.mCustomerID = rs.getInt("CustomerID");
            product.mName = rs.getString("Name");
            product.mAddress = rs.getString("Address");
            product.mPhone = rs.getString("Phone");
            product.mPaymentInfo = rs.getString("PaymentInfo");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return product;
    }

    public int saveCustomer(CustomerModel customer) {
        try {
            String sql = "INSERT INTO Customers(CustomerID, Name, Address, Phone, PaymentInfo) VALUES" + customer;
            System.out.println(sql);
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed")) {
                return DUPLICATE_ERROR;
            }
        }

        return SAVED_OK;
    }

    public ProductModel loadProduct(int productID) {
        ProductModel product = new ProductModel();

        try {
            String sql = "SELECT ProductID, Name, Price, Quantity FROM Products WHERE ProductId = " + productID;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            product.mProductID = rs.getInt("ProductId");
            product.mName = rs.getString("Name");
            product.mPrice = rs.getDouble("Price");
            product.mQuantity = rs.getDouble("Quantity");


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return product;
    }

    public int saveProduct(ProductModel product) {
        try {
            String sql = "INSERT INTO Products(ProductID, Name, Price, Quantity) VALUES " + product;
            System.out.println(sql);
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);

        } catch (Exception e) {
            String msg = e.getMessage();
            System.out.println(msg);
            if (msg.contains("UNIQUE constraint failed"))
                return DUPLICATE_ERROR;
        }

        return SAVED_OK;
    }
}
