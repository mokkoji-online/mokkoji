<template>
  <div v-if="mainStream" class="relative w-full h-full top-0 left-0">
    <video ref="videoElement" autoplay class="w-full h-full rounded-[5vb] object-cover"></video>
    <div
      class="absolute top-[90%] left-[2%] w-[20%] h-[8%] flex justify-center items-center text-white bg-black opacity-100 rounded-r-xl text-r-md"
    >
      {{ userName }}
    </div>
  </div>
  <div v-else class="relative w-full top-0 left-0">
    <video ref="videoElement" autoplay class="w-full h-full rounded-[3vb] object-cover" />
    <div
      class="absolute top-[80%] left-[3%] w-[35%] h-[15%] flex justify-center items-center bg-black rounded-r-xl text-white text-r-sm"
    >
      {{ userName }}
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const props = defineProps({
  streamManager: {
    type: Object
  },
  mainStream: {
    type: Boolean
  }
})

const userName = ref()
const videoElement = ref(null)

// 사용자 이름 가져오기
const getUserName = () => {
  const { clientData } = getConnectionData()
  userName.value = clientData
}

// 연결돼 있는 사용자 정보 가져오기
const getConnectionData = () => {
  const { connection } = props.streamManager.stream
  return JSON.parse(connection.data)
}

onMounted(() => {
  getUserName()

  if (videoElement.value && props.streamManager) {
    props.streamManager.addVideoElement(videoElement.value)
  }
})
</script>

<style></style>
