<template>
  <ov-video v-if="streamManager" :stream-manager="streamManager" :main-stream="mainStream" />
</template>

<script setup>
import OvVideo from './OvVideo.vue'

const props = defineProps({
  streamManager: {
    type: Object
  },
  mainStream: {
    type: Boolean
  }
})
</script>

<style></style>
