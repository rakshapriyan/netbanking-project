document.addEventListener("DOMContentLoaded", function () {
    // Check if the user is already logged in
    const token = sessionStorage.getItem("token");
    const role = sessionStorage.getItem("role");

    if (token) {
        // Redirect based on the user's role
        if (role === "customer") {
            window.location.href = '/dashboard.html';
        } else if (role === "employee" || role === "manager") {
            window.location.href = '/employeeDashboard.html';
        } else {
            console.error("Unknown role. Logging out.");
            sessionStorage.clear();
            window.location.href = '/login.html';
        }
    }
});

// Add event listener to the login form
document.getElementById("loginForm").addEventListener("submit", function (event) {
    event.preventDefault(); // Prevent the default form submission

    // Get form data
    const username = document.getElementById("username").value.trim();
    const password = document.getElementById("password").value.trim();

    // Validate inputs
    if (!username || !password) {
        alert("Please enter both username and password.");
        return;
    }

    // Create the data as a JSON object
    const data = {
        username: username,
        password: password
    };

    // Make POST request to the servlet with the correct headers
    axios.post('http://localhost:8080/Netbanking1/login', data, {
        headers: {
            'Content-Type': 'application/json' // Set the content type to application/json
        }
    })
    .then(function (response) {
        console.log("Login response:", response); // Log the full response
        
        // Check if the response contains the required data
        if (response.status === 200 && response.data.token && response.data.role) {
            // Save the data in sessionStorage
            sessionStorage.setItem("name", response.data.name);
            sessionStorage.setItem("token", response.data.token);
            sessionStorage.setItem("role", response.data.role);

            // Redirect based on the user's role
            if (response.data.role === "customer") {
                window.location.href = '/dashboard.html';
            } else if (response.data.role === "employee" || response.data.role === "manager") {
                window.location.href = '/employeeDashboard.html';
            } else {
                alert("Unknown role. Please contact support.");
                console.error("Unknown role:", response.data.role);
            }
        } else {
            // Handle missing data or unexpected response
            alert("Unexpected response. Please try again.");
        }
    })
    .catch(function (error) {
        // Handle errors from the server
        if (error.response) {
            if (error.response.status === 403) {
                // Handle 403 Forbidden specifically
                alert("Invalid username or password. Please try again.");
            } else {
                console.error("Server error:", error.response);
                alert(`Error: ${error.response.statusText}`);
            }
        } else {
            console.error("Network or unexpected error:", error);
            alert("An error occurred during login. Please try again later.");
        }
    });
});

// Prevent accessing the login page if already logged in
window.addEventListener("popstate", function () {
    const token = sessionStorage.getItem("token");
    const role = sessionStorage.getItem("role");

    if (token) {
        // Redirect based on the user's role
        if (role === "customer") {
            window.location.href = '/dashboard.html';
        } else if (role === "employee" || role === "manager") {
            window.location.href = '/employeeDashboard.html';
        } else {
            console.error("Unknown role. Logging out.");
            sessionStorage.clear();
            window.location.href = '/login.html';
        }
    }
});
