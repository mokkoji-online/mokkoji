<template>
  <ov-video v-if="streamManager" :stream-manager="streamManager" :main-stream="mainStream" />
</template>

<script setup>
import { ref } from 'vue'
import OvVideo from './OvVideo.vue'

const props = defineProps({
  streamManager: {
    type: Object
  },
  mainStream: {
    type: Boolean
  },
  isFront: {
    type: Boolean
  }
})

const userName = ref('')
</script>

<style></style>
