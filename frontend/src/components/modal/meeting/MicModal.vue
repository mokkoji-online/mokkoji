<template>
  <div class="fixed w-[10%] h-[8%] top-[2%] left-[45%]">
    <div
      v-if="isMic"
      id="gift-container"
      class="h-[100%] bg-blue-400 flex flex-col justify-center items-center rounded-r-lg"
    >
      <div class="text-r-md text-white font-bold">마이크 ON</div>
    </div>
    <div
      v-else
      id="gift-container"
      class="h-[100%] bg-red-400 flex flex-col justify-center items-center rounded-r-lg"
    >
      <div class="text-r-md text-white font-bold">마이크 OFF</div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  isMic: {
    type: Boolean
  }
})
</script>

<style scoped></style>
