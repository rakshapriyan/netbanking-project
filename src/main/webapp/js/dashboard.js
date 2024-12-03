document.addEventListener("DOMContentLoaded", async () => {
  const token = sessionStorage.getItem("token");
  const role = sessionStorage.getItem("role");

  // Validate session and redirect based on role
  if (!token || !role) {
    console.error("Invalid session. Redirecting to login.");
    sessionStorage.clear();
    window.location.href = '/login.html';
    return; // Ensure no further code executes
  }

  if (role === "manager" || role === "employee") {
    console.log("Redirecting to employee dashboard.");
    window.location.href = '/employeeDashboard.html';
    return;
  }

  if (role !== "customer") {
    console.error("Unknown role. Redirecting to login.");
    sessionStorage.clear();
    window.location.href = '/login.html';
    return;
  }

  // If role is 'customer', continue to load the dashboard
  console.log("Welcome to the customer dashboard.");

  // DOM elements
  const accountDropdown = document.getElementById("accountDropdown");
  const transactionsTableBody = document.getElementById("transactionsTable").querySelector("tbody");
  const totalBalanceCard = document.getElementById("totalBalance");
  const totalCredited = document.getElementById("totalCredited");
  const totalDebited = document.getElementById("totalDebited");

  let accounts = [];
  let transactions = [];

  // Axios instance for making API calls
  const axiosInstance = axios.create({
    baseURL: "http://localhost:8080/Netbanking1",
    timeout: 5000,
    headers: {
      "Content-Type": "application/json",
      Authorization: token,
    },
  });

  // Function to fetch accounts and populate the dropdown and total balance
  const fetchAccounts = async () => {
    try {
      const response = await axiosInstance.get("/accounts");
      accounts = response.data;

      // Populate account dropdown
      accounts.forEach((account) => {
        const option = document.createElement("option");
        option.value = account.accountNumber;
        option.textContent = `Account ${account.accountNumber}`;
        accountDropdown.appendChild(option);
      });

      // Calculate and display total balance
      const totalBalance = accounts.reduce((sum, account) => sum + account.balance, 0).toFixed(2);
      totalBalanceCard.textContent = `$${totalBalance}`;
    } catch (error) {
      console.error("Error fetching accounts:", error);
      alert("Failed to load accounts. Please try again later.");
    }
  };

  // Function to fetch transactions
  const fetchTransactions = async () => {
    try {
      const response = await axiosInstance.post("/transactions");
      transactions = response.data;

      displayTransactions();
      calculateTotals();
    } catch (error) {
      console.error("Error fetching transactions:", error);
      alert("Failed to load transactions. Please try again later.");
    }
  };

  // Function to display transactions in the table
  const displayTransactions = (accountNumber = "all") => {
    transactionsTableBody.innerHTML = "";

    const filteredTransactions = accountNumber === "all"
      ? transactions
      : transactions.filter((transaction) => transaction.accountNumber === parseInt(accountNumber));

    if (filteredTransactions.length === 0) {
      transactionsTableBody.innerHTML = `<tr><td colspan="6" class="text-center">No transactions found</td></tr>`;
      return;
    }

    filteredTransactions.forEach((transaction, index) => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td>${index + 1}</td>
        <td>${new Date(transaction.timestamp).toLocaleDateString()}</td>
        <td>${transaction.description}</td>
        <td>${transaction.transactionType}</td>
        <td>$${transaction.amount.toFixed(2)}</td>
        <td>${transaction.status}</td>
      `;
      transactionsTableBody.appendChild(row);
    });
  };

  // Function to calculate totals for credited and debited transactions in the last 10 days
  const calculateTotals = () => {
    const tenDaysAgo = Date.now() - 10 * 24 * 60 * 60 * 1000;

    const creditedTotal = transactions
      .filter((transaction) => transaction.transactionType === "credit" && new Date(transaction.timestamp).getTime() >= tenDaysAgo)
      .reduce((sum, transaction) => sum + transaction.amount, 0);

    const debitedTotal = transactions
      .filter((transaction) => transaction.transactionType === "debit" && new Date(transaction.timestamp).getTime() >= tenDaysAgo)
      .reduce((sum, transaction) => sum + transaction.amount, 0);

    totalCredited.textContent = `$${creditedTotal.toFixed(2)}`;
    totalDebited.textContent = `$${debitedTotal.toFixed(2)}`;
  };

  // Function to download transactions as a CSV file
  const downloadTransactions = () => {
    const csvContent =
      "data:text/csv;charset=utf-8," +
      "Transaction Number,Date,Description,Type,Amount,Status\n" +
      transactions
        .map(
          (transaction) =>
            `${transaction.transactionNumber},${new Date(transaction.timestamp).toLocaleDateString()},${transaction.description},${transaction.transactionType},${transaction.amount},${transaction.status}`
        )
        .join("\n");

    const link = document.createElement("a");
    link.href = encodeURI(csvContent);
    link.download = "transactions.csv";
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
  };

  // Event listener for account dropdown change
  accountDropdown.addEventListener("change", () => {
    const selectedAccount = accountDropdown.value;
    displayTransactions(selectedAccount);
  });

  // Event listener for the download button
  document.getElementById("downloadBtn").addEventListener("click", downloadTransactions);

  // Fetch initial data
  await fetchAccounts();
  await fetchTransactions();
});
