// Global Variables
let sampleData = [];
let customerIDs = [];

window.onload = function () {
    // URL for the API call
    const apiUrl = 'http://localhost:8080/Netbanking1/customerDetails';

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
            <td>${account.userId}</td>
            <td>$${account.username}</td>
            <td>${account.name}</td>
            <td>${account.emailId}</td>
            <td>${account.userStatus}</td>
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
    document.getElementById('customerId').value = account.userId;
    document.getElementById('username').value = account.username;
    document.getElementById('password').value = account.password;
    document.getElementById('name').value = account.name;
    document.getElementById('phoneNumber').value = account.phoneNumber;
    document.getElementById('emailId').value = account.emailId;
    document.getElementById('createdTimeStamp').value = new Date(account.createdTimeStamp);
    document.getElementById('aadharNumber').value = account.aadharNumber;
    document.getElementById('panNumber').value = account.panNumber;
    document.getElementById('address').value = account.address;
    document.getElementById('city').value = account.city;
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



function createAccount() {
    // Get the form data
    const formData = {
        username: document.getElementById('username').value,
        password: document.getElementById('password').value,
        name: document.getElementById('name').value,
        phoneNumber: document.getElementById('phoneNumber').value,
        emailId: document.getElementById('emailId').value,
        aadharNumber: document.getElementById('aadharNumber').value,
        panNumber: document.getElementById('panNumber').value,
        address: document.getElementById('address').value,
        city: document.getElementById('city').value,
        state: document.getElementById('state').value,
        pincode: document.getElementById('pincode').value
    };

    // Send POST request to the server using Axios
    axios.post('/api/create-account', formData)
        .then(function(response) {
            // Handle success
            alert('Account created successfully!');
            console.log(response.data); // You can display more details here if needed
            closeCreateModal(); // Close the modal after successful account creation
        })
        .catch(function(error) {
            // Handle error
            alert('Error creating account. Please try again.');
            console.error(error);
        });
}

// Enable edit mode for the form fields
function enableEdit() {
    // Get all input fields in the form
    const form = document.getElementById('accountDetailsForm');
    const inputs = form.querySelectorAll('input');
    
    // Enable input fields except for the disabled ones (e.g., Customer ID)
    inputs.forEach(input => {
        if (input.id !== 'customerId') { // Example: Keep Customer ID disabled
            input.disabled = false;
        }
    });

    // Show the Submit button and hide the Edit button
    document.getElementById('edit-btn').style.display = 'none';
    document.getElementById('submit-btn').style.display = 'inline-block';
}

// Submit the edited account details
function submitDetails() {
    // Gather the data from the form
    const form = document.getElementById('accountDetailsForm');
    const formData = {};
    const inputs = form.querySelectorAll('input');
    inputs.forEach(input => {
        formData[input.name] = input.value;
    });

    // Perform validation (if required)
    if (!formData.name || !formData.emailId) {
        alert('Name and Email ID are required.');
        return;
    }

    // Submit the data (example using Axios)
    axios.put('http://localhost:8080/Netbanking1//customerDetails', formData)
        .then(response => {
            alert('Account details updated successfully!');
            closeModal(); // Close the modal after submission
        })
        .catch(error => {
            console.error(error);
            alert('Failed to update account details.');
        });

    // Disable the fields again
    inputs.forEach(input => input.disabled = true);

    // Show the Edit button and hide the Submit button
    document.getElementById('edit-btn').style.display = 'inline-block';
    document.getElementById('submit-btn').style.display = 'none';
}

// Close the modal
function closeModal() {
    document.getElementById('detailsModal').style.display = 'none';
}

// Function to deactivate the account (implement this based on your requirements)
function deactivateAccount() {
    const confirmDeactivation = confirm("Are you sure you want to deactivate this account?");
    if (confirmDeactivation) {
        // Implement deactivation logic here (e.g., calling an API)
        alert("Account deactivated.");
    }
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