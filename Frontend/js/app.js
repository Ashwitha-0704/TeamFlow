const API_BASE = 'http://localhost:8080/api';

function showAlert(message, type = 'success') {
    const alertDiv = document.createElement('div');
    alertDiv.className = `alert alert-${type} alert-dismissible fade show alert-floating`;
    alertDiv.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.body.appendChild(alertDiv);
    setTimeout(() => alertDiv.remove(), 4000);
}

async function apiCall(url, method = 'GET', body = null) {
    const options = {
        method,
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include'
    };
    if (body) options.body = JSON.stringify(body);

    const response = await fetch(API_BASE + url, options);

    if (response.status === 204) return null;

    const data = await response.json().catch(() => null);

    if (!response.ok) {
        const msg = data?.message || data?.error || 'Request failed';
        throw new Error(msg);
    }
    return data;
}

function formatDate(dateStr) {
    if (!dateStr) return '-';
    return new Date(dateStr).toLocaleDateString('en-US', {
        year: 'numeric', month: 'short', day: 'numeric'
    });
}

function formatDateTime(dateStr) {
    if (!dateStr) return '-';
    return new Date(dateStr).toLocaleString('en-US', {
        year: 'numeric', month: 'short', day: 'numeric',
        hour: '2-digit', minute: '2-digit'
    });
}

function statusBadge(status) {
    if (!status) return '-';
    const cls = status.toLowerCase().replace(/ /g, '_');
    return `<span class="badge badge-status badge-${cls}">${status.replace(/_/g, ' ')}</span>`;
}

function priorityBadge(priority) {
    if (!priority) return '-';
    return `<span class="badge badge-status badge-${priority.toLowerCase()}">${priority}</span>`;
}

function initSidebar() {
    // Determine the current filename from path (e.g. "dashboard.html")
    const pathParts = window.location.pathname.split('/');
    const currentFile = pathParts[pathParts.length - 1] || 'index.html';
    
    document.querySelectorAll('.sidebar-nav a').forEach(link => {
        const href = link.getAttribute('href');
        if (href === currentFile || (currentFile === 'index.html' && href === 'dashboard.html')) {
            link.classList.add('active');
        }
    });

    const toggle = document.querySelector('.menu-toggle');
    const sidebar = document.querySelector('.sidebar');
    if (toggle && sidebar) {
        toggle.addEventListener('click', () => sidebar.classList.toggle('show'));
    }
}

async function loadCurrentUser() {
    try {
        const user = await apiCall('/auth/me');
        const nameEl = document.getElementById('userName');
        const roleEl = document.getElementById('userRole');
        if (nameEl) nameEl.textContent = user.name;
        if (roleEl) roleEl.textContent = user.role;
        return user;
    } catch (e) {
        console.error('Failed to load user', e);
        if (!window.location.pathname.endsWith('login.html')) {
            window.location.href = 'login.html';
        }
    }
}

async function loadNotificationCount() {
    try {
        const data = await apiCall('/notifications/count');
        const badge = document.getElementById('notifBadge');
        if (badge && data.count > 0) {
            badge.textContent = data.count;
            badge.style.display = 'inline';
        }
    } catch (e) {
        console.error('Failed to load notification count', e);
    }
}

async function logout() {
    try {
        // Post to backend standard logout url
        await fetch('http://localhost:8080/logout', { method: 'POST', credentials: 'include' });
    } catch (e) {
        console.error('Logout error', e);
    }
    // Direct back to login page
    window.location.href = 'login.html';
}

document.addEventListener('DOMContentLoaded', () => {
    initSidebar();
    if (!window.location.pathname.endsWith('login.html')) {
        loadCurrentUser();
        loadNotificationCount();
    }
});
