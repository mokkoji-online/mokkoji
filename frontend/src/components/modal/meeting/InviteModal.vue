<template>
  <div class="fixed w-[24%] h-[17%] top-[11%] right-[0.9%]">
    <div
      class="pb-[1vh] h-[100%] bg-[#E7C6FF] opacity-95 flex flex-col justify-center items-center rounded-r-lg"
    >
      <div class="w-full flex items-center">
        <div class="w-[70%] font-bold text-r-sm self-start ml-[0.7vw] my-[1vh]">
          참여자 초대하기
        </div>
        <button
          class="ml-auto mr-[1vh] w-[8%] aspect-square rounded-full hover:bg-white"
          @click="$emit('remove-invite-modal')"
        >
          <IconCancelBlack class="size-[50%]" />
        </button>
      </div>
      <div class="mb-[1vh] text-r-sm">
        행사 주소를 복사한 후, 초대하고 싶은 친구에게 보내주세요.
      </div>
      <div
        class="p-[0.5vw] bg-white text-r-sm w-[95%] border-[0.1vb] border-gray rounded-r-lg flex justify-center items-center"
      >
        <div class="w-[95%] overflow-hidden">
          {{ address }}
        </div>
        <button
          class="flex justify-center items-center w-[8%] aspect-square rounded-full hover:hover:bg-slate-200"
        >
          <IconCopyBlack class="size-[80%]" @click="copyAddress" />
        </button>
      </div>
    </div>
    <transition-group name="up">
      <div v-if="isAddressCopy" class="absolute w-[24%] h-[25%] bottom-[38%] right-[5%]">
        <div class="h-[100%] bg-neutral-500 flex flex-col justify-center items-center rounded-r-lg">
          <div class="font-bold text-white text-r-sm">복사 완료</div>
        </div>
      </div>
    </transition-group>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import IconCancelBlack from '@/icons/meeting/IconCancelBlack.vue'
import IconCopyBlack from '@/icons/meeting/IconCopyBlack.vue'

defineEmits(['remove-invite-modal'])

const address = ref(`https://mokkoji.online/meetings/${sessionStorage.getItem('sessionId')}`)
const isAddressCopy = ref(false)

const copyAddress = () => {
  navigator.clipboard.writeText(address.value)

  isAddressCopy.value = true

  setTimeout(() => {
    isAddressCopy.value = false
  }, 700)
}
</script>

<style></style>
