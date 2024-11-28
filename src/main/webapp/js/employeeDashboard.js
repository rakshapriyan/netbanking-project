document.addEventListener('DOMContentLoaded', async function () {
    // Check for token and redirect to login if missing
    const token = sessionStorage.getItem('token');
    if (!token) {
        console.error('Authorization token is missing. Redirecting to login.');
        window.location.href = '/login.html';
        return;
    }

    console.log('Authorization token:', token);

    try {
        // Fetch data from the backend using axios with Authorization header
        const response = await axios.post('http://localhost:8080/Netbanking1/transactions', null, {
            headers: {
                'Authorization': token // Send token directly without 'Bearer' prefix
            }
        });

        const transactions = response.data;

        if (transactions.length === 0) {
            console.log('No transactions available for this week.');
            return;
        }

        // Variables to hold the data for chart and statistics
        const transactionsPerDay = new Array(7).fill(0);
        let totalCreditedAmount = 0;
        let totalDebittedAmount = 0;

        // Map to get the current date and find the corresponding index for each transaction day
        const currentDate = new Date();
        const daysOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'];

        transactions.forEach(transaction => {
            const transactionDate = new Date(transaction.timestamp);
            const dayIndex = (currentDate.getDay() + 7 - transactionDate.getDay()) % 7; // Adjusts for past days

            // Accumulate the total credited and debited amounts
            if (transaction.transactionType === 'credit') {
                totalCreditedAmount += transaction.amount;
            } else if (transaction.transactionType === 'debit') {
                totalDebittedAmount += transaction.amount;
            }

            // Increment the corresponding day's transaction count
            transactionsPerDay[dayIndex]++;
        });

        // Update the dashboard with the fetched data
        document.getElementById('totalCreditedAmount').textContent = totalCreditedAmount;
        document.getElementById('totalDebittedAmount').textContent = totalDebittedAmount;

        // Load the chart with data
        loadTransactionChart(transactionsPerDay);

        // Load recent transactions
        loadRecentTransactions(transactions);
    } catch (error) {
        console.error('Error fetching transaction data:', error);

        // Redirect to login page if unauthorized
        if (error.response && error.response.status === 401) {
            alert('Your session has expired. Please log in again.');
            sessionStorage.clear();
            window.location.href = '/login.html';
        }
    }

    // Function to create line chart
    function loadTransactionChart(data) {
        const ctx = document.getElementById('transactionChart').getContext('2d');

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'],
                datasets: [{
                    label: 'Transactions per Week',
                    data: data,
                    borderColor: '#0056b3',
                    fill: false,
                    tension: 0.1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                },
                animation: {
                    duration: 1500, // Duration of the animation (in milliseconds)
                    easing: 'easeInOutQuad', // Easing function for smooth animation
                    onComplete: function () {
                        console.log('Animation Complete');
                    },
                    delay: function (context) {
                        let delay = context.index * 200; // Adds 200ms delay for each point
                        return delay;
                    }
                }
            }
        });
    }

    // Function to load recent transactions in the table
    function loadRecentTransactions(transactions) {
        const tbody = document.getElementById('transactionList');
        transactions.forEach(transaction => {
            const row = document.createElement('tr');
            row.innerHTML = `
              <td>${transaction.transactionId}</td>
              <td>${transaction.transactionNumber}</td>
              <td>${new Date(transaction.timestamp).toLocaleDateString()}</td>
              <td>${transaction.amount}</td>
              <td>${transaction.transactionType.charAt(0).toUpperCase() + transaction.transactionType.slice(1)}</td>
              <td>${transaction.createdBy}</td>
              <td>${transaction.description}</td>
              <td>${transaction.status}</td>
            `;
            tbody.appendChild(row);
        });
    }
});

// Event listener to handle back navigation
window.addEventListener('popstate', function () {
    const token = sessionStorage.getItem('token');
    if (!token) {
        console.error('Authorization token is missing during back navigation. Redirecting to login.');
        window.location.href = '/login.html';
    }
});
