async function renderHeader() {
    const resp = await fetch('/config/header.json');
    const cfg = await resp.json();
    const currentPath = window.location.pathname;
    const navHtml = cfg.navItems.map(item => {
        const isActive = (item.url === currentPath);
        const activeClass = isActive ? ' active' : '';
        return `<a href="${item.url}" class="nav-link${activeClass}">${item.name}</a>`;
    }).join('');
    const html = `
        <header class="main-header">
            <div class="header-inner">
                <div class="logo-area">
                    <span class="logo-icon">${cfg.logo.icon}</span>
                    <span class="logo-text">${cfg.logo.text}</span>
                </div>
                <nav class="main-nav">
                    ${navHtml}
                </nav>
                <div class="user-area"></div>
            </div>
        </header>
    `;
    document.getElementById('header-container').innerHTML = html;
}
document.addEventListener('DOMContentLoaded', renderHeader);