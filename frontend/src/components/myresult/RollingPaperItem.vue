<template>
  <div class="absolute w-[100px] h-[100px] bottom-[100px] right-[100px] overflow-hidden">
    <p class="text-center text-[8px] mb-1">From {{ msg[0].name }}</p>
    <p class="text-[9px] whitespace-pre-wrap">{{ msg[0].content }}</p>
  </div>
  <div class="absolute w-[100px] h-[100px] bottom-[100px] left-[-45px] overflow-hidden">
    <p class="text-center text-[8px] mb-1">From {{ msg[1].name }}</p>
    <p class="text-[9px] whitespace-pre-wrap">{{ msg[1].content }}</p>
  </div>
  <div class="absolute w-[100px] h-[100px] bottom-[100px] left-[110px] overflow-hidden">
    <p class="text-center text-[8px] mb-1">From {{ msg[2].name }}</p>
    <p class="text-[9px] whitespace-pre-wrap">{{ msg[2].content }}</p>
  </div>
  <div class="absolute w-[100px] h-[100px] top-[120px] left-[110px] overflow-hidden">
    <p class="text-center text-[8px] mb-1">From {{ msg[3].name }}</p>
    <p class="text-[9px] whitespace-pre-wrap">{{ msg[3].content }}</p>
  </div>
  <div class="absolute w-[100px] h-[100px] bottom-[-60px] left-[110px] overflow-hidden">
    <p class="text-center text-[8px] mb-1">From {{ msg[4].name }}</p>
    <p class="text-[9px] whitespace-pre-wrap">
      {{ msg[4].content }}
    </p>
  </div>
  <div class="absolute w-[100px] h-[100px] bottom-[-60px] left-[-45px] overflow-hidden">
    <p class="text-center text-[8px] mb-1">From {{ msg[5].name }}</p>
    <p class="text-[9px] whitespace-pre-wrap">{{ msg[5].content }}</p>
  </div>
  <div class="absolute w-[100px] h-[100px] bottom-[-60px] right-[100px] overflow-hidden">
    <p class="text-center text-[8px] mb-1">From {{ msg[6].name }}</p>
    <p class="text-[9px] whitespace-pre-wrap">{{ msg[6].content }}</p>
  </div>
  <div class="absolute w-[100px] h-[100px] top-[120px] right-[100px] overflow-hidden">
    <p class="text-center text-[8px] mb-1">From {{ msg[7].name }}</p>
    <p class="text-[9px] whitespace-pre-wrap">{{ msg[7].content }}</p>
  </div>
  <div class="absolute w-[100px] h-[100px] top-[120px] left-[-45px] overflow-hidden">
    <p class="text-center text-[8px] mb-1">From {{ msg[8].name }}</p>
    <p class="text-[9px] whitespace-pre-wrap">{{ msg[8].content }}</p>
  </div>
</template>
<script setup>
const props = defineProps(['msg'])
</script>
