document.addEventListener('DOMContentLoaded', function() {
   
	function getAllTransactions() {
        fetch('/api/transactions')
        .then(response => {
            if (!response.ok) {
				console.log('Fetched transactions successfully:', response);

                throw new Error('Failed to fetch transactions');
            }
            return response.json();
        })
        .then(data => {
			console.log('Fetched transactions :', data);

            const transactionList = document.getElementById('transactionList');
            transactionList.innerHTML = ''; 
            data.forEach(transaction => {
                const div = document.createElement('div');
                div.textContent = `Account Number: ${transaction.fromAccountNumber},From Person Name: ${transaction.fromPersonName}, Amount: ${transaction.amount}`;
                transactionList.appendChild(div);
                div.addEventListener("click", async () => {
                    let p = await fetch(`/api/transactions/${transaction.id}`).then(res => res.json());
                    alert(JSON.stringify(p));
                })
            });
        })
        .then(data => {
			console.log('Fetched :', data);

            const transactionList = document.getElementById('transactionList');
            transactionList.innerHTML = ''; 
            data.forEach(transaction => {
                const div = document.createElement('div');
                div.textContent = `Account Number: ${transaction.toAccountNumber},From Person Name: ${transaction.toPersonName}`;
                transactionList.appendChild(div);
                div.addEventListener("click", async () => {
                    let p = await fetch(`/api/transactions/${transaction.id}`).then(res => res.json());
                    alert(JSON.stringify(p));
                })
            });
        })
        .catch(error => {
            console.error('Error fetching transactions:', error.message);
        });
    }
	
	
            
	const fromAccountNumberEl = document.getElementById("fromAccountNumber");
	fromAccountNumberEl.addEventListener('change', async function(event){
	  const div = document.getElementById("fromPersonName")
	  await fetch(`/api/accounts/${fromAccountNumberEl.value}`).then(res => res.json())
	  .then( data => {
		  const personAccount = data.personAccount;
		  const person = data.person;
		  
		  const dataHTML = `
		   <h2>Person Account Information:</h2>
                <p><strong>Account Number:</strong> ${personAccount.accountNumber}</p>
                <p><strong>Account Balance:</strong> ${personAccount.accountBalance}</p>
                <p><strong>Account Type:</strong> ${personAccount.accountType}</p>
                <h2>Person Information:</h2>
                <p><strong>Name:</strong> ${person.name}</p>
                <p><strong>Balance:</strong> ${person.balance}</p>
                <p><strong>Person ID:</strong> ${person.personId}</p>
		  	`;
		  	div.innerHTML = dataHTML;
		  
	  });

	  
	  
	})
	
	const toAccountNumberEl = document.getElementById("toAccountNumber");
	toAccountNumberEl.addEventListener('change', async function(event){
	  const div = document.getElementById("toPersonName")
	  await fetch(`/api/accounts/${toAccountNumberEl.value}`).then(res => res.json())
	  .then(data => {
            const personAccount = data.personAccount;
            const person = data.person;

            const dataHTML = `
                <h2>Person Account Information:</h2>
                <p><strong>Account Number:</strong> ${personAccount.accountNumber}</p>
                <p><strong>Account Balance:</strong> ${personAccount.accountBalance}</p>
                <p><strong>Account Type:</strong> ${personAccount.accountType}</p>
                <h2>Person Information:</h2>
                <p><strong>Name:</strong> ${person.name}</p>
                <p><strong>Balance:</strong> ${person.balance}</p>
                <p><strong>Person ID:</strong> ${person.personId}</p>
            `;

            div.innerHTML = dataHTML;
        });
	  	  
	})
	
    getAllTransactions();

    const addButton = document.querySelector('#addTransactionForm button');
    addButton.addEventListener('click', function(event) {
        event.preventDefault();
        const fromAccountNumber = document.getElementById('fromAccountNumber').value;
       // const fromPersonName = document.getElementById('fromPersonId').value;
        const toAccountNumber = document.getElementById('toAccountNumber').value;
       // const toPersonName = document.getElementById('toPersonName').value;
        const amount = parseFloat(document.getElementById('amount').value);
        addTransaction({ fromAccountNumber ,toAccountNumber, amount });
    	document.getElementById('addTransactionForm').reset(); // Reset the form
    });

    function addTransaction(transactionData) {
        fetch('/api/transactions', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(transactionData),
        })
        .then(data => {
			console.log(JSON.stringify(transactionData))
		})
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to add transaction: ' + response.statusText);
            }
            getAllTransactions();
        })
        .catch(error => {
            console.error('Error adding transaction:', error.message);
        });
    }
	
	
    
});
