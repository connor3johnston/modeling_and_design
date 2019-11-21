import com.google.gson.Gson;

import java.io.PrintWriter;
import java.net.*;
import java.util.Scanner;

public class StoreServer {
    static String dbfile = "/Users/Connor/Desktop/current-classes/modeling_and_design/Project2/data/store.db";

    public static void main(String[] args) {

        int port = 8080;

        if (args.length > 0) {
            System.out.println("Running arguments: ");
            for (String arg : args)
                System.out.println(arg);
            port = Integer.parseInt(args[0]);
            dbfile = args[1];
        }

        try {
            SQLiteDataAdapter adapter = new SQLiteDataAdapter();
            Gson gson = new Gson();
            adapter.connect(dbfile);

            ServerSocket server = new ServerSocket(port);
            System.out.println("Server is listening at port = " + port);

            while (true) {
                Socket pipe = server.accept();
                PrintWriter out = new PrintWriter(pipe.getOutputStream(), true);
                Scanner in = new Scanner(pipe.getInputStream());

                MessageModel msg = gson.fromJson(in.nextLine(), MessageModel.class);
                if (msg.code == MessageModel.GET_CUSTOMER) {
                    String[] temp = msg.data.split(":");
                    temp = temp[1].split("}");
                    String data = temp[0];

                    System.out.println("GET customer with id = " + data);
                    CustomerModel c = adapter.loadCustomer(Integer.parseInt(data));

                    if (c == null) {
                        msg.code = MessageModel.OPERATION_FAILED;
                    } else {
                        msg.code = MessageModel.OPERATION_OK;
                        msg.data = gson.toJson(c);
                    }

                    out.println(gson.toJson(msg));
                } else if (msg.code == MessageModel.PUT_CUSTOMER) {
                    CustomerModel c = gson.fromJson(msg.data, CustomerModel.class);
                    System.out.println("PUT customer with customer = " + c);
                    int res = adapter.saveCustomer(c);
                    if (res == IDataAdapter.SAVED_OK) {
                        msg.code = MessageModel.OPERATION_OK;
                    } else {
                        msg.code = MessageModel.OPERATION_FAILED;
                    }

                    out.println(gson.toJson(msg));
                } else if (msg.code == MessageModel.GET_PRODUCT) {
                    String[] temp = msg.data.split(",");
                    temp = temp[0].split(":");
                    String data = temp[1];

                    System.out.println("GET product with id = " + data);
                    ProductModel p = adapter.loadProduct(Integer.parseInt(data));

                    if (p == null) {
                        msg.code = MessageModel.OPERATION_FAILED;
                    } else {
                        msg.code = MessageModel.OPERATION_OK;
                        msg.data = gson.toJson(p);
                    }

                    out.println(gson.toJson(msg));
                } else if (msg.code == MessageModel.PUT_PRODUCT) {
                    ProductModel p = gson.fromJson(msg.data, ProductModel.class);
                    System.out.println("PUT product with product = " + p);
                    int res = adapter.saveProduct(p);
                    if (res == IDataAdapter.SAVED_OK) {
                        msg.code = MessageModel.OPERATION_OK;
                    } else {
                        msg.code = MessageModel.OPERATION_FAILED;
                    }

                    out.println(gson.toJson(msg));
                } else if (msg.code == MessageModel.GET_PURCHASE) {
                    String[] temp = msg.data.split(",");
                    temp = temp[0].split(":");
                    String data = temp[1];

                    System.out.println("GET purchase with id = " + data);
                    PurchaseModel p = adapter.loadPurchase(Integer.parseInt(data));

                    if (p == null) {
                        msg.code = MessageModel.OPERATION_FAILED;
                    } else {
                        msg.code = MessageModel.OPERATION_OK;
                        msg.data = gson.toJson(p);
                    }

                    out.println(gson.toJson(msg));
                } else if (msg.code == MessageModel.PUT_PURCHASE) {
                    PurchaseModel p = gson.fromJson(msg.data, PurchaseModel.class);
                    System.out.println("PUT purchase with purchase = " + p);
                    int res = adapter.savePurchase(p);
                    if (res == IDataAdapter.SAVED_OK) {
                        msg.code = MessageModel.OPERATION_OK;
                    } else {
                        msg.code = MessageModel.OPERATION_FAILED;
                    }

                    out.println(gson.toJson(msg));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
