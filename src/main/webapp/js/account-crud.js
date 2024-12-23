const sampleData = [
    { accountNumber: 123456, name: 'John Doe', balance: 5000, accountStatus: 'Active', dateOpened: '2022-01-15', ifscCode: 'ABC123', city: 'New York', state: 'NY', pincode: 10001 },
    { accountNumber: 654321, name: 'Jane Smith', balance: 3000, accountStatus: 'Inactive', dateOpened: '2020-06-10', ifscCode: 'XYZ456', city: 'Los Angeles', state: 'CA', pincode: 90001 },
    { accountNumber: 112233, name: 'Alice Brown', balance: 7000, accountStatus: 'Active', dateOpened: '2019-05-05', ifscCode: 'LMN789', city: 'Chicago', state: 'IL', pincode: 60601 },
    // More sample data...
];

// Populate accounts in the table
function populateAccounts() {
    const tableBody = document.querySelector('#accountTable tbody');
    tableBody.innerHTML = '';

    sampleData.forEach(account => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${account.accountNumber}</td>

            <td>$${account.balance.toLocaleString()}</td>
            <td>${account.accountStatus}</td>
            
            <td>${account.city}</td>
           
        `;
        row.addEventListener('click', () => showAccountDetails(account));
        tableBody.appendChild(row);
    });
}

// Show filter modal
function showFilterModal() {
    const filterModal = document.getElementById('filterModal');
    filterModal.style.display = 'block'; // Show the modal
}

// Close filter modal
function closeFilterModal() {
    const filterModal = document.getElementById('filterModal');
    filterModal.style.display = 'none'; // Hide the modal
}

// Apply filters and update table
function applyFilter() {
    const status = document.getElementById('status').value;
    const city = document.getElementById('city').value.toLowerCase();
    const state = document.getElementById('state').value.toLowerCase();

    const filteredData = sampleData.filter(account =>
        (!status || account.accountStatus === status) &&
        (city === '' || account.city.toLowerCase().includes(city)) &&
        (state === '' || account.state.toLowerCase().includes(state))
    );

    updateTable(filteredData);
    closeFilterModal(); // Close the modal after applying filters
}

// Update the table with filtered data
function updateTable(data) {
    const tableBody = document.querySelector('#accountTable tbody');
    tableBody.innerHTML = '';

    data.forEach(account => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${account.accountNumber}</td>
            <td>${account.name}</td>
            <td>$${account.balance.toLocaleString()}</td>
            <td>${account.accountStatus}</td>
            <td>${new Date(account.dateOpened).toLocaleDateString()}</td>
            <td>${account.ifscCode}</td>
            <td>${account.city}</td>
            <td>${account.state}</td>
            <td>${account.pincode}</td>
        `;
        row.addEventListener('click', () => showAccountDetails(account));
        tableBody.appendChild(row);
    });
}

// Show account details in a modal
// Function to show account details in the modal
function showAccountDetails(account) {
    // Populate the modal fields with account data
    document.getElementById('accountNumber').value = account.accountNumber;
    document.getElementById('holderName').value = account.holderName;
    document.getElementById('balance').value = account.balance;
    document.getElementById('status').value = account.status;
    document.getElementById('dateOpened').value = account.dateOpened;
    document.getElementById('ifscCode').value = account.ifscCode;
    document.getElementById('city').value = account.city;
    document.getElementById('state').value = account.state;
    document.getElementById('pincode').value = account.pincode;

    // Display the modal
    document.getElementById('detailsModal').style.display = 'block';
}

// Function to deactivate the account
function deactivateAccount() {
    const accountNumber = document.getElementById('accountNumber').value;
    if (confirm(`Are you sure you want to deactivate account ${accountNumber}?`)) {
        // Perform deactivation logic (e.g., API call)
        alert(`Account ${accountNumber} deactivated.`);
        closeModal();
    }
}


// Close the details modal
function closeModal() {
    document.getElementById('detailsModal').style.display = 'none';
}

// Close the modal when clicking outside
window.onclick = function(event) {
    const filterModal = document.getElementById('filterModal');
    const detailsModal = document.getElementById('detailsModal');
    if (event.target === filterModal || event.target === detailsModal) {
        filterModal.style.display = 'none';
        detailsModal.style.display = 'none';
    }
}

// Initialize the page
populateAccounts();


// Create Account Button Logic
function createAccount() {
    alert("Create Account functionality will be implemented.");
}

// Deactivate Account Logic
function deactivateAccount() {
    const accountNumber = document.getElementById('accountNumber').value;
    alert(`Account ${accountNumber} has been deactivated.`);
    closeModal();  // Close the modal after deactivating
}








// Mock data for customer IDs
const customerIDs = [
    "CUST001",
    "CUST002",
    "CUST003",
    "CUST004",
    "CUST005",
];

// Show Create Account Modal
function showCreateModal() {
    document.getElementById("createAccountModal").style.display = "block";
}

// Close Create Account Modal
function closeCreateModal() {
    document.getElementById("createAccountModal").style.display = "none";
    document.getElementById("customerId").value = "";
    document.getElementById("customerDropdown").style.display = "none";
}

// Filter Customer IDs
function filterCustomerIDs() {
    const input = document.getElementById("customerId").value.toUpperCase();
    const dropdown = document.getElementById("customerDropdown");

    // Clear previous suggestions
    dropdown.innerHTML = "";

    if (input) {
        const filtered = customerIDs.filter(id => id.toUpperCase().includes(input));
        dropdown.style.display = filtered.length ? "block" : "none";

        // Populate dropdown
        filtered.forEach(id => {
            const li = document.createElement("li");
            li.textContent = id;
            li.onclick = () => selectCustomerID(id);
            dropdown.appendChild(li);
        });
    } else {
        dropdown.style.display = "none";
    }
}

// Select Customer ID
function selectCustomerID(id) {
    document.getElementById("customerId").value = id;
    document.getElementById("customerDropdown").style.display = "none";
}

// Create Account Logic
function createAccount() {
    const customerId = document.getElementById("customerId").value;
    const accountNumber = document.getElementById("accountNumber").value;
    const holderName = document.getElementById("holderName").value;
    const initialBalance = document.getElementById("initialBalance").value;

    if (!customerId || !accountNumber || !holderName || !initialBalance) {
        alert("Please fill in all fields!");
        return;
    }

    // Example action on form submission
    alert(`Account created for Customer ID: ${customerId}`);
    closeCreateModal();
}
