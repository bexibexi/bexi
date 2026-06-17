/**
 * 公共JavaScript文件
 */

// 格式化日期
function formatDate(dateStr) {
    if (!dateStr) return '';
    var date = new Date(dateStr);
    var year = date.getFullYear();
    var month = String(date.getMonth() + 1).padStart(2, '0');
    var day = String(date.getDate()).padStart(2, '0');
    var hours = String(date.getHours()).padStart(2, '0');
    var minutes = String(date.getMinutes()).padStart(2, '0');
    return year + '-' + month + '-' + day + ' ' + hours + ':' + minutes;
}

// 确认删除
function confirmDelete(message) {
    return confirm(message || '确定要删除吗？删除后不可恢复！');
}

// 显示提示消息
function showAlert(type, message, duration) {
    duration = duration || 3000;
    var alertDiv = document.createElement('div');
    alertDiv.className = 'alert alert-' + type + ' alert-dismissible fade show position-fixed';
    alertDiv.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
    alertDiv.innerHTML = message +
        '<button type="button" class="btn-close" data-bs-dismiss="alert"></button>';
    document.body.appendChild(alertDiv);

    setTimeout(function() {
        alertDiv.remove();
    }, duration);
}

// AJAX请求封装
function ajax(options) {
    var xhr = new XMLHttpRequest();
    xhr.open(options.method || 'GET', options.url, true);
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');

    xhr.onload = function() {
        if (xhr.status === 200) {
            try {
                var response = JSON.parse(xhr.responseText);
                if (options.success) {
                    options.success(response);
                }
            } catch (e) {
                if (options.success) {
                    options.success(xhr.responseText);
                }
            }
        } else {
            if (options.error) {
                options.error(xhr.statusText);
            }
        }
    };

    xhr.onerror = function() {
        if (options.error) {
            options.error('网络错误');
        }
    };

    if (options.data) {
        var params = [];
        for (var key in options.data) {
            if (options.data.hasOwnProperty(key)) {
                params.push(encodeURIComponent(key) + '=' + encodeURIComponent(options.data[key]));
            }
        }
        xhr.send(params.join('&'));
    } else {
        xhr.send();
    }
}

// 页面加载完成后执行
document.addEventListener('DOMContentLoaded', function() {
    // 自动关闭提示框
    var alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            var bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        }, 5000);
    });
});
