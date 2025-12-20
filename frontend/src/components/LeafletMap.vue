<script setup>
import { VMap, VMapOsmTileLayer, VMapZoomControl } from 'vue-map-ui';

const props = defineProps({
  center: {
    type: Array,
    default: () => [48.8566, 2.3522]
  },
  zoom: {
    type: Number,
    default: 18
  }
})
</script>

<template>
  <div class="rd-lg overflow-hidden shadow">
    <VMap :center="props.center" :zoom="props.zoom" style="height: 500px;">
      <VMapOsmTileLayer />
      <VMapZoomControl />
    </VMap>
  </div>
</template>
