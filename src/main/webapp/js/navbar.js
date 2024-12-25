// Define navigation links for different roles
const customerLinks = [
    { text: "Dashboard", href: "/dashboard.html" },
    { text: "Transfer", href: "/transfer.html" },
    { text: "Transactions", href: "/transaction.html" },
];

const employeeLinks = [
    { text: "Dashboard", href: "/employeeDashboard.html" },
    { text: "Transfer", href: "/transfer.html" },
    { text: "Transactions", href: "/transaction.html" },
    { text: "Accounts", href: "/account.html" }
];

const managerLinks = [
    { text: "Dashboard", href: "/employeeDashboard.html" },
    { text: "Transfer", href: "/transfer.html" },
    { text: "Transactions", href: "/transaction.html" },
    { text: "Accounts", href: "/account.html" },
    { text: "Customers", href: "/profile.html" }
];

// Get user role from session storage
const role = sessionStorage.getItem('role');

// Select the appropriate links based on the role
let navLinks = [];
if (role === 'customer') {
    navLinks = customerLinks;
} else if (role === 'employee') {
    navLinks = employeeLinks;
} else if (role === 'manager') {
    navLinks = managerLinks;
}

// Function to show logout alert and redirect
// Function to show logout alert and redirect
function logout() {
    // Clear both session and local storage
    sessionStorage.clear();
    localStorage.clear();

    // Show an alert message
    alert("You have been logged out.");

    // Redirect to the login page
    window.location.href = "/login.html";
}


// Function to show movie title animation
function showMovieTitle(name, href) {
    const titleDiv = document.createElement("div");
    titleDiv.classList.add("movie-title");
    titleDiv.textContent = name;

    // Add blur effect to the background
    document.body.classList.add("blurred");

    document.body.appendChild(titleDiv);

    setTimeout(() => {
        titleDiv.remove();
        document.body.classList.remove("blurred"); // Remove blur after animation
        window.location.href = href; // Navigate to the target page
    }, 2000); // Duration of the animation
}

// Function to build the navigation bar
function buildNavbar(links) {
    const navContainer = document.getElementById("nav-links");
    navContainer.innerHTML = ""; // Clear existing links

    // Create navigation links
    links.forEach((link) => {
        const navItem = document.createElement("li");
        const navLink = document.createElement("a");
        navLink.href = link.href;
        navLink.classList.add("nav-link");
        navLink.textContent = link.text;

        navLink.addEventListener("click", (event) => {
            event.preventDefault(); // Prevent default navigation
            showMovieTitle(link.text, link.href);
        });

        navItem.appendChild(navLink);
        navContainer.appendChild(navItem);
    });

    // Add profile section with dropdown
    const profileItem = document.createElement("li");
    profileItem.classList.add("profile");

    const profileLink = document.createElement("a");
    profileLink.href = "#";
    profileLink.textContent = "Profile";
    profileLink.classList.add("profile-link");

    const dropdown = document.createElement("ul");
    dropdown.classList.add("dropdown");
    dropdown.innerHTML = `
        <li><a href="/profile.html" class="dropdown-item">Account Details</a></li>
        <li><a href="#" id="logout-btn" class="dropdown-item">Logout</a></li>
    `;

    profileItem.appendChild(profileLink);
    profileItem.appendChild(dropdown);
    navContainer.appendChild(profileItem);

    // Attach logout functionality
    document.getElementById("logout-btn").addEventListener("click", (e) => {
        e.preventDefault();
        logout();
        showMovieTitle("Logging Out", "/login.html");
    });
}

// Initialize the navbar when the document is fully loaded
document.addEventListener("DOMContentLoaded", () => {
    buildNavbar(navLinks);
});



