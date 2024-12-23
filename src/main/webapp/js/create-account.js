const customerSearch = document.getElementById('customer-search');
const dropdownList = document.getElementById('dropdown-list');
const customerDetails = document.getElementById('customer-details');
const accountForm = document.getElementById('account-form');

// Mock customer data
const customers = [
  { id: 'C001', name: 'John Doe', email: 'john@example.com' },
  { id: 'C002', name: 'Jane Smith', email: 'jane@example.com' },
  { id: 'C003', name: 'Emily Johnson', email: 'emily@example.com' }
];

// Display dropdown when input is focused
customerSearch.addEventListener('focus', () => {
  updateDropdown('');
  dropdownList.classList.add('active');
});

// Populate dropdown dynamically
customerSearch.addEventListener('input', () => {
  const query = customerSearch.value.toLowerCase();
  updateDropdown(query);
});

// Update dropdown options
function updateDropdown(query) {
  const filteredCustomers = customers.filter(c => c.id.toLowerCase().includes(query));
  dropdownList.innerHTML = filteredCustomers.length
    ? filteredCustomers.map(c => `<li data-id="${c.id}">${c.id}</li>`).join('')
    : '<li>No matching customers</li>';
}

// Handle customer selection
dropdownList.addEventListener('click', (e) => {
  if (e.target.tagName === 'LI' && e.target.dataset.id) {
    const selectedId = e.target.dataset.id;
    const customer = customers.find(c => c.id === selectedId);

    // Display customer details
    document.getElementById('customer-name').value = customer.name;
    document.getElementById('customer-email').value = customer.email;
    customerDetails.classList.remove('hidden');
    accountForm.classList.remove('hidden');

    // Hide dropdown
    dropdownList.classList.remove('active');
  }
});

// Hide dropdown when clicking outside
document.addEventListener('click', (e) => {
  if (!e.target.closest('.search-dropdown')) {
    dropdownList.classList.remove('active');
  }
});

// Handle account form submission
accountForm.addEventListener('submit', (e) => {
  e.preventDefault();
  alert('Account created successfully!');
});
