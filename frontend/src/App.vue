<script setup>
import { RouterLink, RouterView, useRoute } from 'vue-router'
import { ref, computed } from 'vue'


// 获取当前路由
const route = useRoute()

// 响应式数据
const isCollapsed = ref(false)

// 计算属性：根据当前路由动态显示页面标题
const pageTitle = computed(() => {
  const routeMap = {
    '/': '仪表盘',
    '/houses': '房屋管理',
    '/users': '用户管理',
    '/settings': '系统设置',
    '/profile': '个人资料'
  }
  return routeMap[route.path] || '管理系统'
})

// 方法：切换侧边栏状态
const toggleSidebar = () => {
  isCollapsed.value = !isCollapsed.value
}

// 方法：处理退出登录
const handleLogout = () => {
  if (confirm('确定要退出登录吗？')) {
    // 这里添加实际的退出登录逻辑
    console.log('退出登录')
    // 例如：清除token，跳转到登录页面等
  }
}
</script>
<template>
  <div class="layout">
    <!-- 侧边栏 -->
    <aside class="sidebar":class="{ 'collapsed': isCollapsed }">
      <div class="sidebar-header">
        <div class="logo-container">
          <div class="logo-icon">🏢</div>
          <h2>管理系统</h2>
        </div>
        <button class="collapse-btn" @click="toggleSidebar">
          {{ isCollapsed ? '→' : '←' }}
        </button>
      </div>
      
      <nav class="sidebar-nav">
        <div class="nav-section">
          <h3 class="section-title">主要</h3>
          <RouterLink to="/" class="nav-item" active-class="active">
            <div class="nav-item-inner">
              <span class="nav-icon">📊</span>
              <span class="nav-text">仪表盘</span>
            </div>
          </RouterLink>
          
          <RouterLink to="/houses" class="nav-item" active-class="active">
            <div class="nav-item-inner">
              <span class="nav-icon">🏠</span>
              <span class="nav-text">房屋管理</span>
            </div>
          </RouterLink>
          
          <RouterLink to="/users" class="nav-item" active-class="active">
            <div class="nav-item-inner">
              <span class="nav-icon">👥</span>
              <span class="nav-text">用户管理</span>
            </div>
          </RouterLink>
        </div>
        
        <div class="nav-section">
          <h3 class="section-title">设置</h3>
          <RouterLink to="/settings" class="nav-item" active-class="active">
            <div class="nav-item-inner">
              <span class="nav-icon">⚙️</span>
              <span class="nav-text">系统设置</span>
            </div>
          </RouterLink>
          
          <RouterLink to="/profile" class="nav-item" active-class="active">
            <div class="nav-item-inner">
              <span class="nav-icon">👤</span>
              <span class="nav-text">个人资料</span>
            </div>
          </RouterLink>
        </div>
      </nav>
      
      <div class="sidebar-footer">
        <div class="user-mini">
          <div class="user-avatar">A</div>
          <div class="user-details">
            <span class="user-name">管理员</span>
            <span class="user-role">系统管理员</span>
          </div>
        </div>
      </div>
    </aside>

    <!-- 主内容区 -->
    <div class="main-content" :class="{ 'collapsed': isCollapsed }">
      <header class="content-header">
        <div class="header-left">
          <div class="breadcrumb">
            <span class="breadcrumb-item">首页</span>
            <span class="breadcrumb-separator">/</span>
            <span class="breadcrumb-item current">{{ pageTitle }}</span>
          </div>
          <h1>{{ pageTitle }}</h1>
        </div>
        <div class="header-right">
          <div class="header-actions">
            <button class="header-action-btn notification-btn">
              <span class="action-icon">🔔</span>
              <span class="notification-badge">3</span>
            </button>
            <button class="header-action-btn">
              <span class="action-icon">🌙</span>
            </button>
          </div>
          
          <div class="user-info">
            <div class="user-dropdown">
              <div class="user-avatar-main">A</div>
              <div class="user-info-text">
                <span class="user-greeting">欢迎回来，</span>
                <span class="user-name-main">管理员</span>
              </div>
              <div class="dropdown-arrow">▼</div>
              
              <div class="dropdown-menu">
                <RouterLink to="/profile" class="dropdown-item">
                  <span class="dropdown-icon">👤</span>
                  <span>个人资料</span>
                </RouterLink>
                <RouterLink to="/settings" class="dropdown-item">
                  <span class="dropdown-icon">⚙️</span>
                  <span>系统设置</span>
                </RouterLink>
                <div class="dropdown-divider"></div>
                <button class="dropdown-item logout-item" @click="handleLogout">
                  <span class="dropdown-icon">🚪</span>
                  <span>退出登录</span>
                </button>
              </div>
            </div>
          </div>
        </div>
      </header>
      
      <div class="content-area">
        <div class="content-container">
          <RouterView />
        </div>
        
        <footer class="content-footer">
          <div class="footer-content">
            <span>© 2023 管理系统. 保留所有权利.</span>
            <div class="footer-links">
              <a href="#" class="footer-link">帮助</a>
              <a href="#" class="footer-link">隐私政策</a>
              <a href="#" class="footer-link">服务条款</a>
            </div>
          </div>
        </footer>
      </div>
    </div>
  </div>
</template>为它设计样式确保覆盖整个页面

<style scoped>
/* 重置和基础样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

/* 基础布局 - 修复全屏问题 */
.layout {
  display: flex;
  height: 100vh; /* 使用vh而不是min-height */
  width: 100vw;
  overflow: hidden; /* 防止滚动条出现在布局外部 */
  background: #f5f7fa;
  font-family: system-ui, -apple-system, sans-serif;
}

/* 侧边栏 - 修复 */
.sidebar {
  width: 260px;
  background: #1e293b;
  color: #cbd5e1;
  display: flex;
  flex-direction: column;
  transition: width 0.3s;
  flex-shrink: 0;
  height: 100vh; /* 确保侧边栏填满高度 */
  position: fixed; /* 固定定位 */
  left: 0;
  top: 0;
  z-index: 100;
}

.sidebar-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 16px;
  border-bottom: 1px solid #334155;
  height: 70px;
}

.logo-container {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 24px;
  background: #3b82f6;
  width: 36px;
  height: 36px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.logo-container h2 {
  color: white;
  font-size: 18px;
  font-weight: 600;
  margin: 0;
}

.collapse-btn {
  background: #475569;
  border: none;
  color: #cbd5e1;
  width: 32px;
  height: 32px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.2s;
}

.collapse-btn:hover {
  background: #64748b;
}

/* 导航 */
.sidebar-nav {
  flex: 1;
  padding: 20px 0;
  overflow-y: auto;
}

.nav-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 12px;
  color: #94a3b8;
  padding: 0 16px;
  margin-bottom: 8px;
  font-weight: 500;
}

.nav-item {
  display: block;
  padding: 10px 16px;
  margin: 4px 12px;
  color: #cbd5e1;
  text-decoration: none;
  border-radius: 6px;
  transition: background 0.2s;
}

.nav-item:hover {
  background: #334155;
}

.nav-item.active {
  background: #3b82f6;
  color: white;
}

.nav-item-inner {
  display: flex;
  align-items: center;
  gap: 12px;
}

.nav-icon {
  font-size: 18px;
  width: 24px;
}

.nav-text {
  font-size: 14px;
}

/* 侧边栏底部 */
.sidebar-footer {
  padding: 16px;
  border-top: 1px solid #334155;
}

.user-mini {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  width: 36px;
  height: 36px;
  background: #3b82f6;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}

.user-details {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: white;
}

.user-role {
  font-size: 12px;
  color: #94a3b8;
}

/* 主内容区 - 修复 */
.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
  margin-left: 260px; /* 与侧边栏宽度一致 */
  width: calc(100vw - 260px); /* 计算剩余宽度 */
  height: 100vh;
  transition: all 0.3s ease;
}

.main-content.collapsed {
  margin-left: 70px; /* 折叠后侧边栏宽度 */
  width: calc(100vw - 70px);
}

/* 折叠状态下的侧边栏样式 */
:deep(.sidebar.collapsed) {
  width: 70px;
}

:deep(.sidebar.collapsed .nav-text),
:deep(.sidebar.collapsed .user-details),
:deep(.sidebar.collapsed .section-title),
:deep(.sidebar.collapsed .logo-container h2) {
  display: none;
}

:deep(.sidebar.collapsed .collapse-btn) {
  margin: 0 auto;
}

/* 顶部栏 */
.content-header {
  height: 70px;
  background: white;
  border-bottom: 1px solid #e2e8f0;
  padding: 0 24px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: sticky;
  top: 0;
  z-index: 10;
}

.header-left h1 {
  font-size: 20px;
  font-weight: 600;
  color: #1e293b;
  margin: 4px 0;
}

.breadcrumb {
  font-size: 12px;
  color: #64748b;
}

.breadcrumb-separator {
  margin: 0 6px;
}

.breadcrumb-item.current {
  color: #3b82f6;
  font-weight: 500;
}

/* 右侧操作区域 */
.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.header-action-btn {
  position: relative;
  background: none;
  border: none;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  cursor: pointer;
  color: #64748b;
  transition: background 0.2s;
}

.header-action-btn:hover {
  background: #f1f5f9;
}

.notification-btn {
  position: relative;
}

.notification-badge {
  position: absolute;
  top: -2px;
  right: -2px;
  background: #ef4444;
  color: white;
  font-size: 10px;
  width: 16px;
  height: 16px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* 用户信息区域 */
.user-info {
  position: relative;
}

.user-dropdown {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;
}

.user-dropdown:hover {
  background: #f1f5f9;
}

.user-avatar-main {
  width: 36px;
  height: 36px;
  background: #3b82f6;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
}

.user-info-text {
  display: flex;
  flex-direction: column;
}

.user-greeting {
  font-size: 12px;
  color: #64748b;
}

.user-name-main {
  font-size: 14px;
  font-weight: 600;
  color: #1e293b;
}

.dropdown-arrow {
  font-size: 10px;
  color: #64748b;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 8px;
  background: white;
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  min-width: 180px;
  display: none;
  z-index: 100;
}

.user-dropdown:hover .dropdown-menu {
  display: block;
}

.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px 16px;
  color: #475569;
  text-decoration: none;
  transition: background 0.2s;
  font-size: 14px;
  border: none;
  background: none;
  width: 100%;
  text-align: left;
  cursor: pointer;
}

.dropdown-item:hover {
  background: #f8fafc;
}

.dropdown-divider {
  height: 1px;
  background: #e2e8f0;
  margin: 4px 0;
}

.logout-item {
  color: #ef4444;
}

/* 内容区域 - 修复 */
.content-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden; /* 防止溢出 */
  height: calc(100vh - 70px); /* 减去顶部栏高度 */
}

.content-container {
  flex: 1;
  padding: 24px;
  overflow-y: auto; /* 允许内容滚动 */
  background: #f8fafc;
  min-height: 0; /* 关键：允许flex容器缩小 */
}

/* 页脚 */
.content-footer {
  background: white;
  border-top: 1px solid #e2e8f0;
  padding: 16px 24px;
  flex-shrink: 0; /* 防止页脚被压缩 */
}

.footer-content {
  display: flex;
  align-items: center;
  justify-content: space-between;
  color: #64748b;
  font-size: 14px;
}

.footer-links {
  display: flex;
  gap: 20px;
}

.footer-link {
  color: #64748b;
  text-decoration: none;
  transition: color 0.2s;
}

.footer-link:hover {
  color: #3b82f6;
}

/* 确保路由视图内容正确显示 */
.content-container > * {
  min-height: 100%;
}
</style>