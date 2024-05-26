document.addEventListener("DOMContentLoaded", () => {
    loadAvailableCars();
    
    const rentCarForm = document.getElementById('rentCarForm');
    const returnCarForm = document.getElementById('returnCarForm');
    
    rentCarForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const customerName = document.getElementById('customerName').value;
        const carId = document.getElementById('carSelection').value;
        const rentalDays = document.getElementById('rentalDays').value;
        
        const response = await fetch('/rent', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ customerName, carId, rentalDays }),
        });
        
        const result = await response.json();
        document.getElementById('output').innerText = result.message;
        loadAvailableCars();
    });

    returnCarForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const carId = document.getElementById('carIdReturn').value;
        
        const response = await fetch('/return', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ carId }),
        });
        
        const result = await response.json();
        document.getElementById('output').innerText = result.message;
        loadAvailableCars();
    });
});

function showRentCar() {
    document.getElementById('rentCar').classList.remove('hidden');
    document.getElementById('returnCar').classList.add('hidden');
}

function showReturnCar() {
    document.getElementById('rentCar').classList.add('hidden');
    document.getElementById('returnCar').classList.remove('hidden');
}

async function loadAvailableCars() {
    const response = await fetch('/availableCars');
    const cars = await response.json();
    const carSelection = document.getElementById('carSelection');
    carSelection.innerHTML = '';
    
    cars.forEach(car => {
        const option = document.createElement('option');
        option.value = car.carId;
        option.text = `${car.brand} ${car.model}`;
        carSelection.add(option);
    });
}
