// Global Variables
let sampleData = [];
let customerIDs = [];

window.onload = function () {
    // URL for the API call
    const apiUrl = 'http://localhost:8080/Netbanking1/accountDetails';

    // Authorization token (replace with your actual token)
    const token = '1:Alice Manager:manager';

    // Using Axios to make a GET request with headers
    axios.get(apiUrl, {
        headers: {
            Authorization: token
        }
    })
        .then(function (response) {
            // Handle success
            sampleData = response.data;
            console.log(sampleData);
            customerIDs = sampleData.map(account => `CUST${account.accountNumber}`);
            populateAccounts();
        })
        .catch(function (error) {
            // Handle error
            console.error('Error fetching data:', error);
            document.getElementById('accountTable').innerHTML = 
                '<tr><td colspan="4">Error fetching data. Check console for details.</td></tr>';
        });
};

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
    document.getElementById('filterModal').style.display = 'block';
}

// Close filter modal
function closeFilterModal() {
    document.getElementById('filterModal').style.display = 'none';
}


// Show account details in a modal
function showAccountDetails(account) {
    document.getElementById('accountNumber').value = account.accountNumber;
    document.getElementById('balance').value = account.balance;
    document.getElementById('status').value = account.accountStatus;
    document.getElementById('dateOpened').value = new Date(account.dateOpened);
    document.getElementById('ifscCode').value = account.ifscCode;
    document.getElementById('city').value = account.city;
    document.getElementById('state').value = account.state;
    document.getElementById('pincode').value = account.pincode;

    document.getElementById('detailsModal').style.display = 'block';
}

// Close the details modal
function closeModal() {
    document.getElementById('detailsModal').style.display = 'none';
}

// Create Account Button Logic
function showCreateModal() {
    document.getElementById('createAccountModal').style.display = 'block';
}

function closeCreateModal() {
    document.getElementById('createAccountModal').style.display = 'none';
    document.getElementById('customerId').value = '';
    document.getElementById('customerDropdown').style.display = 'none';
}



async function createAccount() {
    const customerId = document.getElementById('customerId').value;
    const branchId = document.getElementById('BranchId').value;
    const initialBalance = document.getElementById('initialDeposit').value;
    const apiUrl = 'http://localhost:8080/Netbanking1/accounts';

    if (!customerId || !branchId || !initialBalance) {
        alert('Please fill in all fields!');
        return;
    }

    // Data to be sent in the POST request
    const accountData = {
        customerId: customerId,
        branchId: branchId,
        initialBalance: initialBalance
    };

    try {
        const response = await axios.post(apiUrl, accountData, {
            headers: {
                'Content-Type': 'application/json',
                'Authorization' : '1:Alice Manager:manager'
            }
        });

        alert(`Account created successfully: ${response.data.message}`);
    } catch (error) {
        if (error.response) {
            // Server responded with a status code outside 2xx
            alert(`Error: ${error.response.data.message}`);
        } else {
            // Network or other error
            console.error('Error while creating account:', error);
            alert('Failed to create account. Please try again.');
        }
    }

    closeCreateModal();
}

// Close the modal when clicking outside
window.onclick = function (event) {
    const filterModal = document.getElementById('filterModal');
    const detailsModal = document.getElementById('detailsModal');
    if (event.target === filterModal) {
        filterModal.style.display = 'none';
    } else if (event.target === detailsModal) {
        detailsModal.style.display = 'none';
    }
};
