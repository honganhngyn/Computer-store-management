document.getElementById('registerForm').addEventListener('submit', (event) => {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;

    if (password !== confirmPassword) {
        document.getElementById('error-message').textContent = 'Mật khẩu không khớp!';
        return;
    }

    fetch('/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
    })
        .then((response) => response.json())
        .then((data) => {
            if (response.ok) {
                alert('Đăng ký thành công!');
                window.location.href = 'login.html';
            } else {
                document.getElementById('error-message').textContent = data;
            }
        })
        .catch((error) => {
            console.error('Error:', error);
        });
});