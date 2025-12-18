<template>
  <div class="about-view">
    <h1>关于我们</h1>
    
    <div class="about-content">
      <div class="about-card">
        <h2>公司简介</h2>
        <p>我们是专业的房屋管理系统提供商，致力于为您提供最优质的房屋管理解决方案。</p>
      </div>
      
      <div class="about-card">
        <h2>我们的服务</h2>
        <ul>
          <li>房屋信息管理</li>
          <li>用户管理</li>
          <li>数据统计分析</li>
          <li>系统维护支持</li>
        </ul>
      </div>
      
      <div class="about-card">
        <h2>联系方式</h2>
        <p>电话: 400-123-4567</p>
        <p>邮箱: support@housemanagement.com</p>
        <p>地址: 北京市朝阳区某某大厦</p>
      </div>
    </div>
  </div>
</template>

<script setup>
// 关于页面逻辑
</script>

<style scoped>
.about-view {
  padding: 20px;
}

.about-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 30px;
}

.about-card {
  background: white;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.about-card h2 {
  color: #1890ff;
  margin-bottom: 15px;
  border-bottom: 2px solid #e6f7ff;
  padding-bottom: 8px;
}

.about-card p {
  line-height: 1.6;
  color: #666;
}

.about-card ul {
  list-style: none;
  padding: 0;
}

.about-card li {
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
  color: #666;
}

.about-card li:last-child {
  border-bottom: none;
}
</style>
