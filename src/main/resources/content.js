chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
    if (message.action === "processCodes") {
        const codes = message.codes;

        // Lặp qua các mã nạp
        codes.forEach(code => {
            // Lặp qua các số từ 0 đến 9 để thêm vào mã
            for (let digit = 0; digit <= 9; digit++) {
                let modifiedCode = code + digit;

                // Tìm trường nhập mã nạp thẻ
                const inputField = document.querySelector('.oxVbmPqVSkCVx79GnnLc7');
                if (inputField) {
                    // Xóa trường nhập nếu có dữ liệu cũ
                    inputField.value = '';
                    // Nhập mã nạp đã được thêm số
                    inputField.value = modifiedCode;

                    // Gửi form (hoặc nhấn Enter)
                    inputField.dispatchEvent(new KeyboardEvent('keydown', { key: 'Enter' }));

                    // Tìm nút "Xác nhận" và nhấn vào đó
                    const confirmButton = document.querySelector('input._3duKww4d68rWsj1YAVEbYt[type="submit"]');
                    if (confirmButton) {
                        confirmButton.click();  // Nhấn nút xác nhận
                    }
                }
            }
        });
    }
});
