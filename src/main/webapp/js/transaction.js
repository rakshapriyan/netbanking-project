document.addEventListener("DOMContentLoaded", () => {
    const filterButton = document.getElementById("filterButton");
    const filterModal = document.getElementById("filterModal");
    const closeModal = document.getElementById("closeModal");
    const applyFilters = document.getElementById("applyFilters");

    const axiosInstance = axios.create({
        baseURL: 'http://localhost:8080/Netbanking1',
        timeout: 5000,
        headers: {
            'Content-Type': 'application/json',
            Authorization: sessionStorage.getItem('token'),
        },
    });

    // Open and close modal functionality
    const openModal = () => {
        filterModal.style.display = "block";
        filterModal.querySelector(".filters").style.animation = "filtersFadeIn 0.8s ease-in-out";
    };

    const closeModalWithAnimation = () => {
        filterModal.style.animation = "fadeOut 0.4s ease forwards";
        setTimeout(() => {
            filterModal.style.display = "none";
            filterModal.style.animation = "fadeIn 0.5s ease forwards";
        }, 400);
    };

    filterButton.addEventListener("click", openModal);
    closeModal.addEventListener("click", closeModalWithAnimation);
    window.addEventListener("click", (event) => {
        if (event.target === filterModal) closeModalWithAnimation();
    });

    // Apply Filters
    applyFilters.addEventListener("click", () => {
        const filters = getFilters();
        fetchTransactions(filters);
        closeModalWithAnimation();
    });

    // Fetch Transactions from Backend using Axios (POST request with filters as JSON object)
    const fetchTransactions = async (filters = {}) => {
        try {
            const response = await axiosInstance.post('/transactions', filters); // Send filters as the request body
            populateTransactionTable(response.data); // Display data in table
        } catch (error) {
            console.error("Error fetching transactions:", error);
            alert("There was an error fetching the transaction data.");
        }
    };

    // Populate transaction table with data
    const populateTransactionTable = (transactions) => {
        const tableBody = document.getElementById("transactionBody");
        tableBody.innerHTML = "";
        transactions.forEach((transaction) => {
            const row = document.createElement("tr");
            row.innerHTML = `
                <td>${transaction.transactionId}</td>
                <td>${new Date(transaction.timestamp).toLocaleDateString()}</td>
                <td>${transaction.transactionType}</td>
                <td>${transaction.amount}</td>
                <td>${transaction.status}</td>
            `;
            tableBody.appendChild(row);
        });
    };

    // Collect filter data
    const getFilters = () => {
        return {
            search: document.getElementById("searchBar").value || null,
            startDate: document.getElementById("startDate").value || null,
            endDate: document.getElementById("endDate").value || null,
            minAmount: parseFloat(document.getElementById("minAmount").value) || null,
            maxAmount: parseFloat(document.getElementById("maxAmount").value) || null,
            type: document.getElementById("typeFilter").value || null,
            status: document.getElementById("statusFilter").value || null,
        };
    };

    // Fetch initial data on load
    fetchTransactions();
});


// Handle "Download Transactions" button click
document.getElementById("downloadButton").addEventListener("click", () => {
    const tableBody = document.getElementById("transactionBody");
    const rows = Array.from(tableBody.getElementsByTagName("tr"));

    // Prepare data for Excel
    const data = [["Transaction ID", "Date", "Type", "Amount", "Status"]]; // Header row
    rows.forEach(row => {
        const cells = Array.from(row.getElementsByTagName("td"));
        const rowData = cells.map(cell => cell.textContent);
        data.push(rowData);
    });

    // Convert data to a worksheet
    const worksheet = XLSX.utils.aoa_to_sheet(data);

    // Create a workbook and append the worksheet
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, "Transactions");

    // Write the workbook to a file and trigger download
    XLSX.writeFile(workbook, "transactions.xlsx");
});


// Sample data for accounts (this can be fetched from an API)
const accounts = [
    "Account 1",
    "Account 2",
    "Account 3",
    "Account 4",
    "Account 5"
];

// Function to filter accounts based on input and show dropdown
function filterAccounts() {
    const input = document.getElementById('searchInput').value.toLowerCase();
    const dropdownMenu = document.getElementById('dropdownMenu');
    dropdownMenu.innerHTML = ''; // Clear previous dropdown items

    if (input) {
        const filteredAccounts = accounts.filter(account => account.toLowerCase().includes(input));
        if (filteredAccounts.length > 0) {
            dropdownMenu.style.display = 'block';
            filteredAccounts.forEach(account => {
                const item = document.createElement('li');
                item.textContent = account;
                item.classList.add('dropdown-item');
                item.onclick = () => {
                    document.getElementById('searchInput').value = account; // Set input value on click
                    dropdownMenu.style.display = 'none'; // Hide dropdown after selection
                };
                dropdownMenu.appendChild(item);
            });
        } else {
            dropdownMenu.style.display = 'none';
        }
    } else {
        dropdownMenu.style.display = 'none';
    }
}

filterAccounts()
