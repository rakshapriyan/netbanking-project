// Sidebar Template
const sidebarHTML = `
<div class="sidebar" id="sidebar">
    <div class="sidebar-header" id="userName">John Doe</div>
    <div class="sidebar-content">
        <div class="details">
            <img src="https://via.placeholder.com/60" alt="User Image">
            <div id="userEmail">Email: john.doe@example.com</div>
            <div id="userPhone">Phone: +1234567890</div>
        </div>
        <div class="divider"></div>
        <div class="sidebar-links" id="navLinks"></div>
    </div>
    <div class="logout">
        <a href="#">Logout</a>
    </div>
</div>
`;

// Inject Sidebar HTML
document.getElementById('sidebarContainer').innerHTML = sidebarHTML;

const sidebar = document.getElementById('sidebar');
const profileIcon = document.getElementById('profileIcon');
const overlay = document.getElementById('overlay');

// Navigation links based on user type
const userType = sessionStorage.getItem('userType');
const navLinks = document.getElementById('navLinks');

const links = {
customer: [
    { name: 'Home', href: '#' },
    { name: 'Orders', href: '#' },
    { name: 'Support', href: '#' },
],
employee: [
    { name: 'Dashboard', href: '#' },
    { name: 'Tasks', href: '#' },
    { name: 'Reports', href: '#' },
],
manager: [
    { name: 'Dashboard', href: '#' },
    { name: 'Team', href: '#' },
    { name: 'Analytics', href: '#' },
],
};

// Populate links
if (userType && links[userType]) {
navLinks.innerHTML = links[userType].map(link => `<a href="${link.href}">${link.name}</a>`).join('');
} else {
navLinks.innerHTML = '<a href="#">Default Link</a>';
}

// Toggle Sidebar
profileIcon.addEventListener('click', () => {
if (sidebar.classList.contains('open')) {
    closeSidebar();
} else {
    openSidebar();
}
});

overlay.addEventListener('click', closeSidebar);

function openSidebar() {
sidebar.style.animation = 'slideIn 0.5s forwards';
sidebar.classList.add('open');
overlay.classList.add('show');
}

function closeSidebar() {
sidebar.style.animation = 'slideOut 0.5s forwards';
setTimeout(() => sidebar.classList.remove('open'), 500);
overlay.classList.remove('show');
}







// <!DOCTYPE html>
// <html lang="en">
// <head>
//     <meta charset="UTF-8">
//     <meta name="viewport" content="width=device-width, initial-scale=1.0">
//     <title>Navbar with Sidebar</title>
//     <style>
//         /* General Styles */
//         body {
//             font-family: Arial, sans-serif;
//             margin: 0;
//             padding: 0;
//             background-color: #f0f0f0;
//         }

//         .navbar {
//             display: flex;
//             justify-content: space-between;
//             align-items: center;
//             padding: 10px 20px;
//             background-color: #333;
//             color: white;
//             box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
//         }

//         .navbar ul {
//             list-style: none;
//             display: flex;
//             margin: 0;
//             padding: 0;
//         }

//         .navbar ul li {
//             margin-right: 15px;
//             position: relative;
//         }

//         .navbar ul li a {
//             color: white;
//             text-decoration: none;
//             font-size: 16px;
//         }

//         .profile-link:hover + .dropdown,
//         .dropdown:hover {
//             display: block;
//         }

//         .dropdown {
//             position: absolute;
//             top: 30px;
//             left: 0;
//             background: #444;
//             box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
//             display: none;
//             list-style: none;
//             padding: 10px 0;
//         }

//         .dropdown li {
//             padding: 5px 20px;
//         }

//         .dropdown li a {
//             color: white;
//             text-decoration: none;
//         }

//         .dropdown li a:hover {
//             color: #ffd700;
//         }
//     </style>
// </head>
// <body>

// <div class="navbar">
//     <div class="logo">My App</div>
//     <ul id="nav-links"></ul>
// </div>

// <!-- Sidebar Container -->
// <div id="sidebarContainer"></div>
// <div class="overlay" id="overlay"></div>

// <script>
//     // Sidebar Template
//     const sidebarHTML = `
//         <div class="sidebar" id="sidebar">
//             <div class="sidebar-header" id="userName">John Doe</div>
//             <div class="sidebar-content">
//                 <div class="details">
//                     <img src="https://via.placeholder.com/60" alt="User Image">
//                     <div id="userEmail">Email: john.doe@example.com</div>
//                     <div id="userPhone">Phone: +1234567890</div>
//                 </div>
//                 <div class="divider"></div>
//                 <div class="sidebar-links" id="navLinks"></div>
//             </div>
//             <div class="logout">
//                 <a href="#" onclick="logout()">Logout</a>
//             </div>
//         </div>
//     `;

//     // Inject Sidebar HTML
//     document.getElementById('sidebarContainer').innerHTML = sidebarHTML;

//     const sidebar = document.getElementById('sidebar');
//     const overlay = document.getElementById('overlay');

//     // Toggle Sidebar
//     document.body.addEventListener('click', (event) => {
//         if (event.target.classList.contains('profile-link')) {
//             openSidebar();
//         } else if (event.target === overlay) {
//             closeSidebar();
//         }
//     });

//     function openSidebar() {
//         sidebar.style.animation = 'slideIn 0.5s forwards';
//         sidebar.classList.add('open');
//         overlay.classList.add('show');
//     }

//     function closeSidebar() {
//         sidebar.style.animation = 'slideOut 0.5s forwards';
//         setTimeout(() => sidebar.classList.remove('open'), 500);
//         overlay.classList.remove('show');
//     }

//     // Populate Navbar
//     const role = sessionStorage.getItem('role');
//     const navLinks = document.getElementById("nav-links");

//     const customerLinks = [
//         { text: "Dashboard", href: "/dashboard.html" },
//         { text: "Transfer", href: "/transfer.html" },
//         { text: "Transactions", href: "/transaction.html" },
//     ];

//     const employeeLinks = [
//         { text: "Dashboard", href: "/employeeDashboard.html" },
//         { text: "Transfer", href: "/transfer.html" },
//         { text: "Transactions", href: "/transaction.html" },
//         { text: "Accounts", href: "/account.html" },
//     ];

//     const managerLinks = [
//         { text: "Dashboard", href: "/employeeDashboard.html" },
//         { text: "Transfer", href: "/transfer.html" },
//         { text: "Transactions", href: "/transaction.html" },
//         { text: "Accounts", href: "/account.html" },
//         { text: "Customers", href: "/profile.html" },
//     ];

//     const links = {
//         customer: customerLinks,
//         employee: employeeLinks,
//         manager: managerLinks,
//     };

//     (links[role] || []).forEach(link => {
//         const li = document.createElement("li");
//         const a = document.createElement("a");
//         a.href = link.href;
//         a.textContent = link.text;
//         li.appendChild(a);
//         navLinks.appendChild(li);
//     });

//     // Add Profile Link
//     const profileLi = document.createElement("li");
//     const profileA = document.createElement("a");
//     profileA.href = "#";
//     profileA.textContent = "Profile";
//     profileA.classList.add("profile-link");
//     profileLi.appendChild(profileA);
//     navLinks.appendChild(profileLi);

//     // Logout Functionality
//     function logout() {
//         sessionStorage.clear();
//         localStorage.clear();
//         alert("You have been logged out.");
//         window.location.href = "/login.html";
//     }
// </script>
// </body>
// </html>
