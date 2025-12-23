<!-- components/AdminHeader.vue -->
<template>
  <header class="admin-header">
    <div class="header-left">
      <h1 class="logo">🏢 房屋管理系统</h1>
      <div class="page-title">
        <span class="breadcrumb">首页 /</span>
        <span class="current-page">管理员面板</span>
      </div>
    </div>
    
    <div class="header-right">
      <div class="admin-info">
        <div class="admin-avatar">A</div>
        <div class="admin-details">
          <div class="admin-name">超级管理员</div>
          <div class="admin-role">系统管理员</div>
        </div>
        <button class="logout-btn" @click="$emit('logout')">退出</button>
      </div>
    </div>
  </header>
</template>

<script setup>
defineEmits(['logout'])
</script>

<style scoped>
.admin-header {
  background: white;
  border-bottom: 1px solid #e4e7ed;
  padding: 0 20px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 20px;
}

.logo {
  font-size: 18px;
  color: #303133;
  margin: 0;
}

.page-title {
  color: #606266;
  font-size: 14px;
}

.breadcrumb {
  color: #909399;
}

.current-page {
  color: #303133;
  font-weight: 500;
  margin-left: 4px;
}

.admin-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.admin-avatar {
  width: 36px;
  height: 36px;
  background: #409eff;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.admin-details {
  text-align: right;
}

.admin-name {
  font-size: 14px;
  color: #303133;
  font-weight: 500;
}

.admin-role {
  font-size: 12px;
  color: #909399;
}

.logout-btn {
  padding: 6px 16px;
  border: 1px solid #dcdfe6;
  background: white;
  border-radius: 4px;
  color: #606266;
  cursor: pointer;
  font-size: 12px;
  transition: all 0.3s;
}

.logout-btn:hover {
  border-color: #409eff;
  color: #409eff;
}
</style>