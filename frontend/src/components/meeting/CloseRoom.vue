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
          <img src="@/assets/logo/mokkoji_logo.png" alt="" class="w-[50%] opacity-10 z-0" />
        </div>
        <div
          class="w-full h-full pr-[7vh] flex flex-col justify-center items-center right-[8%] text-r-md font-bold"
        >
          <h1 class="text-[2.6lvw]">호스트가 행사를 종료했어요.</h1>
          <h1 class="text-[2.6lvw]">다음에 또 놀러오세요!</h1>
          <br />
          <button
            class="w-[20%] aspect-[3] outline outline-[0.5vh] outline-purple-400 rounded-r-lg hover:bg-purple-400 z-10"
            @click="goToHome"
          >
            메인화면 이동
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onBeforeMount } from 'vue'
import { useRouter } from 'vue-router'

const emit = defineEmits(['waiting-room']['leave-meeting'])

const router = useRouter()

const goToHome = () => {
  router.push('/')
}

onBeforeMount(() => {
  emit('leave-meeting')
})

onMounted(() => {
  emit('waiting-room')
})
</script>

<style scoped></style>
