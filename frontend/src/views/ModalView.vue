<template>
  <div
    v-if="showModal"
    class="fixed w-full h-screen my-auto bg-black bg-opacity-30 top-0 left-0 z-10 flex justify-center px-8"
  >
    <div
      v-show="showModal"
      class="p-4 bg-white self-start m-auto rounded-r-xl"
      v-on-click-outside="clickOutsideEmit"
    >
      <svg
        xmlns="http://www.w3.org/2000/svg"
        viewBox="0 0 24 24"
        width="30"
        height="30"
        fill="gray"
        class="m-1 float-right hover:bg-neutral-200 hover:cursor-pointer rounded-full"
        @click="$emit('close-modal')"
      >
        <path
          d="M19 6.41L17.59 5 12 10.59 6.41 5 5 6.41 10.59 12 5 17.59 6.41 19 12 13.41 17.59 19 19 17.59 13.41 12z"
        />
      </svg>
      <br />
      <slot />
    </div>
  </div>
</template>

<script setup>
import { vOnClickOutside } from '@vueuse/components'

const props = defineProps({
  showModal: {
    type: Boolean,
    default: false
  }
})

const emit = defineEmits(['close-modal'])

const clickOutsideEmit = () => {
  emit('close-modal')
}
</script>

<style scoped></style>
