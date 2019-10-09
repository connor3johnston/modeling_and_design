public class SavingsAccount extends Account {

  private static int previousWithdrawals;
  private static int  maxWithdrawals;
  private static double depositAmountToWaiveFee;
  private static double monthlyFee;

  public SavingsAccount(double initialBalance, String owner, int maxWithdrawals, double monthlyFee, double depositAmountToWaiveFee) {
    super(initialBalance, owner);

    this.maxWithdrawals = maxWithdrawals;
    this.previousWithdrawals = 0;
    this.depositAmountToWaiveFee = depositAmountToWaiveFee;
    this.monthlyFee = monthlyFee;
  }

  public void chargeMonthlyFee() {
    if (monthlyDepositAmount >= depositAmountToWaiveFee) {
      System.out.println("Monthly fee waived.");
      return;
    }

    balance -= monthlyFee;
  }

  @Override
  public void withdraw(double withdrawalAmount) {
    if (balance - withdrawalAmount < 0) {
      System.out.println("Withdrawal could not be performed. This would cause an overdraft.");
      return;
    }

    if (previousWithdrawals >= maxWithdrawals) {
      System.out.println("You have hit your max withdrawals for the month.");
      return;
    }

    super.withdraw(withdrawalAmount);
    previousWithdrawals++;
  }
}
