import javax.jws.soap.SOAPBinding;
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
            return null;
        }

        return product;
    }

    public int saveCustomer(CustomerModel customer) {
        try {
            Statement stmt = connection.createStatement();

            CustomerModel c = loadCustomer(customer.mCustomerID);

            if (c != null) {
                stmt.executeUpdate("DELETE FROM Customers WHERE CustomerID = " + customer.mCustomerID);
            }

            String sql = "INSERT INTO Customers(CustomerID, Name, Address, Phone, PaymentInfo) VALUES" + customer;
            System.out.println(sql);
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
            return null;
        }
        return product;
    }

    public int saveProduct(ProductModel product) {
        try {
            Statement stmt = connection.createStatement();

            ProductModel p = loadProduct(product.mProductID);

            if (p != null) {
                stmt.executeUpdate("DELETE FROM Products WHERE ProductID = " + product.mProductID);
            }

            String sql = "INSERT INTO Products(ProductID, Name, Price, Quantity) VALUES " + product;
            System.out.println(sql);
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

    public PurchaseModel loadPurchase(int purchaseID) {
        PurchaseModel purchase = new PurchaseModel();

        try {
            String sql = "SELECT PurchaseID, ProductID, CustomerID, Price, Quantity, Cost, Tax, Date " +
                    "FROM Purchases WHERE PurchaseID = " + purchaseID;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            purchase.mProductID = rs.getInt("ProductId");
            purchase.mCustomerID = rs.getInt("CustomerId");
            purchase.mPrice = rs.getInt("Price");
            purchase.mQuantity = rs.getInt("Quantity");
            purchase.mCost = rs.getInt("Cost");
            purchase.mTax = rs.getInt("Tax");
            purchase.mDate = rs.getString("Date");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }

        return purchase;
    }

    public int savePurchase(PurchaseModel purchase) {
        try {
            Statement stmt = connection.createStatement();

            PurchaseModel p = loadPurchase(purchase.mPurchaseID);

            if (p != null) {
                stmt.executeUpdate("DELETE FROM Purchases WHERE PurchaseID = " + purchase.mPurchaseID);
            }

            String sql = "INSERT INTO Purchases VALUES " + purchase;
            System.out.println(sql);
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

    public UserModel loadUser(String username) {
        UserModel user = new UserModel();

        try {
            String sql = "SELECT * FROM Users WHERE Username = \"" + username + "\"";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                user = new UserModel();
                user.mUsername = username;
                user.mPassword = rs.getString("Password");
                user.mFullname = rs.getString("FullName");
                user.mUserType = rs.getInt("UserType");
                if (user.mUserType == UserModel.CUSTOMER) {
                    user.mCustomerID = rs.getInt("CustomerID");
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return user;
    }

    public int saveUser(UserModel user) {
        try {
            Statement stmt = connection.createStatement();

            UserModel u = loadUser(user.mUsername);

            if (u != null) {
                stmt.executeUpdate("DELETE FROM Users WHERE Username = '" + u.mUsername + "'");
            }

            String sql = "INSERT INTO Users VALUES " + user;
            System.out.println(sql);
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

    public PurchaseListModel loadPurchaseHistory(int id) {
        PurchaseListModel res = new PurchaseListModel();
        try {
            String sql = "SELECT * FROM Purchases WHERE CustomerId = " + id;
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PurchaseModel purchase = new PurchaseModel();
                purchase.mCustomerID = id;
                purchase.mPurchaseID = rs.getInt("PurchaseID");
                purchase.mProductID = rs.getInt("ProductID");
                purchase.mPrice = rs.getDouble("Price");
                purchase.mQuantity = rs.getInt("Quantity");
                purchase.mCost = rs.getDouble("Cost");
                purchase.mTax = rs.getDouble("Tax");
                purchase.mTotal = rs.getDouble("Total");
                purchase.mDate = rs.getString("Date");

                res.purchases.add(purchase);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return res;
    }

    public ProductListModel loadProductList(String name) {
        ProductListModel res = new ProductListModel();
        try {
            String sql = "SELECT * FROM Products WHERE Name = '" + name + "'";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                ProductModel product = new ProductModel();
                product.mProductID = rs.getInt("ProductID");
                product.mName = name;
                product.mPrice = rs.getDouble("Price");
                product.mQuantity = rs.getInt("Quantity");

                res.products.add(product);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return res;
    }

    public PurchaseListModel loadFullPurchaseHistory() {
        PurchaseListModel res = new PurchaseListModel();
        try {
            String sql = "SELECT * FROM Purchases";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                PurchaseModel purchase = new PurchaseModel();
                purchase.mCustomerID = rs.getInt("CustomerID");
                purchase.mPurchaseID = rs.getInt("PurchaseID");
                purchase.mProductID = rs.getInt("ProductID");
                purchase.mPrice = rs.getDouble("Price");
                purchase.mQuantity = rs.getInt("Quantity");
                purchase.mCost = rs.getDouble("Cost");
                purchase.mTax = rs.getDouble("Tax");
                purchase.mTotal = rs.getDouble("Total");
                purchase.mDate = rs.getString("Date");

                res.purchases.add(purchase);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return res;
    }
}
