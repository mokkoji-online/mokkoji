<template>
  <div class="fixed w-[23%] h-[20%] bottom-[15%] left-[45.5%]">
    <div
      id="gift-container"
      class="h-[100%] bg-purple-100 flex flex-col justify-center items-center rounded-r-lg"
    >
      <div class="font-bold text-r-lg underline decoration-[1vh] decoration-purple-400/50">
        마음 주시는 곳
      </div>
      <div class="mb-[1vh]"></div>
      <div class="text-r-md">{{ bank }}: {{ accountNumber }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onBeforeMount } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const bank = ref(sessionStorage.getItem('bank'))
const accountNumber = ref(sessionStorage.getItem('accountNumber'))

onBeforeMount(async () => {
  if (sessionStorage.getItem('isHost') === 'true') {
    const res = await userStore.getBankAccount()
    bank.value = res[0]
    accountNumber.value = res[1]
  }
})
</script>

<style></style>
