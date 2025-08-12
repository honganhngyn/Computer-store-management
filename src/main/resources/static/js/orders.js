document.addEventListener("DOMContentLoaded", function () {
    const addButton = document.getElementById("addButton");
    const addOrderForm = document.getElementById("addOrderForm");
    const addOrderModal = document.getElementById("addOrderModal");
    const closeModal = document.getElementById("closeModal");
    const closeEditModal = document.getElementById("closeEditModal");
    const editOrderModal = document.getElementById("editOrderModal");
    const editOrderForm = document.getElementById("editOrderForm");
    const searchInput = document.getElementById("searchInput");
    const searchButton = document.getElementById("searchButton");
    const orderTableBody = document.querySelector("#orderTable tbody");

    // Hiển thị modal khi nhấn nút Thêm đơn hàng
    addButton.addEventListener("click", function () {
        addOrderModal.style.display = "flex";
    });

    // Đóng modal thêm đơn hàng
    closeModal.addEventListener("click", function () {
        addOrderModal.style.display = "none";
    });

    // Đóng modal sửa đơn hàng
    closeEditModal.addEventListener("click", function () {
        editOrderModal.style.display = "none";
    });

    // Đóng modal khi click ra ngoài modal
    window.addEventListener("click", function (event) {
        if (event.target === addOrderModal || event.target === editOrderModal) {
            addOrderModal.style.display = "none";
            editOrderModal.style.display = "none";
        }
    });

    // Xử lý submit form thêm đơn hàng
    addOrderForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Ngăn form reload trang
        const formData = new FormData(addOrderForm);

        const orderData = {
            customerId: parseInt(formData.get("customerId")),
            employeeId: parseInt(formData.get("employeeId")),
            orderDate: formData.get("formattedDate"),
            status: formData.get("status"),
        };

        // Gửi request đến backend bằng fetch API
        fetch("/orders/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                customerId: orderData.customerId,
                employeeId: orderData.employeeId,
                orderDate: orderData.orderDate,
                status: orderData.status,
            }),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json(); // Giả sử backend trả về JSON
        })
        .then((data) => {
            console.log("Thêm đơn hàng thành công:", data);
            alert("Đơn hàng đã được thêm thành công!");

            // Cập nhật danh sách đơn hàng sau khi thêm
            location.reload();
        })
        .catch((error) => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi thêm đơn hàng " + error.message);
        });

        // Đóng modal và reset form
        addOrderModal.style.display = "none";
        addOrderForm.reset();
    });

    // Xử lý khi click vào dòng đơn hàng để sửa
    orderTableBody.addEventListener("click", function (event) {
        const row = event.target.closest("tr");
        if (!row) return; // Nếu không phải dòng đơn hàng thì không làm gì

        const orderId = row.cells[0].textContent.trim();
        const status = row.cells[4].textContent.trim();

        // Điền dữ liệu vào form sửa đơn hàng
        document.getElementById("editOrderId").value = orderId;
        document.getElementById("editStatus").value = status;

        // Hiển thị modal sửa đơn hàng
        editOrderModal.style.display = "flex";
    });

    // Xử lý submit form sửa đơn hàng
    editOrderForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Ngăn form reload trang
        const formData = new FormData(editOrderForm);

        const orderData = {
            orderId: parseInt(formData.get("orderId")),
            status: formData.get("status"),
        };

        // Gửi request đến backend bằng fetch API để cập nhật trạng thái đơn hàng
        fetch(`/orders/updateStatus`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                orderId: orderData.orderId,
                status: orderData.status,
            }),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json(); // Giả sử backend trả về JSON
        })
        .then((data) => {
            console.log("Cập nhật trạng thái đơn hàng thành công:", data);
            alert("Trạng thái đơn hàng đã được cập nhật thành công!");

            // Cập nhật dữ liệu trong bảng
            const updatedRow = orderTableBody.querySelector(`tr[data-order-id="${orderData.orderId}"]`);
            if (updatedRow) {
                updatedRow.cells[4].textContent = orderData.status;
            }

            // Đóng modal và reset form
            editOrderModal.style.display = "none";
            // Cập nhật danh sách đơn hàng sau khi sửa
            //location.reload();
        })
        .catch((error) => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi cập nhật trạng thái đơn hàng: " + error.message);
        });
    });

    // Xử lý khi nhấn tìm kiếm
    searchButton.addEventListener("click", function () {
        const orderId = searchInput.value;

        if (!orderId) {
            alert("Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        fetch(`/orders/search?orderId=${orderId}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Server lỗi: ${response.status}`);
                }
                return response.json();
            })
            .then((order) => {
                orderTableBody.innerHTML = ""; // Xóa dữ liệu cũ

                if (order.length === 0) {
                    alert("Không tìm thấy đơn hàng nào!");
                    return;
                }
                const row = `
                    <tr>
                        <td>${order.orderId}</td>
                        <td>${order.customerId}</td>
                        <td>${order.employeeId}</td>
                        <td>${order.orderDate}</td>
                        <td>${order.status}</td>
                    </tr>
                `;
                orderTableBody.insertAdjacentHTML("beforeend", row);
            })
            .catch((error) => {
                console.error("Lỗi:", error);
                alert("Đã xảy ra lỗi khi tìm kiếm: " + error.message);
            });
    });

});