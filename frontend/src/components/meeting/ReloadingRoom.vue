<template>
  <div
    class="flex items-center justify-center w-full h-screen text-gray-900 px-[2vw]"
    id="main-gradient3"
  >
    <div
      class="flex flex-col justify-center items-center w-full h-3/4 mx-[14vw] bg-white rounded-r-xl shadow-md border-sm border-yellow-100"
    >
      <div class="relative w-full h-[100%] flex items-center">
        <div class="absolute w-full h-full flex justify-center items-center">
          <img src="@/assets/logo/mokkoji_logo.png" alt="" class="h-[90%] opacity-10 z-0" />
        </div>
        <div
          class="w-full h-full pr-[7vh] flex flex-col justify-center items-center right-[8%] text-r-md font-bold"
        >
          <h1 class="text-[2.6lvw]">행사에 참여하는 사람이 존재하지 않아</h1>
          <h1 class="text-[2.6lvw]">행사가 종료됐어요.</h1>
          <br />
          <button
            class="w-[20%] aspect-[3] bg-purple-300 rounded-r-lg hover:bg-purple-400 z-10"
            @click="joinMeeting"
          >
            다시 생성하기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onBeforeMount } from 'vue'
import { useSessionStore } from '@/stores/meeting'

const emit = defineEmits(['waiting-room']['leave-meeting'])

const store = useSessionStore()

const joinMeeting = async () => {
  store.createSession()
}

onMounted(() => {
  emit('waiting-room')
})

onBeforeMount(() => {
  emit('leave-meeting')
})
</script>

<style></style>
