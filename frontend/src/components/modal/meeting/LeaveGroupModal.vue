<template>
  <div
    class="fixed w-[10%] aspect-[1.5] bottom-[11%] left-[78%] flex flex-col justify-center items-center bg-red-100 rounded-r-lg"
  >
    <div class="w-full h-1/2 flex items-center border-b-[0.1vb] border-zinc-400">
      <button
        class="w-full h-full border-sm font-bold text-r-sm rounded-r-lg hover:bg-red-200"
        @click="$emit('leave-group-meeting')"
      >
        소그룹 나가기
      </button>
    </div>
    <div class="w-full h-1/2 flex items-center">
      <button
        class="w-full h-full font-bold text-r-sm rounded-r-lg hover:bg-red-200"
        @click="$emit('leave-main-meeting')"
      >
        행사 나가기
      </button>
    </div>
  </div>
</template>

<script setup>
defineEmits(['leave-main-meeting']['leave-group-meeting'])
</script>

<style>
.bg-red-200 {
  background-color: #f9dcda;
}
</style>
