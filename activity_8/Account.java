public class Account {

  protected static String owner;
  protected static double balance;
  protected static double monthlyDepositAmount;
  protected static double monthlyWithdrawalAmount;

  public Account(double initialBalance, String owner) {
    this.owner = owner;
    balance = initialBalance;
  }

  public double getBalance() {
    return balance;
  }

  public String getOwner() {
    return owner;
  }

  public void deposit(double depositAmount) {
    if (depositAmount < 0) {
     throw new IllegalArgumentException("Invalid amount: deposit amount cannot be negative");
    }

    monthlyDepositAmount += depositAmount;
    balance += depositAmount;
  }

  public void withdraw(double withdrawalAmount) {
    if (withdrawalAmount < 0) {
      throw new IllegalArgumentException("Invalid amount: withdrawal amount cannot be negative");
    }

    monthlyWithdrawalAmount += withdrawalAmount;
    balance -= withdrawalAmount;
  }

  public void resetMonthlyDepositAmount() {
    monthlyDepositAmount = 0;
  }

  public void resetMonthlyWithdrawalAmount() {
    monthlyWithdrawalAmount = 0;
  }
}
