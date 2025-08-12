document.getElementById('loginForm').addEventListener('submit', async (event) => {
    event.preventDefault();

    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;

    try {
        const response = await fetch('/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            const result = await response.json();
            window.location.href = "/";// Điều hướng đến trang chính hoặc giao diện phù hợp
            alert(`Đăng nhập thành công! Vai trò: ${result.role}`);
        } else {
            const error = await response.json(); // Lấy thông báo lỗi từ backend
            document.getElementById('error-message').textContent = error.error || "Đã xảy ra lỗi!";
        }
    } catch (err) {
        console.error('Lỗi:', err);
        document.getElementById('error-message').textContent = "Tài khoản không tồn tại hoặc sai mật khẩu!";
    }
});