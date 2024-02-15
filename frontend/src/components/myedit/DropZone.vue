<template>
  <div
    @dragenter.prevent="setActive"
    @dragover.prevent="setActive"
    @dragleave.prevent="setInactive"
    @drop.prevent="onDrop"
  >
    <slot :dropZoneActive="active"></slot>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
const emit = defineEmits(['files-dropped'])
const active = ref(false)
let inActiveTimeout = null
// 파일 드래그앤 드롭
const setActive = () => {
  active.value = true
  clearTimeout(inActiveTimeout)
}
const setInactive = () => {
  inActiveTimeout = setTimeout(() => {
    active.value = false
  }, 50)
}

const onDrop = (e) => {
  setInactive()
  emit('files-dropped', [...e.dataTransfer.files])
}

const preventDefaults = (e) => {
  e.preventDefault()
}

const events = ['dragenter', 'dragover', 'dragleave', 'drop']

onMounted(() => {
  events.forEach((eventName) => {
    document.body.addEventListener(eventName, preventDefaults)
  })
})

onUnmounted(() => {
  events.forEach((eventName) => {
    document.body.removeEventListener(eventName, preventDefaults)
  })
})
</script>

<style scoped></style>
