<template>
  <ov-video
    v-if="streamManager"
    :stream-manager="streamManager"
    :main-stream="mainStream"
    @toggle-camera="toggleCamera"
  />
</template>

<script setup>
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

const emit = defineEmits(['toggle-camera'])

const toggleCamera = () => {
  emit('toggle-camera')
}
</script>

<style></style>
