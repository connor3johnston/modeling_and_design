public class CheckingAccount extends Account {

  private static double overdraftFee;

  public CheckingAccount(double initialBalance, String owner, double overdraftFee) {
    super(initialBalance, owner);

    this.overdraftFee = overdraftFee;
  }

  @Override
  public void withdraw(double withdrawalAmount) {
    super.withdraw(withdrawalAmount);

    if (balance < 0) {
      balance -= overdraftFee;
    }
  }
}
