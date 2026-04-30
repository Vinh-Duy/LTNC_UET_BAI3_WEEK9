package bank_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Tài khoản tiết kiệm.
 */
public class SavingsAccount extends Account {
  private static final Logger logger = LoggerFactory.getLogger(SavingsAccount.class);
  private static final double MAX_WITHDRAW = 1000.0;
  private static final double MIN_BALANCE = 5000.0;

  public SavingsAccount(long accountNumber, double balance) {
    super(accountNumber, balance);
  }

  @Override
  public void deposit(double amount) {
    logger.debug("Đang xử lý giao dịch nạp tiền tiết kiệm...");
    double initialBalance = getBalance();
    try {
      doDepositing(amount);
      double finalBalance = getBalance();
      Transaction t = new Transaction(
          Transaction.TYPE_DEPOSIT_SAVINGS,
          amount,
          initialBalance,
          finalBalance);
      addTransaction(t);
      logger.info("Nạp tiền vào tài khoản {} thành công: +{}", getAccountNumber(), amount);
    } catch (BankException e) {
      logger.error("Lỗi nạp tiền tiết kiệm: {}", e.getMessage(), e);
    }
  }

  @Override
  public void withdraw(double amount) {
    logger.debug("Đang xử lý giao dịch rút tiền tiết kiệm...");
    double initialBalance = getBalance();
    try {
      if (amount > MAX_WITHDRAW) {
        throw new InvalidFundingAmountException(amount);
      }
      if (initialBalance - amount < MIN_BALANCE) {
        throw new InsufficientFundsException(amount);
      }

      doWithdrawing(amount);
      double finalBalance = getBalance();

      Transaction t = new Transaction(
          Transaction.TYPE_WITHDRAW_SAVINGS,
          amount,
          initialBalance,
          finalBalance);
      addTransaction(t);
      logger.info("Rút tiền tiết kiệm thành công. Số dư còn: {}", finalBalance);
    } catch (BankException e) {
      logger.error("Lỗi rút tiền tiết kiệm: {}", e.getMessage(), e);
    }
  }
}