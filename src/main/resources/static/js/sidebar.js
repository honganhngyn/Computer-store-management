document.addEventListener("DOMContentLoaded", () => {
    const userDropdown = document.querySelector(".user-dropdown");
    const userMenu = document.getElementById("user-menu");
  
    // Thêm sự kiện hover để hiển thị menu
    if (userDropdown) {
      userDropdown.addEventListener("mouseenter", () => {
        userMenu.classList.remove("hidden");
      });
  
      userDropdown.addEventListener("mouseleave", () => {
        userMenu.classList.add("hidden");
      });
    }
  });