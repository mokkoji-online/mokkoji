<template>
  <div class="fixed w-[26%] aspect-auto bottom-[11%] left-[13%]">
    <div
      class="p-[1vw] pl-[1.5vw] h-[100%] bg-black flex flex-col justify-center items-start space-y-[1vh] rounded-r-lg"
    >
      <div class="flex w-[23vw] items-center h-[5vh]">
        <div class="text-white text-r-md">{{ host }}님의 행사 정보</div>
        <button
          class="ml-auto w-[2.5vw] aspect-square size-[100%] flex justify-center items-center hover:bg-neutral-500 rounded-full"
        >
          <IconCancelWhite class="size-[40%]" @click="$emit('remove-detail-modal')" />
        </button>
      </div>
      <div class="text-white text-r-sm">
        <div>행사 주소 | {{ address }}</div>
      </div>
      <button
        class="ml-[4vw] w-[5vw] text-white text-[0.8vw] hover:bg-neutral-500 rounded-r-lg"
        @click="copyAddress"
      >
        <IconCopyWhite class="mr-[0.3vw]" />주소 복사
      </button>

      <div class="text-white text-r-sm flex">
        행사ID
        <div class="text-white ml-[1.2vw] flex">| {{ sessionId }}</div>
      </div>
      <button
        class="ml-[4vw] w-[4.3vw] text-white text-[0.8vw] hover:bg-neutral-500 rounded-r-lg"
        @click="copySessionInfo"
      >
        <IconCopyWhite class="mr-[0.3vw]" />ID 복사
      </button>
      <div class="text-white text-r-sm flex">
        주최자
        <div class="text-white ml-[1.3vw]">| {{ host }}</div>
      </div>
      <div class="text-white text-r-sm flex">
        <div>날짜</div>
        <div class="text-white ml-[2.2vw] text-r-sm">| {{ today }}</div>
      </div>
    </div>
    <transition-group name="up">
      <div v-if="isAddressCopy" class="absolute w-[24%] h-[13%] bottom-[59%] left-[21%]">
        <div class="h-[100%] bg-neutral-500 flex flex-col justify-center items-center rounded-r-lg">
          <div class="font-bold text-white text-r-sm">복사 완료</div>
        </div>
      </div>
    </transition-group>
    <transition-group name="up">
      <div v-if="isSessionIdCopy" class="absolute w-[24%] h-[13%] bottom-[38%] left-[21%]">
        <div class="h-[100%] bg-neutral-500 flex flex-col justify-center items-center rounded-r-lg">
          <div class="font-bold text-white text-r-sm">복사 완료</div>
        </div>
      </div>
    </transition-group>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import IconCopyWhite from '@/icons/meeting/IconCopyWhite.vue'
import IconCancelWhite from '@/icons/meeting/IconCancelWhite.vue'

const host = sessionStorage.getItem('host')
const address = ref(`https://mokkoji.online/meetings/${sessionStorage.getItem('sessionId')}`)
const sessionId = ref(sessionStorage.getItem('sessionId'))
const date = new Date()
const today = date.toLocaleDateString()
const isAddressCopy = ref(false)
const isSessionIdCopy = ref(false)

// 행사 주소 복사
const copyAddress = () => {
  navigator.clipboard.writeText(address.value)

  isAddressCopy.value = true

  setTimeout(() => {
    isAddressCopy.value = false
  }, 700)
}

// 행사 세션 아이디 복사
const copySessionInfo = () => {
  navigator.clipboard.writeText(sessionId.value)

  isSessionIdCopy.value = true

  setTimeout(() => {
    isSessionIdCopy.value = false
  }, 700)
}
</script>

<style scoped>
.up-enter-active {
  transition: all 0.3s;
}

.up-leave-active {
  transition: all 0.3s;
}

.up-enter-from,
.up-leave-to {
  transform: translateY(10px);
  opacity: 0;
}
</style>
