<script setup>
import { VMap, VMapOsmTileLayer, VMapZoomControl } from 'vue-map-ui';
</script>

<template>
  <div class="rd-lg overflow-hidden shadow">
    <VMap :center="[40.737, -17.923]" :zoom="13" style="height: 500px;">
      <VMapOsmTileLayer />
      <VMapZoomControl />
    </VMap>
  </div>
</template>
