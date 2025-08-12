document.addEventListener("DOMContentLoaded", function () {
    const addButton = document.getElementById("addButton");
    const addCategoryForm = document.getElementById("addCategoryForm");
    const addCategoryModal = document.getElementById("addCategoryModal");
    const closeModal = document.getElementById("closeModal");
    const closeEditModal = document.getElementById("closeEditModal");
    const editCategoryModal = document.getElementById("editCategoryModal");
    const editCategoryForm = document.getElementById("editCategoryForm");
    const searchInput = document.getElementById("searchInput");
    const searchButton = document.getElementById("searchButton");
    const categoryTableBody = document.querySelector("#categoryTable tbody");

    // Hiển thị modal khi nhấn
    addButton.addEventListener("click", function () {
        addCategoryModal.style.display = "flex";
    });

    // Đóng modal thêm
    closeModal.addEventListener("click", function () {
        addCategoryModal.style.display = "none";
    });

    // Đóng modal sửa
    closeEditModal.addEventListener("click", function () {
        editCategoryModal.style.display = "none";
    });

    // Đóng modal khi click ra ngoài modal
    window.addEventListener("click", function (event) {
        if (event.target === addCategoryModal || event.target === editCategoryModal) {
            addCategoryModal.style.display = "none";
            editCategoryModal.style.display = "none";
        }
    });

    // Xử lý submit form thêm
    addCategoryForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Ngăn form reload trang
        const formData = new FormData(addCategoryForm);

        const categoryData = {
            name: formData.get("name"),
        };

        // Gửi request đến backend bằng fetch API
        fetch("/categories/add", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                name: categoryData.name,
            }),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json(); // Giả sử backend trả về JSON
        })
        .then((data) => {
            console.log("Thêm loại thành công:", data);
            alert("Loại sản phẩm đã được thêm thành công!");

            // Cập nhật danh sách sau khi thêm
            location.reload();
        })
        .catch((error) => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi thêm loại sản phẩm " + error.message);
        });

        // Đóng modal và reset form
        addCategoryModal.style.display = "none";
        addCategoryForm.reset();
    });

    // Xử lý khi click vào dòng để sửa
    categoryTableBody.addEventListener("click", function (event) {
        const row = event.target.closest("tr");
        if (!row) return; // Nếu không phải dòng thì không làm gì

        const categoryId = row.cells[0].textContent.trim();
        const categoryName = row.cells[1].textContent.trim();

        // Điền dữ liệu vào form sửa 
        document.getElementById("editCategoryId").value = categoryId;
        document.getElementById("editCategoryName").value = categoryName;

        // Hiển thị modal sửa
        editCategoryModal.style.display = "flex";
    });

    // Xử lý submit form sửa
    editCategoryForm.addEventListener("submit", function (event) {
        event.preventDefault(); // Ngăn form reload trang
        const formData = new FormData(editCategoryForm);

        const categoryData = {
            categoryId: formData.get("categoryId"),
            name: formData.get("name"),
        };

        // Gửi request đến backend bằng fetch API để cập nhật
        fetch(`/categories/update`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                categoryId: categoryData.categoryId,
                name: categoryData.name,
            }),
        })
        .then((response) => {
            if (!response.ok) {
                throw new Error(`Server lỗi: ${response.status}`);
            }
            return response.json(); // Giả sử backend trả về JSON
        })
        .then((data) => {
            console.log("Cập nhật loại sản phẩm thành công:", data);
            alert("Loại sản phẩm đã được cập nhật thành công!");

            // Cập nhật dữ liệu trong bảng
            const updatedRow = categoryTableBody.querySelector(`tr[data-category-id="${categoryData.categoryId}"]`);
            if (updatedRow) {
                updatedRow.cells[1].textContent = categoryData.name;
            }

            // Đóng modal và reset form
            editCategoryModal.style.display = "none";
            // Cập nhật danh sách sau khi sửa
            //location.reload();
        })
        .catch((error) => {
            console.error("Lỗi:", error);
            alert("Đã xảy ra lỗi khi cập nhật loại sản phẩm: " + error.message);
        });
    });

    // Xử lý khi nhấn tìm kiếm
    searchButton.addEventListener("click", function () {
        const keyword = searchInput.value.trim();

        if (!keyword) {
            alert("Vui lòng nhập từ khóa tìm kiếm!");
            return;
        }

        fetch(`/categories/search?keyword=${encodeURIComponent(keyword)}`)
            .then((response) => {
                if (!response.ok) {
                    throw new Error(`Server lỗi: ${response.status}`);
                }
                return response.json();
            })
            .then((categories) => {
                categoryTableBody.innerHTML = ""; // Xóa dữ liệu cũ

                if (categories.length === 0) {
                    alert("Không tìm thấy loại sản phẩm nào!");
                    return;
                }

                categories.forEach((category) => {
                    const row = `
                        <tr>
                            <td>${category.categoryId}</td>
                            <td>${category.name}</td>
                        </tr>
                    `;
                    categoryTableBody.insertAdjacentHTML("beforeend", row);
                });
            })
            .catch((error) => {
                console.error("Lỗi:", error);
                alert("Đã xảy ra lỗi khi tìm kiếm: " + error.message);
            });
    });

});