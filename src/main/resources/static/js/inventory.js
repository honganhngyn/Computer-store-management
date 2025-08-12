document.addEventListener("DOMContentLoaded", function () {
    const addButton = document.getElementById("addButton");
    const addInventoryForm = document.getElementById("addInventoryForm");
    const addInventoryModal = document.getElementById("addInventoryModal");
    const closeModal = document.getElementById("closeModal");
    const closeEditModal = document.getElementById("closeEditModal");
    const editInventoryModal = document.getElementById("editInventoryModal");
    const editInventoryForm = document.getElementById("editInventoryForm");
    const searchInput = document.getElementById("searchInput");
    const searchButton = document.getElementById("searchButton");
    const inventoryTableBody = document.querySelector("#inventoryTable tbody");

    // Hiển thị modal khi nhấn nút Thêm kho
    addButton.addEventListener("click", function () {
        addInventoryModal.style.display = "flex";
    });

    // Đóng modal thêm kho
    closeModal.addEventListener("click", function () {
        addInventoryModal.style.display = "none";
    });

    // Đóng modal sửa kho
    closeEditModal.addEventListener("click", function () {
        editInventoryModal.style.display = "none";
    });

    // Đóng modal khi click ra ngoài modal
    window.addEventListener("click", function (event) {
        if (event.target === addInventoryModal || event.target === editInventoryModal) {
            addInventoryModal.style.display = "none";
            editInventoryModal.style.display = "none";
        }
    });

    // Xử lý submit form thêm kho
    addInventoryForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Ngăn form reload trang
        const formData = new FormData(addInventoryForm);

        const inventoryData = {
            productId: formData.get("productId"),
            quantity: parseInt(formData.get("quantity")),
            inbound: parseInt(formData.get("inbound")),
            outbound: parseInt(formData.get("outbound")),
        };

        // Gửi request đến backend bằng fetch API
        fetch("/inventory/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                productId: inventoryData.productId,
                quantity: inventoryData.quantity,
                inbound: inventoryData.inbound,
                outbound: inventoryData.outbound,
            }),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => {
            alert(data.message);
            if (data.message === "Thêm kho thành công.") {
                location.reload(); // Reload lại trang sau khi thêm thành công
            }
        })
        .catch(error => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi thêm kho: " + error.message);
        });
    });

    // Hiển thị modal sửa kho khi nhấn vào dòng
    inventoryTableBody.addEventListener("click", function(event) {
        const row = event.target.closest("tr");
        if (!row) return;

        const inventoryId = row.cells[0].textContent.trim();
        const productId = row.cells[1].textContent.trim();
        const quantity = row.cells[2].textContent.trim();
        const inbound = row.cells[3].textContent.trim();
        const outbound = row.cells[4].textContent.trim();

        // Điền dữ liệu vào form sửa kho
        const inventoryIdField = document.getElementById("inventoryId");
        const productIdField = document.getElementById("editProductId");
        const quantityField = document.getElementById("editQuantity");
        const inboundField = document.getElementById("editInbound");
        const outboundField = document.getElementById("editOutbound");

        if (inventoryIdField && productIdField && quantityField && inboundField && outboundField) {
            inventoryIdField.value = inventoryId;
            productIdField.value = productId;
            quantityField.value = quantity;
            inboundField.value = inbound;
            outboundField.value = outbound;

            editInventoryModal.style.display = "flex";
        } else {
            console.error("Không tìm thấy các phần tử form sửa kho.");
        }
    });

    // Xử lý submit form sửa kho
    editInventoryForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Ngăn form reload trang
        const formData = new FormData(editInventoryForm);
        const inventoryData = {
            inventoryId: parseInt(formData.get("inventoryId")),
            productId: formData.get("productId"),
            quantity: parseInt(formData.get("quantity")),
            inbound: parseInt(formData.get("inbound")),
            outbound: parseInt(formData.get("outbound")),
        };

        // Gửi request đến backend bằng fetch API
        fetch("/inventory/update", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                inventoryId: inventoryData.inventoryId,
                productId: inventoryData.productId,
                quantity: inventoryData.quantity,
                inbound: inventoryData.inbound,
                outbound: inventoryData.outbound,
            }),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json();
        })
        .then((data) => {
            console.log("Sửa kho thành công:", data);
            alert("Sửa kho thành công!");

            // Cập nhật danh sách kho sau khi sửa
            const updatedRow = document.querySelector(`#inventoryTable tbody tr td[data-id="${data.productId}"]`).parentNode;
            if (updatedRow) {
                updatedRow.cells[1].textContent = data.productId;
                updatedRow.cells[2].textContent = data.quantity;
                updatedRow.cells[3].textContent = data.inbound;
                updatedRow.cells[4].textContent = data.outbound;
                updatedRow.cells[5].textContent = new Date().toLocaleDateString();
            }

            // Đóng modal và reset form
            editInventoryModal.style.display = "none";
        })
        .catch(error => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi sửa kho: " + error.message);
        });
    });

    // Xử lý tìm kiếm kho
    searchButton.addEventListener("click", function () {
        const productId = searchInput.value;
        if (!productId) {
            alert("Vui lòng nhập mã sản phẩm cần tìm kiếm.");
            return;
        }

        fetch(`/inventory/search?productId=${productId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json();
        })
        .then((inventory) => {
            inventoryTableBody.innerHTML = "";
            if (!inventory) {
                alert("Không tìm thấy sản phẩm nào.");
                return;
            }
            
            const row = `
            <tr>
                <td>${inventory.inventoryId}</td>
                <td>${inventory.productId}</td>
                <td>${inventory.quantity}</td>
                <td>${inventory.inbound}</td>
                <td>${inventory.outbound}</td>
                <td>${inventory.formattedDate}</td>
            </tr>
            `;
            inventoryTableBody.insertAdjacentHTML("beforeend", row);
        })
        .catch(error => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi tìm kiếm kho: " + error.message);
        });
    });
});