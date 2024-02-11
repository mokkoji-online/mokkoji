<template>
  <div v-if="mainStream" class="relative">
    <div
      class="absolute top-[90%] left-[3%] w-[20%] h-[8%] flex justify-center items-center text-white bg-black opacity-100 rounded-r-xl text-r-md"
    >
      {{ userName }}
    </div>
    <video ref="videoElement" autoplay class="w-full h-full rounded-[5.5vb]" />
  </div>
  <div v-else class="relative">
    <div
      class="absolute top-[80%] left-[3%] w-[35%] h-[15%] flex justify-center items-center bg-black rounded-r-xl"
    >
      <span class="text-white text-opacity-none text-[2vh]">{{ userName }}</span>
    </div>
    <video ref="videoElement" autoplay class="w-full h-full rounded-[3vb]" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import html2canvas from 'html2canvas'

const props = defineProps({
  streamManager: {
    type: Object
  },
  mainStream: {
    type: Boolean
  }
})

console.log(props.streamManager)

const userName = ref($cookies.get('user').name)
const videoElement = ref(null)

const captureImg = () => {
  const target = videoElement.value
  if (!target) {
    return alert('결과 저장 실패')
  }
  target.focus()
  html2canvas(target).then((canvas) => {
    onSaveAs(canvas.toDataURL('image/png'), 'test.png')
  })
}

const onSaveAs = (uri, filename) => {
  const link = document.createElement('a')
  document.body.appendChild(link)
  link.href = uri
  link.download = filename
  link.click()
  document.body.removeChild(link)
}

onMounted(() => {
  if (videoElement.value && props.streamManager) {
    props.streamManager.addVideoElement(videoElement.value)
  }
})
</script>

<style></style>
