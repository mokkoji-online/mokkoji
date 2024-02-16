<template>
  <div class="h-[22rem] w-72 mx-8 my-4 rounded-xl bg-white border-2 border-slate-300">
    <p class="pl-2 pt-2 pb-2 text-black flex">
      <strong class="effect-pink-recollection">{{ recollection.eventDay }}</strong>
    </p>
    <div class="bg-custom">
      <div class="flex justify-center items-center">
        <router-link :to="{ name: 'resultpage', params: { resultId: recollection.resultId } }">
          <img :src="`${recollection.image}`" alt="unregistered" height="180" width="180" />
        </router-link>
      </div>
      <p class="text-lg mt-2 text-center">
        <strong class="effect-blacnk-recollection">{{ recollection.name }}</strong>
      </p>
    </div>
    <div class="mt-5 text-center text-black">
      <pre
        class="text-base whitespace-pre-wrap w-[284px] h-[72px] custom-border flex justify-center items-center"
        >{{ recollection.content }}</pre
      >
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

import { useRouter } from 'vue-router'

const props = defineProps(['recollection'])

const mainImage = ref('@/assets/logo/mokkoji_logo_with_bg.png')

onMounted(() => {
  setTimeout(() => {
    mainImage.value = props.recollection.image

    console.log('Main Image: ', mainImage.value)
  }, 500)
})
</script>

<style scoped>
.effect-pink-recollection {
  box-shadow: inset 0 -5px 0 #fcd8f0;
  color: black;
}

.effect-blacnk-recollection {
  box-shadow: inset 0 -2px 0 #000000;
  color: black;
}

.custom-border {
  border-top: solid 2px;
  border-color: #ffffff;
}

.bg-custom {
  background-image: linear-gradient(rgba(255, 255, 255, 0.4), rgb(255, 255, 255)),
    url('src/assets/eventlist/recollection_background_2.png');
  background-color: white;
}
</style>
