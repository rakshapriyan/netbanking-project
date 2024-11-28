// Base URL for API
const baseURL = "http://localhost:8080/Netbanking1";

// Set default Axios headers for authorization
axios.defaults.headers.common['Authorization'] = sessionStorage.getItem('token');

// Fetch and display user profile details
async function fetchProfileDetails() {
    try {
        const response = await axios.get(`${baseURL}/profile`);
        const userData = response.data;

        // Populate profile details
        document.getElementById('userId').value = userData.userId;
        document.getElementById('username').value = userData.username;
        document.getElementById('name').value = userData.name;
        document.getElementById('phoneNumber').value = userData.phoneNumber;
        document.getElementById('emailId').value = userData.emailId;
        document.getElementById('aadharNumber').value = userData.aadharNumber;
        document.getElementById('panNumber').value = userData.panNumber;
        document.getElementById('address').value = userData.address;
        document.getElementById('city').value = userData.city;
        document.getElementById('state').value = userData.state;
        document.getElementById('pincode').value = userData.pincode;
    } catch (error) {
        console.error("Error fetching profile details:", error);
        alert("Failed to load profile details. Please try again later.");
    }
}

// Fetch and populate account details dropdown
async function fetchAccountDetails() {
    try {
        const response = await axios.get(`${baseURL}/accounts`);
        const accounts = response.data;

        const accountSelect = document.getElementById('account-select');
        accounts.forEach(account => {
            const option = document.createElement('option');
            option.value = account.accountNumber;
            option.textContent = `Account ${account.accountNumber}`;
            accountSelect.appendChild(option);
        });
    } catch (error) {
        console.error("Error fetching account details:", error);
        alert("Failed to load account details. Please try again later.");
    }
}

// Load account details for the selected account
function loadAccountDetails() {
    const accountNumber = parseInt(document.getElementById('account-select').value, 10); // Convert to number
    const accountDetailsDiv = document.getElementById('account-details');

    axios
        .get(`${baseURL}/accountDetails`)
        .then(response => {
            const accounts = response.data; // Response data is an array of accounts
            console.log(response.data);
            const account = accounts.find(acc => acc.accountNumber === accountNumber);

            if (account) {
                const formattedDate = new Date(account.dateOpened).toLocaleDateString(); // Format date
                accountDetailsDiv.innerHTML = `
                    <p><strong>Account Number:</strong> ${account.accountNumber}</p>
                    <p><strong>Balance:</strong> ₹${account.balance.toFixed(2)}</p> <!-- Format balance -->
                    <p><strong>Status:</strong> ${account.accountStatus}</p>
                    <p><strong>Date Opened:</strong> ${formattedDate}</p>
                    <p><strong>Account Holder Name:</strong> ${account.name}</p>
                    <p><strong>IFSC Code:</strong> ${account.ifscCode}</p>
                    <p><strong>Address:</strong> ${account.city}, ${account.state}, ${account.pincode}</p>
                `;
            } else {
                accountDetailsDiv.innerHTML = `<p>No details found for the selected account.</p>`;
            }
        })
        .catch(error => {
            console.error("Error loading account details:", error);
            alert("Failed to load account details. Please try again later.");
        });
}


function enableEdit() {
    const inputs = document.querySelectorAll('#customer-form input');
    const submitBtn = document.getElementById('submit-btn');
    inputs.forEach(input => input.disabled = false);
    submitBtn.disabled = false;
}

function showSection(sectionId) {
    // Hide all sections
    const profileSection = document.getElementById('profile-section');
    const accountsSection = document.getElementById('accounts-section');

    profileSection.style.display = 'none';
    accountsSection.style.display = 'none';

    // Show the selected section
    if (sectionId === 'profile') {
        profileSection.style.display = 'block';
    } else if (sectionId === 'accounts') {
        accountsSection.style.display = 'block';
    }
}

// Load profile and account details on page load
window.onload = function () {
    fetchProfileDetails();
    fetchAccountDetails();
};
