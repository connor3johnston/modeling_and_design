public class TxtReceiptBuilder implements IReceiptBuilder {
    StringBuilder sb = new StringBuilder();

    @Override
    public void appendHeader(String header) {
        sb.append(String.format("%s\n", header));
    }

    @Override
    public void appendCustomer(CustomerModel customer) {
        sb.append(String.format("Customer ID: %s\n", customer.mCustomerID));
        sb.append(String.format("Customer Name: %s\n", customer.mName));
    }

    @Override
    public void appendProduct(ProductModel product) {
        sb.append(String.format("Product ID: %s\n", product.mName));
        sb.append(String.format("Product Name: %s\n", product.mName));
    }

    @Override
    public void appendPurchase(PurchaseModel purchase) {
        sb.append(String.format("Quantity: %s\n", purchase.mQuantity));
        sb.append(String.format("Price: $%.2f\n", purchase.mPrice));
        sb.append(String.format("Tax: $%.2f\n", purchase.mTax));
        sb.append(String.format("Total Cost: $%.2f\n", purchase.mTotal));
    }

    @Override
    public void appendFooter(String footer) {
        sb.append(String.format("%s\n", footer));
    }

    public String getReceipt() {
        return sb.toString();
    }
}
