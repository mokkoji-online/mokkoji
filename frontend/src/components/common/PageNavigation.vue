<template>
  <div class="pt-10">
    <div class="flex justify-center">
      <ul class="flex">
        <li>
          <a
            @click="onPageChange(1)"
            class="border-2 cursor-pointer border-pink-100 border-solid rounded-lg p-1 opacity-70 hover:opacity-100 hover:bg-pink-100 w-12 h-2"
            >최신</a
          >
        </li>
        <li>
          <a
            @click="onPageChange(startPage == 1 ? 1 : startPage - 1)"
            class="border-2 cursor-pointer border-pink-100 border-solid rounded-lg p-1 opacity-70 hover:opacity-100 hover:bg-pink-100 w-12 h-2"
            >이전</a
          >
        </li>
        <div class="justify-center flex items-center px-2">
          <p>{{ currentPage }} / {{ totalPage }}</p>
        </div>
        <li>
          <a
            @click="onPageChange(endRange ? totalPage : endPage + 1)"
            class="border-2 cursor-pointer border-pink-100 border-solid rounded-lg p-1 opacity-70 hover:opacity-100 hover:bg-pink-100 w-12 h-2"
            >다음</a
          >
        </li>
        <li>
          <a
            @click="onPageChange(totalPage)"
            class="border-2 cursor-pointer border-pink-100 border-solid rounded-lg p-1 opacity-70 hover:opacity-100 hover:bg-pink-100 w-12 h-2"
            >마지막</a
          >
        </li>
      </ul>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'

const props = defineProps({
  currentPage: {
    type: Number
  },
  totalPage: {
    type: Number
  }
})
const emit = defineEmits(['pageChange'])

const navigationSize = parseInt(1)

const startPage = computed(() => {
  return parseInt((props.currentPage - 1) / navigationSize) * navigationSize + 1
})

const endPage = computed(() => {
  let lastPage =
    parseInt((props.currentPage - 1) / navigationSize) * navigationSize + navigationSize
  return props.totalPage < lastPage ? props.totalPage : lastPage
})

const endRange = computed(() => {
  return parseInt((props.totalPage - 1) / navigationSize) * navigationSize < props.currentPage
})

const onPageChange = (pg) => {
  console.log(pg + '로 이동!!!')
  emit('pageChange', pg)
}
</script>

<style scoped>
a {
  cursor: pointer;
  color: black;
}
a:hover {
  color: black;
}
</style>
