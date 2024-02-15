<template>
  <component :is="tag" class="">
    <button @click="$emit('remove', file)" class="text-sm" aria-label="Remove">×</button>
    <img :src="file.url" :alt="file.file.name" :title="file.file.name" class="w-16 h-16" />
  </component>
</template>

<script setup>
//파일 미리보기
defineEmits(['remove'])
defineProps({
  file: { type: Object, required: true },
  tag: { type: String, default: 'li' }
})
</script>

<style scoped></style>
