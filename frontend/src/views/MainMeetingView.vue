<template>
  <MainMeeting @create-group-meeting="createGroupMeeting(event)" />
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import MainMeeting from '@/components/meeting/MainMeeting.vue'

const emit = defineEmits(['create-meeting'])

const router = useRouter()

const createGroupMeeting = (event) => {
  console.log(event)
  router.push({
    path: `/groupmeetings`
  })
}

onMounted(() => {
  emit('create-meeting')
})
</script>

<style scoped></style>
