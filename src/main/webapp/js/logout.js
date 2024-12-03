async function logout() {
    try {
        
        localStorage.clear();
        sessionStorage.clear();

        // Redirect to the login page
        window.location.href = "/login.html";
    } catch (error) {
        console.error("Error during logout:", error);
        alert("Failed to log out. Please try again.");
    }
}
