package bank_system;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp đại diện cho Ngân hàng quản lý danh sách khách hàng.
 */
public class Bank {
  private static final Logger logger = LoggerFactory.getLogger(Bank.class);
  private static final String ID_REGEX = "\\d{9}";

  private List<Customer> customerList;

  public Bank() {
    this.customerList = new ArrayList<>();
  }

  public List<Customer> getCustomerList() {
    return customerList;
  }

  /**
   * Thiết lập danh sách khách hàng.
   *
   * @param customerList Danh sách khách hàng mới
   */
  public void setCustomerList(List<Customer> customerList) {
    if (customerList == null) {
      this.customerList = new ArrayList<>();
    } else {
      this.customerList = customerList;
    }
  }

  /**
   * Đọc danh sách khách hàng và tài khoản từ InputStream.
   *
   * @param inputStream Luồng dữ liệu đầu vào
   */
  public void readCustomerList(InputStream inputStream) {
    logger.debug("Bắt đầu đọc dữ liệu khách hàng từ InputStream...");
    if (inputStream == null) {
      return;
    }

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      String line;
      Customer currentCustomer = null;

      while ((line = reader.readLine()) != null) {
        line = line.trim();
        if (line.isEmpty()) {
          continue;
        }

        currentCustomer = processLineAndGetCustomer(line, currentCustomer);
      }
    } catch (Exception e) {
      logger.error("Lỗi khi đọc dữ liệu: {}", e.getMessage(), e);
    }
  }

  private Customer processLineAndGetCustomer(String line, Customer currentCustomer) {
    int lastSpaceIndex = line.lastIndexOf(' ');
    if (lastSpaceIndex > 0) {
      String token = line.substring(lastSpaceIndex + 1).trim();
      
      if (token.matches(ID_REGEX)) {
        String name = line.substring(0, lastSpaceIndex).trim();
        Customer newCustomer = new Customer(Long.parseLong(token), name);
        customerList.add(newCustomer);
        logger.debug("Thêm khách hàng: {}", name);
        return newCustomer;
      } else if (currentCustomer != null) {
        processAccountInfo(line, currentCustomer);
      }
    }
    return currentCustomer;
  }

  private void processAccountInfo(String line, Customer currentCustomer) {
    String[] parts = line.split("\\s+");
    if (parts.length >= 3) {
      long accNum = Long.parseLong(parts[0]);
      double balance = Double.parseDouble(parts[2]);
      if (Account.CHECKING_TYPE.equals(parts[1])) {
        currentCustomer.addAccount(new CheckingAccount(accNum, balance));
      } else if (Account.SAVINGS_TYPE.equals(parts[1])) {
        currentCustomer.addAccount(new SavingsAccount(accNum, balance));
      }
    }
  }

  /**
   * Lấy thông tin khách hàng sắp xếp theo ID.
   *
   * @return Chuỗi thông tin danh sách khách hàng
   */
  public String getCustomersInfoByIdOrder() {
    List<Customer> copyList = new ArrayList<>(customerList);
    copyList.sort(Comparator.comparingLong(Customer::getIdNumber));
    return buildCustomerInfoString(copyList);
  }

  /**
   * Lấy thông tin khách hàng sắp xếp theo Tên.
   *
   * @return Chuỗi thông tin danh sách khách hàng
   */
  public String getCustomersInfoByNameOrder() {
    List<Customer> copyList = new ArrayList<>(customerList);
    copyList.sort((c1, c2) -> {
      int res = c1.getFullName().compareTo(c2.getFullName());
      return res != 0 ? res : Long.compare(c1.getIdNumber(), c2.getIdNumber());
    });
    return buildCustomerInfoString(copyList);
  }

  private String buildCustomerInfoString(List<Customer> sortedList) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < sortedList.size(); i++) {
      sb.append(sortedList.get(i).getCustomerInfo());
      if (i < sortedList.size() - 1) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }
}