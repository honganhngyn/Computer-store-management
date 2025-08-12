document.addEventListener("DOMContentLoaded", function () {
    const addButton = document.getElementById("addButton");
    const addSupplierForm = document.getElementById("addSupplierForm");
    const addSupplierModal = document.getElementById("addSupplierModal");
    const closeModal = document.getElementById("closeModal");
    const closeEditModal = document.getElementById("closeEditModal");
    const editSupplierModal = document.getElementById("editSupplierModal");
    const editSupplierForm = document.getElementById("editSupplierForm");
    const searchInput = document.getElementById("searchInput");
    const searchButton = document.getElementById("searchButton");
    const supplierTableBody = document.querySelector("#supplierTable tbody");

    // Hiển thị modal khi nhấn
    addButton.addEventListener("click", function () {
        addSupplierModal.style.display = "flex";
    });

    // Đóng modal thêm 
    closeModal.addEventListener("click", function () {
        addSupplierModal.style.display = "none";
    });

    // Đóng modal sửa
    closeEditModal.addEventListener("click", function () {
        editSupplierModal.style.display = "none";
    });

    // Đóng modal khi click ra ngoài modal
    window.addEventListener("click", function (event) {
        if (event.target === addSupplierModal || event.target === editSupplierModal) {
            addSupplierModal.style.display = "none";
            editSupplierModal.style.display = "none";
        }
    });

    // Xử lý submit form thêm
    addSupplierForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Ngăn form reload trang
        const formData = new FormData(addSupplierForm);

        const supplierData = {
            name: formData.get("name"),
            email: formData.get("email"),
            phone: formData.get("phone"),
            address: formData.get("address"),
        };

        // Gửi request đến backend bằng fetch API
        fetch("/suppliers/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                name: supplierData.name,
                email: supplierData.email,
                phone: supplierData.phone,
                address: supplierData.address,
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
                console.error("Lỗi thêm thông tin nhà cung cấp:", data.error);
                alert(data.error);
            } else {
                console.log("Thêm thông tin nhà cung cấp thành công:", data);
                alert("Thông tin nhà cung cấp đã được thêm thành công!");
                location.reload(); // Cập nhật danh sách
            }
        })
        .catch((error) => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi thêm nhà cung cấp " + error.message);
        });

        // Đóng modal và reset form
        addSupplierModal.style.display = "none";
        addSupplierForm.reset();
    });

    // Xử lý khi click vào dòng để sửa
    supplierTableBody.addEventListener("click", function (event) {
        const row = event.target.closest("tr");
        if (!row) return; // Nếu không phải dòng thì không làm gì

        const supplierId = row.cells[0].textContent.trim();
        const supplierName = row.cells[1].textContent.trim();
        const supplierEmail = row.cells[2].textContent.trim();
        const supplierPhone = row.cells[3].textContent.trim();
        const supplierAddress = row.cells[4].textContent.trim();

        // Điền dữ liệu vào form sửa
        document.getElementById("editSupplierId").value = supplierId;
        document.getElementById("editSupplierName").value = supplierName;
        document.getElementById("editSupplierEmail").value = supplierEmail;
        document.getElementById("editSupplierPhone").value = supplierPhone;
        document.getElementById("editSupplierAddress").value = supplierAddress;

        // Hiển thị modal sửa
        editSupplierModal.style.display = "flex";
    });

    // Xử lý submit form sửa
    editSupplierForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Ngăn form reload trang
        const formData = new FormData(editSupplierForm);

        const supplierData = {
            supplierId: formData.get("supplierId"),
            name: formData.get("name"),
            email: formData.get("email"),
            phone: formData.get("phone"),
            address: formData.get("address"),
        };

        // Gửi request đến backend bằng fetch API để cập nhật
        fetch(`/suppliers/update`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },

            body: new URLSearchParams({
                supplierId: supplierData.supplierId,
                name: supplierData.name,
                email: supplierData.email,
                phone: supplierData.phone,
                address: supplierData.address,
            }),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json(); // Giả sử backend trả về JSON
        })
        .then((data) => {
            console.log("Cập nhật thông tin nhà cung cấp thành công:", data);
            alert("Thông tin nhà cung cấp đã được cập nhật thành công!");

            // Cập nhật dữ liệu trong bảng
            const updatedRow = supplierTableBody.querySelector(`tr[data-supplier-id="${supplierData.supplierId}"]`);
            if (updatedRow) {
                updatedRow.cells[1].textContent = supplierData.name;
                updatedRow.cells[2].textContent = supplierData.email;
                updatedRow.cells[3].textContent = supplierData.phone;
                updatedRow.cells[4].textContent = supplierData.address;
            }

            // Đóng modal và reset form
            editSupplierModal.style.display = "none";
            // Cập nhật danh sách sau khi sửa
            //location.reload();
        })
        .catch((error) => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi cập nhật thông tin nhà cung cấp: " + error.message);
        });
    });

    // Xử lý khi nhấn tìm kiếm
    searchButton.addEventListener("click", function () {
        const keyword = searchInput.value.trim();

        if (!keyword) {
            alert("Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        fetch(`/suppliers/search?keyword=${encodeURIComponent(keyword)}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Server lỗi: ${response.status}`);
                }
                return response.json();
            })
            .then((suppliers) => {
                supplierTableBody.innerHTML = ""; // Xóa dữ liệu cũ

                if (suppliers.length === 0) {
                    alert("Không tìm thấy nhà cung cấp nào!");
                    return;
                }

                suppliers.forEach((supplier) => {
                    const row = `
                        <tr>
                            <td>${supplier.supplierId}</td>
                            <td>${supplier.name}</td>
                            <td>${supplier.email}</td>
                            <td>${supplier.phone}</td>
                            <td>${supplier.address}</td>
                        </tr>
                    `;
                    supplierTableBody.insertAdjacentHTML("beforeend", row);
                });
            })
            .catch((error) => {
                console.error("Lỗi:", error);
                alert("Đã xảy ra lỗi khi tìm kiếm: " + error.message);
            });
    });

});