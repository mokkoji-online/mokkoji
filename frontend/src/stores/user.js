import { defineStore } from 'pinia'
import { ref, onBeforeMount } from 'vue'
import tokenService from '@/services/token.service'
import axios from '@/services/api'

export const useUserStore = defineStore('user', () => {
  const name = ref('')
  const email = ref('')
  const image = ref('')
  const isLogin = ref(false)
  const forceReload = ref(false)

  const getLoginStatus = () => {
    try {
      const user = tokenService.getUser()
      user.name = name.value
      user.email = email.value
      user.image = image.value
      isLogin.value = true
    } catch (err) {
      console.log(err)
    }
  }

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
