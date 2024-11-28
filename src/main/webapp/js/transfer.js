document.addEventListener("DOMContentLoaded", () => {
    const fromAccountDropdown = document.getElementById("fromAccount");
    const toAccountDropdown = document.getElementById("toAccount");
    const transferTypeInputs = document.querySelectorAll('input[name="transferType"]');
    const selfTransferFields = document.getElementById("selfTransferFields");
    const transferToOtherFields = document.getElementById("transferToOtherFields");
    const recipientBankInputs = document.querySelectorAll('input[name="recipientBank"]');
    const ifscCodeField = document.getElementById("ifscCodeField");
    const fromBankDropdown = document.getElementById("fromBank");
    const transferForm = document.getElementById("transferForm");

    // Fetch and populate accounts for both From and To Accounts
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

                const toOption = document.createElement("option");
                toOption.value = account.accountNumber;
                toOption.textContent = `Account: ${account.accountNumber}`;
                toAccountDropdown.appendChild(toOption);
            });
        } catch (error) {
            console.error("Error fetching accounts:", error);
        }
    }

    // Fetch and populate bank options (both sender and recipient)

    // Toggle fields based on transfer type selection
    transferTypeInputs.forEach(input => {
        input.addEventListener("change", () => {
            if (input.value === "self") {
                selfTransferFields.classList.add("show");
                transferToOtherFields.classList.remove("show");

                // Show "To Account" dropdown when "Self Transfer" is selected
                toAccountDropdown.closest('.form-group').classList.remove("hidden");
            } else {
                selfTransferFields.classList.remove("show");
                transferToOtherFields.classList.add("show");

                // Hide "To Account" dropdown when "Transfer to Other Account" is selected
                toAccountDropdown.closest('.form-group').classList.add("hidden");
            }
        });
    });

    // Show or hide IFSC code input based on recipient bank selection (Yes/No)
    recipientBankInputs.forEach(input => {
        input.addEventListener("change", () => {
            if (input.value === "No") {
                ifscCodeField.classList.add("show");
            } else {
                ifscCodeField.classList.remove("show");
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

    fetchAccounts();
});
