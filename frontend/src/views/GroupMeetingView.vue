<template>
  <GroupMeeting :session="sessions" />
</template>

<script setup>
import { ref, onMounted } from 'vue'
import GroupMeeting from '@/components/meeting/GroupMeeting.vue'

const props = defineProps({
  mainSessionId: {
    type: String
  },
  groupNumber: {
    type: String
  }
})

const emit = defineEmits(['create-meeting'])

const sessions = ref({ sessionId: props.mainSessionId, groupNumber: props.groupNumber })

onMounted(() => {
  emit('create-meeting')
})
</script>

<style scoped></style>
