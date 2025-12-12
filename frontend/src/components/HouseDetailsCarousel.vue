<script setup>
import 'vue3-carousel/carousel.css'
import { Carousel, Slide, Pagination, Navigation } from 'vue3-carousel'

const carouselConfig = {
  itemsToShow: 2.5,
  wrapAround: true
}
</script>

<template>
  <Carousel v-bind="carouselConfig">
    <Slide>
      1
    </Slide>
    <Slide>
      2
    </Slide>

    <template #addons>
      <Navigation />
      <Pagination />
    </template>
  </Carousel>
</template>
