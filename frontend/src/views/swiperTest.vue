<template>
  <div class="h-[100vh] w-[100vw] relative">
    <img
      :src="`src/assets/rollingtemplate/baby.png`"
      :alt="`template_${design}`"
      width="450px"
      height="700px"
      class="absolute z-10 inset-0"
    />
    <img
      :src="`src/assets/rollingnote/rainbow.png`"
      :alt="`template_${color}`"
      class="absolute z-20 inset-0"
      width="420px"
      height="650px"
    />

    <swiper :pagination="pagination" :modules="modules" class="mt-3">
      <swiper-slide v-for="n in 2" :key="n" class="pt-3 relative">
        <div class="fixed w-[100px] h-[100px] top-16 left-16 bottom-16 right-16">
          <p class="text-center text-[8px] mb-1">From {{ msg[n - 1][0].name }}</p>
          <p class="text-[9px]">{{ msg[n - 1][0].content }}</p>
        </div>
        <!-- <div class="absolute w-[100px] h-[100px] bottom-[100px] left-[-45px]">
          <p class="text-center text-[8px] mb-1">From {{ msg[n - 1][1].name }}</p>
          <p class="text-[9px]">{{ msg[n - 1][1].content }}</p>
        </div>
        <div class="absolute w-[100px] h-[100px] bottom-[100px] left-[110px]">
          <p class="text-center text-[8px] mb-1">From {{ msg[n - 1][2].name }}</p>
          <p class="text-[9px]">{{ msg[n - 1][2].content }}</p>
        </div>
        <div class="absolute w-[100px] h-[100px] top-[120px] left-[110px]">
          <p class="text-center text-[8px] mb-1">From {{ msg[n - 1][3].name }}</p>
          <p class="text-[9px]">{{ msg[n - 1][3].content }}</p>
        </div>
        <div class="absolute w-[100px] h-[100px] bottom-[-60px] left-[110px]">
          <p class="text-center text-[8px] mb-1">From {{ msg[n - 1][4].name }}</p>
          <p class="text-[9px]">
            {{ msg[n - 1][4].content }}
          </p>
        </div>
        <div class="absolute w-[100px] h-[100px] bottom-[-60px] left-[-45px]">
          <p class="text-center text-[8px] mb-1">From {{ msg[n - 1][5].name }}</p>
          <p class="text-[9px]">{{ msg[n - 1][5].content }}</p>
        </div>
        <div class="absolute w-[100px] h-[100px] bottom-[-60px] right-[100px]">
          <p class="text-center text-[8px] mb-1">From {{ msg[n - 1][6].name }}</p>
          <p class="text-[9px]">{{ msg[n - 1][6].content }}</p>
        </div>
        <div class="absolute w-[100px] h-[100px] top-[120px] right-[100px]">
          <p class="text-center text-[8px] mb-1">From {{ msg[n - 1][7].name }}</p>
          <p class="text-[9px]">{{ msg[n - 1][7].content }}</p>
        </div>
        <div class="absolute w-[100px] h-[100px] top-[120px] left-[-45px]">
          <p class="text-center text-[8px] mb-1">From {{ msg[n - 1][8].name }}</p>
          <p class="text-[9px]">{{ msg[n - 1][8].content }}</p>
        </div> -->
      </swiper-slide>
    </swiper>
  </div>
</template>
<script setup>
import { Swiper, SwiperSlide } from 'swiper/vue'
import msg from '@/temp/result/messages.json'

// Import Swiper styles
import 'swiper/css'
import 'swiper/css/pagination'
import '../pagination.css'

// Import required modules
import { Pagination } from 'swiper/modules'

const pagination = {
  clickable: true,
  renderBullet: function (index, className) {
    return '<span class="' + className + '">' + (index + 1) + '</span>'
  }
}

const modules = [Pagination]
</script>
