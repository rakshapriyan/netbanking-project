document.addEventListener("DOMContentLoaded", () => {
    const fromAccountDropdown = document.getElementById("fromAccount");
    const toAccountDropdown = document.getElementById("toAccount");
    const transferTypeInputs = document.querySelectorAll('input[name="transferType"]');
    const selfTransferFields = document.getElementById("selfTransferFields");
    const transferToOtherFields = document.getElementById("transferToOtherFields");
    const recipientBankInputs = document.querySelectorAll('input[name="recipientBank"]');
    const ifscCodeField = document.getElementById("ifscCodeField");
    const transferForm = document.getElementById("transferForm");

    const userRole = sessionStorage.getItem("role"); // Fetch user role (customer, employee, or manager)

    // Hide transfer options for employee and manager roles
    function configureRoleBasedUI() {
        if (userRole === "employee" || userRole === "manager") {
            // Hide transfer type radio buttons
            document.querySelector(".radio-group").classList.add("hidden");
            transferToOtherFields.classList.remove("hidden");
            selfTransferFields.classList.add("hidden");
        } else if (userRole === "customer") {
            // Show transfer type options for customers
            transferTypeInputs.forEach(input => {
                input.addEventListener("change", () => {
                    if (input.value === "self") {
                        // Show the "To Account" dropdown
                        selfTransferFields.classList.remove("hidden");
                        transferToOtherFields.classList.add("hidden");
                    } else if (input.value === "other") {
                        // Hide the "To Account" dropdown
                        selfTransferFields.classList.add("hidden");
                        transferToOtherFields.classList.remove("hidden");
                    }
                });
            });
        }
    }

    // Show or hide IFSC code input based on recipient bank selection
    recipientBankInputs.forEach(input => {
        input.addEventListener("change", () => {
            if (input.value === "No") {
                ifscCodeField.classList.remove("hidden");
            } else {
                ifscCodeField.classList.add("hidden");
            }
        });
    });

    // Handle form submission
    transferForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const transferData = new FormData(transferForm);
        const data = {
            fromAccount: transferData.get("fromAccount"),
            toAccount: transferData.get("toAccount") || transferData.get("recipientAccount"),
            recipientName: transferData.get("recipientName"),
            ifscCode: transferData.get("ifscCode"),
            amount: transferData.get("amount"),
            description: transferData.get("description"),
        };

        try {
            const axiosInstance = axios.create({
                baseURL: 'http://localhost:8080/Netbanking1',
                timeout: 5000,
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: sessionStorage.getItem('token'),
                },
            });

            await axiosInstance.post('/transfer', data);
            alert('Transfer Successful!');
        } catch (error) {
            console.error("Error during transfer:", error);
            alert('Transfer failed. Please try again.');
        }
    });

    // Fetch and populate accounts
    async function fetchAccounts() {
        try {
            const axiosInstance = axios.create({
                baseURL: 'http://localhost:8080/Netbanking1',
                timeout: 5000,
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: sessionStorage.getItem('token'),
                },
            });
            const response = await axiosInstance.get('/accounts');
            const accounts = response.data;

            accounts.forEach(account => {
                const option = document.createElement("option");
                option.value = account.accountNumber;
                option.textContent = `Account: ${account.accountNumber}`;
                fromAccountDropdown.appendChild(option);

                if (userRole === "customer") {
                    const toOption = document.createElement("option");
                    toOption.value = account.accountNumber;
                    toOption.textContent = `Account: ${account.accountNumber}`;
                    toAccountDropdown.appendChild(toOption);
                }
            });
        } catch (error) {
            console.error("Error fetching accounts:", error);
        }
    }

    // Apply role-based configurations
    configureRoleBasedUI();
    fetchAccounts();
});
