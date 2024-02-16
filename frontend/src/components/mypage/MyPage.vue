<template>
  <div
    class="flex mt-0 w-full flex-col justify-center items-center px-16 py-12 max-md:max-w-full max-md:px-5"
    id="main-gradient2"
  >
    <div id="card-div">
      <div class="absolute -mt-32 mx-4 justify-center">
        <img alt="프로필 사진" id="image-profile" :src="userData.image" />
      </div>
      <div class="gap-4 flex max-md:flex-col max-md:gap-2 mt-20">
        <div class="flex flex-col items-center w-6/12">
          <span class="flex flex-col mt-10 max-md:mt-10"
            ><div class="text-3xl font-bold">{{ userData.name }}님</div>
          </span>
          <div class="text-black text-xl self-stretch mt-2.5">{{ userData.email }}</div>
          <span class="mt-16 pl-2 pr-4"
            ><div
              class="text-black text-2xl"
              v-text="userData.accountRegistered ? '계좌가 등록되었어요.' : '등록된 계좌가 없어요.'"
            ></div>
          </span>
          <div class="text-sm font-light max-w-[24lvh] mt-5 whitespace-wrap text-center">
            계좌를 등록하시면 참가자들의 마음을 받을 수 있어요
          </div>
          <button
            @click="$router.push('mypage/detail')"
            class="m-10 self-center text-primary3 hover:bg-natural-yellow hover:text-red-500"
          >
            회원정보 수정
          </button>
        </div>
        <div class="flex flex-col justify-around items-stretch">
          <div>
            <span class="flex items-stretch justify-between">
              <div class="text-black text-2xl font-extrabold">활동 기록</div>
              <!-- TODO : 결과물 보기 링크 -->
              <a href="/eventlist" class="text-1xl">상세보기 ></a>
            </span>
          </div>
          <div class="flex items-stretch justify-between gap-5 mt-6 pr-1.5">
            <div id="div-stat">
              모꼬지 주최<br /><span class="text-purple-400">{{ userData.eventCount }}</span
              >번<br />
            </div>
            <div id="div-stat">
              참여자 수<br /><span class="text-purple-400">{{ userData.totalParticipant }}</span
              >명
            </div>
          </div>
          <div class="flex items-stretch justify-between gap-5 mt-6 pr-1.5">
            <div id="div-stat">
              모꼬지 <br /><span class="text-purple-400">{{ userData.totalTime }}</span
              >분
            </div>
            <div id="div-stat">
              받은 메세지<br /><span class="text-purple-400">{{ userData.totalMessage }}</span
              >개
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onBeforeMount } from 'vue'
import axios from '@/services/api'

const userData = ref({})

// axios로 회원 정보 가져오기
onBeforeMount(() => {
  axios
    .get(import.meta.env.VITE_SERVER + '/users/mypage')
    .then((res) => {
      userData.value = res.data
    })
    .catch((err) => {
      console.error(err)
    })
})
</script>

<style scoped></style>
