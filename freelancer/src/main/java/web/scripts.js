document.getElementById('registerForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    fetch('/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
    }).then(response => {
        if (response.ok) {
            alert('Registration successful!');
        } else {
            alert('Registration failed!');
        }
    });
});

document.getElementById('loginForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const username = document.getElementById('loginUsername').value;
    const password = document.getElementById('loginPassword').value;

    fetch('/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`
    }).then(response => {
        if (response.ok) {
            alert('Login successful!');
        } else {
            alert('Login failed!');
        }
    });
});

document.getElementById('businessForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const name = document.getElementById('businessName').value;
    const description = document.getElementById('businessDescription').value;
    const contactInfo = document.getElementById('businessContact').value;

    fetch('/createBusiness', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `name=${encodeURIComponent(name)}&description=${encodeURIComponent(description)}&contactInfo=${encodeURIComponent(contactInfo)}`
    }).then(response => {
        if (response.ok) {
            alert('Business created successfully!');
        } else {
            alert('Failed to create business!');
        }
    });
});

document.getElementById('updateBusinessForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const id = document.getElementById('updateBusinessId').value;
    const name = document.getElementById('updateBusinessName').value;
    const description = document.getElementById('updateBusinessDescription').value;
    const contactInfo = document.getElementById('updateBusinessContact').value;

    fetch('/updateBusiness', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `id=${encodeURIComponent(id)}&name=${encodeURIComponent(name)}&description=${encodeURIComponent(description)}&contactInfo=${encodeURIComponent(contactInfo)}`
    }).then(response => {
        if (response.ok) {
            alert('Business updated successfully!');
        } else {
            alert('Failed to update business!');
        }
    });
});

document.getElementById('deleteBusinessForm').addEventListener('submit', function (e) {
    e.preventDefault();
    const id = document.getElementById('deleteBusinessId').value;

    fetch('/deleteBusiness', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `id=${encodeURIComponent(id)}`
    }).then(response => {
        if (response.ok) {
            alert('Business deleted successfully!');
        } else {
            alert('Failed to delete business!');
        }
    });
});

document.getElementById('listBusinesses').addEventListener('click', function () {
    fetch('/listBusinesses')
        .then(response => response.json())
        .then(data => {
            const businessesList = document.getElementById('businessesList');
            businessesList.innerHTML = '';
            data.forEach(business => {
                const div = document.createElement('div');
                div.classList.add('business');
                div.innerHTML = `<h3>${business.name}</h3><p>${business.description}</p><p>${business.contactInfo}</p>`;
                businessesList.appendChild(div);
            });
        });
});
