<template>
  <div class="fixed w-[24%] h-[17%] top-[11%] right-[0.9%]">
    <div
      id="gift-container"
      class="pb-[1vh] h-[100%] bg-[#E7C6FF] opacity-95 flex flex-col justify-center items-center rounded-r-lg"
    >
      <div class="font-bold text-r-sm self-start ml-[0.7vw] my-[1vh]">참여자 초대하기</div>
      <div class="mb-[1vh] text-r-sm">
        회의 주소를 복사한 후, 초대하고 싶은 친구에게 보내주세요.
      </div>
      <div
        class="p-[0.5vw] bg-white text-r-sm w-[95%] border-[0.1vb] border-gray rounded-r-lg flex justify-center items-center"
      >
        <div class="w-[95%] overflow-hidden">
          {{ address }}
        </div>
        <div>
          <IconCopyBlack class="size-[80%] hover:cursor-pointer" @click="copyAddress" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import IconCopyBlack from '@/icons/meeting/IconCopyBlack.vue'

const address = ref(`https://mokkoji.online/meetings/${sessionStorage.getItem('sessionId')}`)

const copyAddress = (sessionInfo) => {
  navigator.clipboard.writeText(address.value)
}
</script>

<style></style>
