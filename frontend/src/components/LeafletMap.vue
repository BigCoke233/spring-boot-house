<script setup>
import { VMap, VMapOsmTileLayer, VMapZoomControl } from 'vue-map-ui';
</script>

<template>
  <div class="rd-lg overflow-hidden shadow">
    <VMap :center="[48.8566, 2.3522]" :zoom="18" style="height: 500px;">
      <VMapOsmTileLayer />
      <VMapZoomControl />
    </VMap>
  </div>
</template>
