document.addEventListener("DOMContentLoaded", function () {
    const addButton = document.getElementById("addButton");
    const addEmployeeForm = document.getElementById("addEmployeeForm");
    const addEmployeeModal = document.getElementById("addEmployeeModal");
    const closeModal = document.getElementById("closeModal");
    const closeEditModal = document.getElementById("closeEditModal");
    const editEmployeeModal = document.getElementById("editEmployeeModal");
    const editEmployeeForm = document.getElementById("editEmployeeForm");
    const searchInput = document.getElementById("searchInput");
    const searchButton = document.getElementById("searchButton");
    const employeeTableBody = document.querySelector("#employeeTable tbody");

    // Hiển thị modal thêm
    addButton.addEventListener("click", function () {
        addEmployeeModal.style.display = "flex";
    });

    // Đóng modal thêm
    closeModal.addEventListener("click", function () {
        addEmployeeModal.style.display = "none";
    });

    // Đóng modal sửa
    closeEditModal.addEventListener("click", function () {
        editEmployeeModal.style.display = "none";
    });

    // Đóng modal khi click ra ngoài
    window.addEventListener("click", function (event) {
        if (event.target === addEmployeeModal || event.target === editEmployeeModal) {
            addEmployeeModal.style.display = "none";
            editEmployeeModal.style.display = "none";
        }
    });

    // Xử lý submit form thêm nhân viên
    addEmployeeForm.addEventListener("submit", function (event) {
        event.preventDefault();
        const formData = new FormData(addEmployeeForm);

        const employeeData = {
            name: formData.get("name"),
            username: formData.get("username"),
            isAdmin: formData.get("isAdmin") === "true", // Chuyển giá trị thành boolean
            position: formData.get("position"),
            salary: parseFloat(formData.get("salary")),
            email: formData.get("email"),
            phone: formData.get("phone"),
        };

        fetch("/employees/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                name: employeeData.name,
                username: employeeData.username,
                isAdmin: employeeData.isAdmin,
                password: employeeData.password,
                position: employeeData.position,
                salary: employeeData.salary,
                email: employeeData.email,
                phone: employeeData.phone,
            }),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json(); // Giả sử backend trả về JSON
        })
        .then(data => {
            if (data.error) {
                console.error("Lỗi thêm thông tin nhân viên:", data.error);
                alert(data.error);
            } else {
                console.log("Thêm thông tin nhân viên thành công:", data);
                alert("Thông tin nhân viên đã được thêm thành công!");
                location.reload(); // Cập nhật danh sách
            }
        })
        .catch(error => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi thêm nhân viên " + error.message);
        });

        addEmployeeModal.style.display = "none";
        addEmployeeForm.reset();
    });

    // Xử lý tìm kiếm
    searchButton.addEventListener("click", function () {
        const keyword = searchInput.value.trim();
        if (!keyword) {
            alert("Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        fetch(`/employees/search?keyword=${encodeURIComponent(keyword)}`)
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json();
        })
        .then((employees) => {
            employeeTableBody.innerHTML = "";

            if (employees.length === 0) {
                alert("Không tìm thấy khách hàng nào!");
                return;
            }

            employees.forEach((employee) => {
                const row = `
                    <tr>
                        <td>${employee.employeeId}</td>
                        <td>${employee.name}</td>
                        <td>${employee.username}</td>
                        <td>${employee.isAdmin ? 'Yes' : 'No'}</td>
                        <td>${employee.position}</td>
                        <td>${employee.formattedSalary}</td>
                        <td>${employee.email}</td>
                        <td>${employee.phone}</td>
                        <td>${employee.formattedDate}</td>
                    </tr>
                `;
                employeeTableBody.insertAdjacentHTML("beforeend", row);
            });
        })
        .catch(error => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi tìm kiếm.");
        });
    });

    // Xử lý khi click vào dòng để sửa
    employeeTableBody.addEventListener("click", function (event) {
        const row = event.target.closest("tr");
        if (!row) return; // Nếu không phải dòng thì không làm gì

        const employeeId = row.cells[0].textContent.trim();
        const employeeName = row.cells[1].textContent.trim();
        const employeeUsername = row.cells[2].textContent.trim();
        const employeeIsAdmin = row.cells[3].textContent.trim();
        const employeePosition = row.cells[4].textContent.trim();
        let employeeSalary = row.cells[5].textContent.trim();
        employeeSalary = parseFloat(employeeSalary.replaceAll(".", ""));
        const employeeEmail = row.cells[6].textContent.trim();
        const employeePhone = row.cells[7].textContent.trim();

        // Điền dữ liệu vào form sửa
        document.getElementById("editEmployeeId").value = employeeId;
        document.getElementById("editEmployeeName").value = employeeName;
        document.getElementById("editEmployeeUsername").value = employeeUsername;
        document.getElementById("editEmployeeIsAdmin").value = employeeIsAdmin;
        document.getElementById("editEmployeePosition").value = employeePosition;
        document.getElementById("editEmployeeSalary").value = employeeSalary;
        document.getElementById("editEmployeeEmail").value = employeeEmail;
        document.getElementById("editEmployeePhone").value = employeePhone;


        // Hiển thị modal sửa
        editEmployeeModal.style.display = "flex";
    });

    // Xử lý submit form sửa nhân viên
    editEmployeeForm.addEventListener("submit", function (event) {
        event.preventDefault();
        const formData = new FormData(editEmployeeForm);

        const employeeData = {
            employeeId: formData.get("employeeId"),
            name: formData.get("name"),
            username: formData.get("username"),
            isAdmin: formData.get("isAdmin") === "true",
            position: formData.get("position"),
            salary: parseFloat(formData.get("salary")),
            email: formData.get("email"),
            phone: formData.get("phone"),
        };

        fetch("/employees/update", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams(employeeData),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json(); // Giả sử backend trả về JSON
        })
        .then(data => {
            console.log("Cập nhật thông tin nhân viên thành công:", data);
            alert("Thông tin nhân viên đã được cập nhật thành công!");

            // Cập nhật dữ liệu trong bảng
            const updatedRow = employeeTableBody.querySelector(`tr[data-employee-id="${employeeData.employeeId}"]`);
            if (updatedRow) {
                updatedRow.cells[1].textContent = employeeData.name;
                updatedRow.cells[2].textContent = employeeData.username;
                updatedRow.cells[3].textContent = employeeData.isAdmin;
                updatedRow.cells[4].textContent = employeeData.position;
                updatedRow.cells[5].textContent = employeeData.salary;
                updatedRow.cells[6].textContent = employeeData.email;
                updatedRow.cells[7].textContent = employeeData.phone;
                //updatedRow.cells[5].textContent = new Date().toLocaleDateString(); // Cập nhật ngày sửa
            }
            editEmployeeModal.style.display = "none";
        })
        .catch(error => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi cập nhật nhân viên.");
        });

        // editEmployeeForm.reset();
    });
});