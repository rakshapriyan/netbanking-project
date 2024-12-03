// Function to fetch accounts from the server with authorization using axios
async function fetchAccounts() {
  try {
    const response = await axios.get('http://localhost:8080/Netbanking1/accounts', {
      headers: {
        'Authorization': sessionStorage.getItem('token'), // Replace 'YOUR_ACCESS_TOKEN' with your actual token
        'Content-Type': 'application/json'
      }
    });

    // Assuming the response data is in the response.data property
    const accounts = response.data;

    // Populate the dropdown with the received account data
    populateDropdown(accounts);
  } catch (error) {
    console.error('Error fetching accounts:', error);
  }
}

// Function to populate the dropdown with account data
function populateDropdown(accounts) {
  const dropdownMenu = document.getElementById("account-dropdown");
  dropdownMenu.innerHTML = ''; // Clear any existing items before appending new ones

  accounts.forEach((account) => {
    const listItem = document.createElement("li");
    listItem.textContent = `Account ${account.accountNumber}`;
    listItem.addEventListener("click", () => selectAccount(account));
    dropdownMenu.appendChild(listItem);
  });
}

// Call fetchAccounts to load data on page load
fetchAccounts();

// Show dropdown when input is focused
function showDropdown() {
  const dropdownMenu = document.getElementById("account-dropdown");
  dropdownMenu.classList.add("show");
}

// Hide dropdown when clicking outside
document.addEventListener("click", (e) => {
  if (!e.target.closest(".dropdown-container")) {
    const dropdownMenu = document.getElementById("account-dropdown");
    dropdownMenu.classList.remove("show");
  }
});

// Display selected account details
function selectAccount(account) {
  document.getElementById("account-id").value = account.accountNumber;
  document.getElementById("account-status").value = account.accountStatus;
  document.getElementById("branch-id").value = account.branchId;
  document.getElementById("date-opened").value = new Date(account.dateOpened).toLocaleDateString();
  document.getElementById("balance").value = account.balance;
  document.getElementById("created-by").value = account.createdBy;
  document.getElementById("created-timestamp").value = new Date(account.createdTimestamp).toLocaleString();
  const dropdownMenu = document.getElementById("account-dropdown");
  dropdownMenu.classList.remove("show");
}

// Filter dropdown items based on input
function filterDropdown() {
  const input = document.getElementById("account-search").value.toLowerCase();
  const items = document.querySelectorAll("#account-dropdown li");

  items.forEach((item) => {
    const text = item.textContent.toLowerCase();
    item.style.display = text.includes(input) ? "block" : "none";
  });
}
