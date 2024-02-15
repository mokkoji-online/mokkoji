<template>
  <div class="fixed w-[18%] h-[23%] bottom-[15%] left-[53%]">
    <div
      class="relative w-full h-[100%] p-[2.5vh] bg-purple-100 flex-col justify-center items-center rounded-r-lg text-r-sm"
    >
      <div class="text-center mb-[1vh]">
        <p>사진을 찍으시면 호스트에게</p>
        <p>소중한 추억을 보낼 수 있어요!</p>
      </div>
      <div class="w-full h-full flex justify-center">
        <div class="w-1/2 flex flex-col items-center">
          <button
            class="mb-[0.5vh] w-[50%] aspect-square bg-purple-300 hover:bg-purple-400 rounded-full"
            @click="$emit('capture-my-video')"
          >
            <IconMyVideo class="size-[70%]" />
          </button>
          <div>개인 사진</div>
        </div>
        <div class="w-1/2 flex flex-col items-center">
          <button
            class="mb-[0.5vh] w-[50%] aspect-square bg-purple-300 hover:bg-purple-400 rounded-full"
            @click="$emit('capture-group-video')"
          >
            <IconGroupVideo class="size-[70%]" />
          </button>
          <div>단체 사진</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import IconMyVideo from '@/icons/meeting/IconMyVideo.vue'
import IconGroupVideo from '@/icons/meeting/IconGroupVideo.vue'

defineEmits(['capture-my-video', 'capture-group-video'])
</script>

<style></style>
