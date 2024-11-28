package com.banking.BankingApplication;

import java.sql.SQLException;

import com.banking.databaseOperations.DBService;
import com.banking.entity.Customer;

public class BankingApplication {

    public static void main(String[] args) throws SQLException {
        Customer customer = new Customer();

        // Using different data for the Customer object
        customer.setUserId(10005L);
        customer.setUsername("john_doe");
        customer.setPassword("securepass123");
        customer.setName("John Doe");
        customer.setPhoneNumber(9876543210L);
        customer.setEmailId("john.doe@example.com");
        customer.setRole("customer");
        customer.setIsOnline(true);
        customer.setUserStatus("active");
        customer.setCreatedBy(5L);
        customer.setCreatedTimestamp(1697023100L);
        customer.setLastModifiedBy(5L);
        customer.setLastModifiedTimestamp(1697023100L);

        customer.setCustomerId(10005L);
        customer.setAadharNumber(987654321234L);
        customer.setPanNumber("ABCD1234E");
        customer.setAddress("789 Pine Street");
        customer.setCity("New York");
        customer.setState("New York");
        customer.setPincode(10001);

        System.out.println("Customer details added successfully.");

        // Initialize DBService to handle database operations
//        DBService dbService = new DBService("/home/raksh-pt7616/Downloads/Zoho/Zoho-Training-main/Project/src/mapping.yaml");
//        Criteria c = new Criteria();
//        c.setColumn("customerId");
//        c.setCompareOperator(">=");
//        c.setValue("1");
//        List<Criteria> criteria = new ArrayList<Criteria>();
//        criteria.add(c);
//        dbService.get(Customer.class, criteria);
//        System.out.print(dbService.get(Customer.class, criteria));
//

        DBService db = new DBService("/home/raksh-pt7616/Downloads/Zoho/Zoho-Training-main/Project/src/mapping.yaml");
//        db.performDelete("Customer", "customerId",  ">=", "1");
//
//        DBService db1 = new DBService("/home/raksh-pt7616/Downloads/Zoho/Zoho-Training-main/Project/src/mapping.yaml");
//        Map<String, String> map = new HashMap<String, String>();
//        map.put("userStatus", "inactive");
//        db1.performInsertOrUpdate("Customer", map);
        db.insert(customer);

    }
}
