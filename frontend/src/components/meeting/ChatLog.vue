<template>
  <div class="w-full flex flex-wrap justify-center">
    <div
      id="chat-container"
      v-for="chatMessage in props.chatLog"
      :key="chatMessage"
      class="p-[2vh] bg-white w-[90%] flex rounded-r-lg inline-block items-center font-semibold mb-[1vh] border-sm border-purple-300"
    >
      <div class="text-r-sm h-full max-w-[80%] inline-block">
        {{ chatMessage.sender + ' : ' + chatMessage.content }}
      </div>
      <div class="text-[0.8vw] ml-auto w-[20%] self-start">
        {{ chatMessage.time }}
      </div>
    </div>
  </div>
</template>

<script setup>
const props = defineProps({
  chatLog: {
    type: Array
  }
})
</script>

<style scoped></style>
