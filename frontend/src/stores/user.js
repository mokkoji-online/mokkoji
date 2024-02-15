import { defineStore } from 'pinia'
import { ref, onBeforeMount } from 'vue'
import tokenService from '@/services/token.service'
import axios from '@/services/api'

export const useUserStore = defineStore('user', () => {
  const name = ref('')
  const email = ref('')
  const image = ref('')
  const isLogin = ref(false)
  // 로그인 또는 회원가입 후 렌더링 위한 새로고침
  const forceReload = ref(false)

  // tokenservice 연계 로그인 판단 후 정보 가져옴
  const getLoginStatus = () => {
    try {
      const user = tokenService.getUser()
      user.name = name.value
      user.email = email.value
      user.image = image.value
      isLogin.value = true
    } catch (err) {
      console.error(err)
    }
  }

  // 회의 주최 시 계좌번호 받기 위한 메서드
  const getBankAccount = async () => {
    const bank = ref('')
    const accountNumber = ref('')
    await axios
      .get(import.meta.env.VITE_SERVER + '/users/userinfo')
      .then((res) => {
        bank.value = res.data.bank
        accountNumber.value = res.data.accountNumber
      })
      .catch((error) => {
        console.error(error)
      })

    return [bank.value, accountNumber.value]
  }

  onBeforeMount(() => {
    getLoginStatus
  })

  return {
    name,
    email,
    image,
    isLogin,
    forceReload,
    getLoginStatus,
    getBankAccount
  }
})
