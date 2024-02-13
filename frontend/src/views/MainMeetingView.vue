<template>
  <MainMeeting @create-group-meeting="createGroupMeeting" />
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import MainMeeting from '@/components/meeting/MainMeeting.vue'

const emit = defineEmits(['create-meeting'])

const router = useRouter()

const createGroupMeeting = () => {
  router.push({
    path: `/groupmeetings`
  })
}

onMounted(() => {
  emit('create-meeting')
})
</script>

<style></style>
