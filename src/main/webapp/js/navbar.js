// Navigation Links Object
const navLinks = [
    { text: "Dashboard", href: "/dashboard.html" },
    { text: "Transfer", href: "/transfer.html" },
    { text: "Transactions", href: "/transaction.html" },
];

// Build Navbar
function buildNavbar(links) {
    const navContainer = document.getElementById("nav-links");
    navContainer.innerHTML = ""; // Clear existing links

    links.forEach((link, index) => {
        const navItem = document.createElement("li");
        const navLink = document.createElement("a");
        navLink.href = link.href;
        navLink.textContent = link.text;
        navLink.id = `nav-link-${index}`;
        navLink.addEventListener("click", handleNavigationClick);
        navItem.appendChild(navLink);
        navContainer.appendChild(navItem);
    });
}

// Handle Navigation Click
function handleNavigationClick(event) {
    event.preventDefault(); // Prevent immediate navigation
    const clickedText = event.target.textContent;
    const targetHref = event.target.href;

    // Show zoom animation
    const animationDiv = document.createElement("div");
    animationDiv.classList.add("clicked-animation");
    animationDiv.textContent = clickedText;
    document.body.appendChild(animationDiv);

    // Remove animation after 2 seconds and navigate
    setTimeout(() => {
        animationDiv.remove();
        window.location.href = targetHref; // Navigate to the target page
    }, 2000);
}

// Initialize Navbar
buildNavbar(navLinks);
