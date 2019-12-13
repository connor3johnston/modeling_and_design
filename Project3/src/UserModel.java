public class UserModel {
    public static final int ADMIN = 0;
    public static final int MANAGER = 1;
    public static final int CASHIER = 2;
    public static final int CUSTOMER = 3;


    public String mUsername, mPassword, mFullname;
    public int mUserType, mCustomerID;

    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        sb.append("\"").append(mUsername).append("\"").append(",");
        sb.append("\"").append(mPassword).append("\"").append(",");
        sb.append("\"").append(mFullname).append("\"").append(",");
        sb.append(mUserType).append(",");
        sb.append(mCustomerID).append(")");
        return sb.toString();
    }
}