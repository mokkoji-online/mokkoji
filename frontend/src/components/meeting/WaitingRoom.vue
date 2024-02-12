<template>
  <img src="@/assets/logo/mokkoji_logo.png" alt="" class="fixed h-[80vh] top-[10%] left-[10%]" />
  <div class="fixed top-[40%] text-r-md font-bold">회의에서 사용할 닉네임을 입력해주세요!</div>
  <button @click="goToMeeting">123123</button>
</template>

<script setup>
import { onMounted, onBeforeMount } from 'vue'
import { useRouter } from 'vue-router'

const emit = defineEmits(['waiting-room']['leave-meeting'])

const router = useRouter()

const goToMeeting = () => {
  router.push('/meetings')
}

onMounted(() => {
  emit('waiting-room')
})

onBeforeMount(() => {
  emit('leave-meeting')
})
</script>

<style></style>
