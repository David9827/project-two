document.getElementById('submitBtn').addEventListener('click', () => {
    const codes = document.getElementById('codes').value.split(',').map(code => code.trim());

    // Gửi các mã đã nhập đến content script
    chrome.tabs.query({ active: true, currentWindow: true }, (tabs) => {
        chrome.tabs.sendMessage(tabs[0].id, { action: 'processCodes', codes: codes });
    });
});
