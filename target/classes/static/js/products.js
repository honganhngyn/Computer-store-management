document.addEventListener("DOMContentLoaded", function () {
    const addButton = document.getElementById("addButton");
    const addProductForm = document.getElementById("addProductForm");
    const addProductModal = document.getElementById("addProductModal");
    const closeModal = document.getElementById("closeModal");
    const closeEditModal = document.getElementById("closeEditModal");
    const editProductModal = document.getElementById("editProductModal");
    const editProductForm = document.getElementById("editProductForm");
    const searchInput = document.getElementById("searchInput");
    const searchButton = document.getElementById("searchButton");
    const productTableBody = document.querySelector("#productTable tbody");

    // Hiển thị modal khi nhấn
    addButton.addEventListener("click", function () {
        addProductModal.style.display = "flex";
    });

    // Đóng modal thêm sản phẩm
    closeModal.addEventListener("click", function () {
        addProductModal.style.display = "none";
    });

    // Đóng modal sửa sản phẩm
    closeEditModal.addEventListener("click", function () {
        editProductModal.style.display = "none";
    });

    // Đóng modal khi click ra ngoài modal
    window.addEventListener("click", function (event) {
        if (event.target === addProductModal || event.target === editProductModal) {
            addProductModal.style.display = "none";
            editProductModal.style.display = "none";
        }
    });

    // Xử lý submit form thêm sản phẩm
    addProductForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Ngăn form reload trang
        const formData = new FormData(addProductForm);

        const productData = {
            name: formData.get("name"),
            description: formData.get("description"),
            price: parseFloat(formData.get("price")),
            categoryId: parseInt(formData.get("categoryId")),
            supplierId: parseInt(formData.get("supplierId")),
        };

        // Gửi request đến backend bằng fetch API
        fetch("/products/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                name: productData.name,
                description: productData.description,
                price: productData.price,
                categoryId: productData.categoryId,
                supplierId: productData.supplierId,
            }),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json(); // Giả sử backend trả về JSON
        })
        .then((data) => {
            console.log("Thêm sản phẩm thành công:", data);
            alert("Sản phẩm đã được thêm thành công!");

            // Cập nhật danh sách sản phẩm sau khi thêm
            location.reload();
        })
        .catch((error) => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi thêm sản phẩm " + error.message);
        });

        // Đóng modal và reset form
        addProductModal.style.display = "none";
        addProductForm.reset();
    });

    // Xử lý khi click vào dòng sản phẩm để sửa
    productTableBody.addEventListener("click", function (event) {
        const row = event.target.closest("tr");
        if (!row) return; // Nếu không phải dòng sản phẩm thì không làm gì

        const productId = row.cells[0].textContent.trim();
        const productName = row.cells[1].textContent.trim();
        const productDescription = row.cells[2].textContent.trim();
        let productPrice = row.cells[3].textContent.trim();
        productPrice = parseFloat(productPrice.replaceAll(".", "")); 
        const productCategory = row.cells[4].textContent.trim();
        const productSupplier = row.cells[5].textContent.trim();

        // Điền dữ liệu vào form sửa sản phẩm
        document.getElementById("editProductId").value = productId;
        document.getElementById("editProductName").value = productName;
        document.getElementById("editProductDescription").value = productDescription;
        document.getElementById("editProductPrice").value = productPrice;
        document.getElementById("editProductCategory").value = productCategory;
        document.getElementById("editProductSupplier").value = productSupplier;

        // Hiển thị modal sửa sản phẩm
        editProductModal.style.display = "flex";
    });

    // Xử lý submit form sửa sản phẩm
    editProductForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Ngăn form reload trang
        const formData = new FormData(editProductForm);

        const productData = {
            productId: formData.get("productId"),
            name: formData.get("name"),
            description: formData.get("description"),
            price: parseFloat(formData.get("price")),
            categoryId: formData.get("categoryId"),
            supplierId: formData.get("supplierId"),
        };

        // Gửi request đến backend bằng fetch API để cập nhật sản phẩm
        fetch(`/products/update`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                productId: productData.productId,
                name: productData.name,
                description: productData.description,
                price: productData.price,
                categoryId: productData.categoryId,
                supplierId: productData.supplierId,
            }),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json(); // Giả sử backend trả về JSON
        })
        .then((data) => {
            console.log("Cập nhật sản phẩm thành công:", data);
            alert("Sản phẩm đã được cập nhật thành công!");

            // Cập nhật dữ liệu trong bảng
            const updatedRow = productTableBody.querySelector(`tr[data-product-id="${productData.productId}"]`);
            if (updatedRow) {
                updatedRow.cells[1].textContent = productData.name;
                updatedRow.cells[2].textContent = productData.description;
                updatedRow.cells[3].textContent = productData.price.toLocaleString(); // Cập nhật giá
                updatedRow.cells[4].textContent = productData.categoryId;
                updatedRow.cells[5].textContent = productData.supplierId;
                updatedRow.cells[6].textContent = new Date().toLocaleDateString(); // Cập nhật ngày sửa
            }

            // Đóng modal và reset form
            editProductModal.style.display = "none";
            // Cập nhật danh sách sản phẩm sau khi sửa
            //location.reload();
        })
        .catch((error) => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi cập nhật sản phẩm: " + error.message);
        });
    });

    // Xử lý khi nhấn tìm kiếm
    searchButton.addEventListener("click", function () {
        const keyword = searchInput.value.trim();

        if (!keyword) {
            alert("Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        fetch(`/products/search?keyword=${encodeURIComponent(keyword)}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Server lỗi: ${response.status}`);
                }
                return response.json();
            })
            .then((products) => {
                productTableBody.innerHTML = ""; // Xóa dữ liệu cũ

                if (products.length === 0) {
                    alert("Không tìm thấy sản phẩm nào!");
                    return;
                }

                products.forEach((product) => {
                    const row = `
                        <tr>
                            <td>${product.productId}</td>
                            <td>${product.name}</td>
                            <td>${product.description}</td>
                            <td>${product.price.toLocaleString()}</td>
                            <td>${product.categoryId}</td>
                            <td>${product.supplierId}</td>
                            <td>${product.formattedDate}</td>
                        </tr>
                    `;
                    productTableBody.insertAdjacentHTML("beforeend", row);
                });
            })
            .catch((error) => {
                console.error("Lỗi:", error);
                alert("Đã xảy ra lỗi khi tìm kiếm: " + error.message);
            });
    });

});