const form = document.getElementById('reserveForm');
const nameInput = document.getElementById('name');
const dateInput = document.getElementById('date');
const timeInput = document.getElementById('time');
const peopleInput = document.getElementById('people');
const reserveButton = document.getElementById('reserveButton');
const reservationsContainer = document.getElementById('reservations');

form.addEventListener('submit', (e) => {
  e.preventDefault();
  if (nameInput.value && dateInput.value && timeInput.value && peopleInput.value) {
    const reservation = {
      name: nameInput.value,
      date: dateInput.value,
      time: timeInput.value,
      people: peopleInput.value
    };
    saveToLocalStorage(reservation);
    displayReservations();
    form.reset();
  }
});

function saveToLocalStorage(reservation) {
  let reservations = JSON.parse(localStorage.getItem('reservations')) || [];
  reservations.push(reservation);
  localStorage.setItem('reservations', JSON.stringify(reservations));
}

function displayReservations() {
  let reservations = JSON.parse(localStorage.getItem('reservations')) || [];
  let reservationsHtml = '';
  reservations.forEach((reservation, index) => {
    reservationsHtml += `
      <div class="reservation">
        <p>Name: ${reservation.name}</p>
        <p>Date: ${reservation.date}</p>
        <p>Time: ${reservation.time}</p>
        <p>People: ${reservation.people}</p>
        <button onclick="deleteReservation(${index})">Delete</button>
      </div>
    `;
  });
  reservationsContainer.innerHTML = reservationsHtml;
}

function deleteReservation(index) {
  let reservations = JSON.parse(localStorage.getItem('reservations')) || [];
  reservations.splice(index, 1);
  localStorage.setItem('reservations', JSON.stringify(reservations));
  displayReservations();
}