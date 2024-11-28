drop database netbanking; 
create database netbanking; 
use netbanking;


-- 1. Create the user table
CREATE TABLE user (
    user_id BIGINT PRIMARY KEY AUTO_INCREMENT UNIQUE,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    phone_number BIGINT,
    email_id VARCHAR(255),
    role VARCHAR(50),
    is_online BOOLEAN,
    user_status VARCHAR(50),
    created_by BIGINT,
    created_timestamp BIGINT,
    last_modified_by BIGINT,
    last_modified_timestamp BIGINT
);

-- 2. Create the branch table
CREATE TABLE branch (
    branch_id BIGINT PRIMARY KEY AUTO_INCREMENT UNIQUE,
    name VARCHAR(255) NOT NULL,
    ifsc_code VARCHAR(20),
    manager_id BIGINT,
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    pincode INT,
    created_by BIGINT,
    created_timestamp BIGINT,
    modified_by BIGINT,
    modified_timestamp BIGINT
);

-- 3. Create the employee table
CREATE TABLE employee (
    employee_id BIGINT PRIMARY KEY AUTO_INCREMENT UNIQUE,
    branch_id BIGINT,
    FOREIGN KEY (employee_id) REFERENCES user(user_id),
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id)
);

-- Add foreign key constraints to the branch table after creating employee

-- 4. Create the customer table
CREATE TABLE customer (
    customer_id BIGINT PRIMARY KEY UNIQUE,
    aadhar_number BIGINT ,
    pan_number VARCHAR(50) ,
    address VARCHAR(255),
    city VARCHAR(100),
    state VARCHAR(100),
    pincode INT,
    FOREIGN KEY (customer_id) REFERENCES user(user_id)
);

-- 5. Create the account table
CREATE TABLE account (
    account_number BIGINT PRIMARY KEY AUTO_INCREMENT UNIQUE,
    customer_id BIGINT,
    branch_id BIGINT,
    balance DECIMAL(15,2),
    account_status VARCHAR(50),
    date_opened BIGINT,
    date_closed BIGINT NULL,
    created_by BIGINT,
    created_timestamp BIGINT,
    last_modified_by BIGINT,
    last_modified_timestamp BIGINT,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id),
    FOREIGN KEY (branch_id) REFERENCES branch(branch_id),
    FOREIGN KEY (created_by) REFERENCES employee(employee_id),
    FOREIGN KEY (last_modified_by) REFERENCES employee(employee_id)
);

-- 6. Create the transaction table
CREATE TABLE transaction (
    transaction_id BIGINT PRIMARY KEY AUTO_INCREMENT UNIQUE,
    transaction_number BIGINT,
    account_number BIGINT,
    transaction_account_number BIGINT NULL,
    transaction_type VARCHAR(50),
    amount DECIMAL(15,2),
    timestamp BIGINT,
    status VARCHAR(50),
    description VARCHAR(255),
    created_by BIGINT,
    created_timestamp BIGINT,
    FOREIGN KEY (account_number) REFERENCES account(account_number),
    FOREIGN KEY (transaction_account_number) REFERENCES account(account_number),
    FOREIGN KEY (created_by) REFERENCES user(user_id)
);

-- 7. Create the login_activity table
CREATE TABLE login_activity (
    login_id BIGINT PRIMARY KEY AUTO_INCREMENT UNIQUE,
    user_id BIGINT,
    login_timestamp BIGINT,
    logout_timestamp BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(user_id)
);
