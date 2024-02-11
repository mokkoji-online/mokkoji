<template>
  <div class="w-max text-sm text-left pl-2 pb-2">
    <p v-if="isConfirm">사용자 이름 설정</p>
    <p v-else>회원 혜택 안내</p>
  </div>

  <div v-if="!isConfirm">
    <div class="text-black text-lg max-w-xl self-center m-3 max-md:max-w-full text-justify">
      <span class="text-4xl">잠깐! </span>
      <span>모두가 행복한 모꼬지를 찾아 주셔서 감사합니다.</span> <br />
      <span>
        <a class="text-primary3" href="/signup">회원가입 </a> 후 더욱 즐겁게 참여할 수 있는
        모꼬지만의 기능을 소개해 드릴게요!</span
      ><br /><br />
      <h1 class="text-2xl pl-5 py-2">직접 가진 못했지만 진심으로 축하해!!</h1>
      <div class="text-sm pl-5 py-2">
        <p>오프라인으로 전하지 못하는 마음을 텍스트, 음성, 영상 메세지로 전달할 수 있어요.</p>
        <p>작성하신 메세지는 롤링페이퍼로 모꼬지의 주인공에게 전해집니다!</p>
      </div>
      <br />
      <h1 class="text-2xl pl-5 py-2">
        주최하시는 행사에 초대하지 못한, 참석하지 못하는 지인이 있나요?
      </h1>
      <div class="text-sm pl-5 py-2 pb-8">
        <p>여러분만의 모꼬지를 기다리는 지인들을 온라인으로 초대해보세요!</p>
        <p>
          참가자들의 마음과 생생한 현장이 담긴 롤링페이퍼, 사진첩을 추억으로 남겨 간직할 수 있어요.
        </p>
      </div>

      <div class="float-right">
        <button @click="isConfirm = true">알겠어요</button>
      </div>
    </div>
  </div>
  <div v-else>
    <label>참가자들에게 성함을 알려주세요!</label><br />
    <input
      placeholder="미작성시 무작위 닉네임을 지정해 드립니다"
      v-model="userName"
      class="w-80 border-2 border-gray"
    /><br />
    <button @click="submitTempName(userName)" class="float-right">참가하기</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  conferenceIdInput: String
})

const router = useRouter()

const isConfirm = ref(false)
const userName = ref('')

// 이름 랜덤 생성
const capFirst = (string) => string.charAt(0).toUpperCase() + string.slice(1)
const getRandomInt = (min, max) => Math.floor(Math.random() * (max - min)) + min

const createName = () => {
  const name1 = ['abandoned', 'able', 'absolute', 'adorable', 'adventurous']
  const name2 = ['people', 'history', 'way', 'art']

  const name =
    capFirst(name1[getRandomInt(0, name1.length + 1)]) +
    ' ' +
    capFirst(name2[getRandomInt(0, name2.length + 1)])
  return name
}

const submitTempName = (tempName) => {
  if (tempName.value === null || tempName.trim('').length === 0) {
    tempName = createName()
  }

  sessionStorage.setItem('userName', tempName)

  router.push('/meetings')
}
</script>

<style></style>
