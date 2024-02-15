<template>
  <div class="fixed w-[30%] h-[50%] left-[35%]">
    <div
      class="px-[5vh] h-[100%] bg-purple-100 flex flex-col justify-center items-center rounded-r-lg"
    >
      <div class="mb-[3vh] font-bold text-r-lg underline decoration-[1vh] decoration-purple-400/50">
        로그인이 필요한 서비스입니다!
      </div>
      <div class="mb-[3vh] text-r-md text-center">
        로그인을 하시면 <strong>롤링페이퍼 작성</strong>과 <strong>사진 촬영</strong> 등 더욱 다양한
        서비스를 즐기실 수 있습니다.
      </div>
      <div class="w-full text-r-md flex justify-center items-center gap-[3vh]">
        <button
          class="w-[30%] aspect-[3] outline outline-[0.5vh] outline-purple-400 bg-white rounded-r-lg hover:bg-purple-300"
          @click="showLoginModal"
        >
          로그인 하기
        </button>
        <button
          class="w-[30%] aspect-[3] outline outline-[0.5vh] outline-red-400 bg-white rounded-r-lg hover:bg-red-300"
          @click="$emit('remove-login-check-modal')"
        >
          괜찮아요
        </button>
      </div>
    </div>
    <ModalView v-if="isLoginModal" :show-modal="isLoginModal" @close-modal="showLoginModal">
      <LoginModal />
    </ModalView>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ModalView from '@/views/ModalView.vue'
import LoginModal from '@/components/modal/home/LoginModal.vue'

defineEmits(['remove-login-check-modal'])

const isLoginModal = ref(false)

const showLoginModal = () => {
  isLoginModal.value = !isLoginModal.value
}
</script>

<style scoped></style>
