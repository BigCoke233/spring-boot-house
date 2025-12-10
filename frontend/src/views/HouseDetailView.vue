<script setup>
import LeafletMap from '@/components/LeafletMap.vue';
import PageContainer from '@/layouts/PageContainer.vue';

const detail = {
  name: 'House Detail',
  description: 'Detailed information about the house',
  address: '123 Main St',
  price: '$500,000',
  sqm: 1500,
  image: '/house.jpg'
}
</script>

<template>
  <PageContainer class="my-30">
    <header class="grid grid-cols-2 gap-4 md:gap-10">
      <div class="">
        <LeafletMap :center="[47.41322, -1.219482]" />
      </div>
      <div class>
        <h1>{{ detail.name }}</h1>
        <p>{{ detail.address }}</p>
        <p>{{ detail.price }}</p>
        <p>{{ detail.sqm }} sqm</p>
      </div>
    </header>
    <section>
      <p>{{ detail.description }}</p>
    </section>
  </PageContainer>
</template>
