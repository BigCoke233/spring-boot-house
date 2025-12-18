<template>
  <div class="houses-view">
    <h1>房屋管理</h1>
    <div class="houses-content">
      <p>这里是房屋管理页面，可以查看和管理所有房屋信息。</p>
      
      <div class="houses-list">
        <div class="house-card" v-for="house in houses" :key="house.id">
          <h3>{{ house.title }}</h3>
          <p>{{ house.description }}</p>
          <div class="house-info">
            <span>价格: {{ house.price }}元/月</span>
            <span>面积: {{ house.area }}㎡</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 模拟房屋数据
const houses = ref([
  {
    id: 1,
    title: '阳光花园小区 3室2厅',
    description: '南北通透，采光好，近地铁',
    price: 3500,
    area: 120
  },
  {
    id: 2,
    title: '世纪城公寓 2室1厅',
    description: '精装修，拎包入住',
    price: 2800,
    area: 85
  },
  {
    id: 3,
    title: '山水名都 4室2厅',
    description: '豪华装修，小区环境优美',
    price: 5000,
    area: 150
  }
])
</script>

<style scoped>
.houses-view {
  padding: 20px;
}

.houses-content {
  max-width: 1200px;
  margin: 0 auto;
}

.houses-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 30px;
}

.house-card {
  background: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

.house-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.house-card h3 {
  margin: 0 0 10px 0;
  color: #333;
}

.house-card p {
  color: #666;
  margin: 0 0 15px 0;
  line-height: 1.5;
}

.house-info {
  display: flex;
  justify-content: space-between;
  color: #1890ff;
  font-weight: 500;
}
</style>