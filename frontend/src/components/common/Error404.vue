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
          class="w-full h-full flex flex-col py-[10vh] justify-center items-center text-r-md font-bold z-10"
        >
          <p class="text-[2.6lvw] mb-[2vh] font-semibold text-center">페이지를 찾을 수 없어요.</p>
          <p class="text-[1.8lvw] text-center">
            존재하지 않는 페이지를 입력하셨거나, 요청하신 페이지가 변경 또는 이동했어요.
          </p>
          <div class="mt-[3vh] w-full flex justify-center gap-[1vw]">
            <a
              @click="$router.go(-1)"
              class="w-[18%] aspect-[3] flex justify-center items-center text-r-md rounded-r-lg text-black border-sm border-indigo-500 hover:bg-indigo-500 hover:text-white hover:cursor-pointer"
            >
              이전 페이지로 이동
            </a>
            <a
              href="/"
              class="w-[10%] aspect-[3] flex justify-center items-center text-r-md rounded-r-lg text-black border-sm border-green-500 hover:bg-green-500 hover:text-white"
            >
              홈으로
            </a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, onBeforeMount } from 'vue'
// 페이지 랜딩 시 header 제거 처리 emit
const emit = defineEmits(['waiting-room']['load-home'])

onMounted(() => {
  emit('waiting-room')
})

onBeforeMount(() => {
  emit('load-home')
})
</script>

<style scoped></style>
