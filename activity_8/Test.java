public class Test {
  public static void main(String[] args) {
    CheckingAccount test1 = new CheckingAccount(50, "Connor", 35);

    test1.withdraw(60);

    System.out.println(String.format("The balance of Test 1 is %s.", test1.getBalance()));

    SavingsAccount test2 = new SavingsAccount(60, "Time", 5, 5, 100);
    test2.deposit(100);

    for (int x = 0; x < 10; x++) {
      test2.withdraw(10);
    }

    test2.chargeMonthlyFee();
    test2.withdraw(100);

    System.out.println(String.format("The balance of Test 2 is %s.", test2.getBalance()));

  }
}
