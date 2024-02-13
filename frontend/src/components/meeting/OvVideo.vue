<template>
  <div v-if="mainStream" class="relative w-full h-full top-0 left-0">
    <video ref="videoElement" autoplay class="w-full h-full rounded-[5vb] object-cover"></video>
    <div
      class="absolute top-[90%] left-[2%] w-[20%] h-[8%] flex justify-center items-center text-white bg-black opacity-100 rounded-r-xl text-r-md"
    >
      {{ userName }}
    </div>
    <button
      class="absolute bg-black text-white top-[90%] left-[90%] w-[7%] h-[8%] text-r-sm rounded-r-xl hover:bg-neutral-800"
      @click="$emit('toggle-camera')"
    >
      <IconScreenChange class="size-[70%]" />
    </button>
  </div>
  <div v-else class="relative w-full h-1/3 top-0 left-0">
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
import IconScreenChange from '@/icons/meeting/IconScreenChange.vue'

defineEmits(['toggle-camera'])

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

const clientData = () => {
  const { clientData } = getConnectionData()
  userName.value = clientData
}

// 사용자 데이터 가져오기
const getConnectionData = () => {
  const { connection } = props.streamManager.stream
  return JSON.parse(connection.data)
}

onMounted(() => {
  if (videoElement.value && props.streamManager) {
    props.streamManager.addVideoElement(videoElement.value)
  }

  clientData()
})
</script>

<style></style>
