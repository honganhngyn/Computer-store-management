document.addEventListener("DOMContentLoaded", function () {
    const addButton = document.getElementById("addButton");
    const addCustomerForm = document.getElementById("addCustomerForm");
    const addCustomerModal = document.getElementById("addCustomerModal");
    const closeModal = document.getElementById("closeModal");
    const closeEditModal = document.getElementById("closeEditModal");
    const editCustomerModal = document.getElementById("editCustomerModal");
    const editCustomerForm = document.getElementById("editCustomerForm");
    const searchInput = document.getElementById("searchInput");
    const searchButton = document.getElementById("searchButton");
    const customerTableBody = document.querySelector("#customerTable tbody");

    // Hiển thị modal khi nhấn
    addButton.addEventListener("click", function () {
        addCustomerModal.style.display = "flex";
    });

    // Đóng modal thêm 
    closeModal.addEventListener("click", function () {
        addCustomerModal.style.display = "none";
    });

    // Đóng modal sửa 
    closeEditModal.addEventListener("click", function () {
        editCustomerModal.style.display = "none";
    });

    // Đóng modal khi click ra ngoài modal
    window.addEventListener("click", function (event) {
        if (event.target === addCustomerModal || event.target === editCustomerModal) {
            addCustomerModal.style.display = "none";
            editCustomerModal.style.display = "none";
        }
    });

    // Xử lý submit form thêm
    addCustomerForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Ngăn form reload trang
        const formData = new FormData(addCustomerForm);

        const customerData = {
            name: formData.get("name"),
            email: formData.get("email"),
            phone: formData.get("phone"),
            address: formData.get("address"),
        };

        // Gửi request đến backend bằng fetch API
        fetch("/customers/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                name: customerData.name,
                email: customerData.email,
                phone: customerData.phone,
                address: customerData.address,
            }),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json(); // Giả sử backend trả về JSON
        })
        .then((data) => {
            if (data.error) {
                console.error("Lỗi thêm thông tin khách hàng:", data.error);
                alert(data.error);
            } else {
                console.log("Thêm thông tin khách hàng thành công:", data);
                alert("Thông tin khách hàng đã được thêm thành công!");
                location.reload(); // Cập nhật danh sách
            }
        })
        .catch((error) => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi thêm khách hàng " + error.message);
        });

        // Đóng modal và reset form
        addCustomerModal.style.display = "none";
        addCustomerForm.reset();
    });

    // Xử lý khi click vào dòng để sửa
    customerTableBody.addEventListener("click", function (event) {
        const row = event.target.closest("tr");
        if (!row) return; // Nếu không phải dòng thì không làm gì

        const customerId = row.cells[0].textContent.trim();
        const customerName = row.cells[1].textContent.trim();
        const customerEmail = row.cells[2].textContent.trim();
        const customerPhone = row.cells[3].textContent.trim();
        const customerAddress = row.cells[4].textContent.trim();

        // Điền dữ liệu vào form sửa 
        document.getElementById("editCustomerId").value = customerId;
        document.getElementById("editCustomerName").value = customerName;
        document.getElementById("editCustomerEmail").value = customerEmail;
        document.getElementById("editCustomerPhone").value = customerPhone;
        document.getElementById("editCustomerAddress").value = customerAddress;

        // Hiển thị modal sửa
        editCustomerModal.style.display = "flex";
    });

    // Xử lý submit form sửa
    editCustomerForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Ngăn form reload trang
        const formData = new FormData(editCustomerForm);

        const customerData = {
            customerId: formData.get("customerId"),
            name: formData.get("name"),
            email: formData.get("email"),
            phone: formData.get("phone"),
            address: formData.get("address"),
        };

        // Gửi request đến backend bằng fetch API để cập nhật
        fetch(`/customers/update`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },

            body: new URLSearchParams({
                customerId: customerData.customerId,
                name: customerData.name,
                email: customerData.email,
                phone: customerData.phone,
                address: customerData.address,
            }),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json(); // Giả sử backend trả về JSON
        })
        .then((data) => {
            console.log("Cập nhật thông tin khách hàng thành công:", data);
            alert("Thông tin khách hàng đã được cập nhật thành công!");

            // Cập nhật dữ liệu trong bảng
            const updatedRow = customerTableBody.querySelector(`tr[data-customer-id="${customerData.customerId}"]`);
            if (updatedRow) {
                updatedRow.cells[1].textContent = customerData.name;
                updatedRow.cells[2].textContent = customerData.email;
                updatedRow.cells[3].textContent = customerData.phone;
                updatedRow.cells[4].textContent = customerData.address;
                updatedRow.cells[5].textContent = new Date().toLocaleDateString(); // Cập nhật ngày sửa
            }

            // Đóng modal và reset form
            editCustomerModal.style.display = "none";
        })
        .catch((error) => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi cập nhật thông tin khách hàng: " + error.message);
        });
    });

    // Xử lý khi nhấn tìm kiếm
    searchButton.addEventListener("click", function () {
        const keyword = searchInput.value.trim();

        if (!keyword) {
            alert("Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        fetch(`/customers/search?keyword=${encodeURIComponent(keyword)}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Server lỗi: ${response.status}`);
                }
                return response.json();
            })
            .then((customers) => {
                customerTableBody.innerHTML = ""; // Xóa dữ liệu cũ

                if (customers.length === 0) {
                    alert("Không tìm thấy khách hàng nào!");
                    return;
                }

                customers.forEach((customer) => {
                    const row = `
                        <tr>
                            <td>${customer.customerId}</td>
                            <td>${customer.name}</td>
                            <td>${customer.email}</td>
                            <td>${customer.phone}</td>
                            <td>${customer.address}</td>
                            <td>${customer.formattedDate}</td>
                        </tr>
                    `;
                    customerTableBody.insertAdjacentHTML("beforeend", row);
                });
            })
            .catch((error) => {
                console.error("Lỗi:", error);
                alert("Đã xảy ra lỗi khi tìm kiếm: " + error.message);
            });
    });

});