// sidebar.js
window.onload = function() {
    // Get the role from session storage
    const userRole = 'manager';
    
    // Set the sidebar content dynamically based on the role
    const sidebar = document.getElementById("sidebar");
    const sidebarLinks = document.getElementById("sidebar-links");

    // Create Employee's Sidebar Links
    const employeeLinks = `
    <li><a href="/employeeDashboard.html" onclick="loadAccount()">Dashboard</a></li>
        <li><a href="#account" onclick="loadAccount()">Accounts</a></li>
        <li><a href="/transfer.html" onclick="loadTransfer()">Transfer Amount</a></li>
        <li><a href="/transaction.html" onclick="loadTransaction()">Transactions</a></li>
        <li><a href="#profile" onclick="loadProfile()">Profile</a></li>
        <li><a href="#logout" onclick="logout()">Logout</a></li>
    `;

    // Create Manager's Sidebar Links
    const managerLinks = `
    <li><a href="/employeeDashboard.html" onclick="loadDashboard()">Dashboard</a></li>
    <li><a href="#employee-management" onclick="loadEmployeeManagement()">Employee Management</a></li>
    <li><a href="/transfer.html" onclick="loadTransfer()">Transfer Amount</a></li>
    <li><a href="/transaction.html" onclick="loadTransaction()">Transactions</a></li>
    <li><a href="#analytics" onclick="loadAnalytics()">Analytics</a></li>
    <li><a href="#profile" onclick="loadProfile()">Profile</a></li>
    <li><a href="#logout" onclick="logout()">Logout</a></li>
`;


    // Dynamically set sidebar links based on role
    if (userRole === 'employee') {
        sidebarLinks.innerHTML = employeeLinks;
    } else if (userRole === 'manager') {
        sidebarLinks.innerHTML = managerLinks;
    }
};

// Logout function
function logout() {
    sessionStorage.removeItem("role");  // Clear role from sessionStorage
    window.location.href = '/login';  // Redirect to login page
}

// Function to handle loading content dynamically (for example purposes)
function loadAccount() { console.log('Account page'); }
function loadTransfer() { console.log('Transfer Amount page'); }
function loadTransaction() { console.log('Transaction page'); }
function loadProfile() { console.log('Profile page'); }
function loadEmployees() { console.log('Employee list page'); }
