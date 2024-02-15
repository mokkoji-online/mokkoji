<template>
  <main>
    <TheHeader v-if="!isMeeting"/>
    <RouterView
      @load-home="leaveMeeting"
      @create-meeting="createMeeting"
      @leave-meeting="leaveMeeting"
      @waiting-room="createMeeting"
    />
    <TheFooter v-if="!isMeeting" />
  </main>
</template>

<script setup>
import { ref } from 'vue'
import TheHeader from '@/components/common/TheHeader.vue'
import TheFooter from '@/components/common/TheFooter.vue'
import { RouterView } from 'vue-router'

const isMeeting = ref(false)

const createMeeting = () => {
  isMeeting.value = true
}

const leaveMeeting = () => {
  isMeeting.value = false
}
</script>

<style scoped></style>
