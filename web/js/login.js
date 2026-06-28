
async function login() {
    const role     = document.getElementById('role').value;
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;
    const errEl    = document.getElementById('errorMsg');

    errEl.style.display = 'none';

    if (!username || !password) {
        showError('Please enter your username and password.');
        return;
    }

    const btn = document.querySelector('.btn-login');
    btn.textContent = 'Signing in…';
    btn.disabled = true;

    try {
        const response = await fetch('http://localhost:8080/login', {
            method:  'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body:    'role='     + encodeURIComponent(role)
                   + '&username=' + encodeURIComponent(username)
                   + '&password=' + encodeURIComponent(password)
        });

        const result = await response.text();

        if (result === 'ADMIN') {
            localStorage.setItem('adminName', username);
            window.location.href = 'admin.html';
        } else if (result.startsWith('STUDENT')) {
            // Server may return "STUDENT,<code>" or just "STUDENT"
            const parts = result.split(',');
            localStorage.setItem('studentName', username);
            localStorage.setItem('studentCode', parts[1] || '');
            window.location.href = 'student.html';
        } else {
            showError('Invalid username or password. Please try again.');
        }
    } catch (err) {
        console.error(err);
        showError('Cannot connect to the server. Make sure HostelServer is running on port 8080.');
    } finally {
        btn.textContent = 'Sign In';
        btn.disabled = false;
    }
}

function showError(msg) {
    const el = document.getElementById('errorMsg');
    el.textContent = msg;
    el.style.display = 'block';
}