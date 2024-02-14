import axios from 'axios'
import tokenService from '@/services/token.service.js'

const instance = axios.create({
  baseURL: import.meta.env.VITE_API_URL
})

instance.interceptors.request.use(
  (config) => {
    const token = tokenService.getLocalAccessToken()
    if (token) {
      if (tokenService.expiredToken(token)) {
        const refreshToken = tokenService.getLocalRefreshToken()
        config.headers['Authorization-Refresh'] = refreshToken
      } else config.headers['Authorization'] = token
    }
    return config
  },
  (err) => {
    switch (err.status) {
      case 401:
        alert(err.errorMsg)
        break
      case 403:
        alert('User 권한이 없습니다')
        break
      case 409:
        alert('이미 가입한 회원입니다')
    }
    return Promise.reject(err)
  }
)

instance.interceptors.response.use(
  (res) => {
    const accessToken = res.headers.get('authorization')
    const refreshToken = res.headers.get('authorizatoin-Refresh')
    if (accessToken != null) tokenService.setLocalAccessToken(accessToken)
    if (refreshToken != null) tokenService.setLocalRefreshToken(refreshToken)

    return res
  },
  (err) => {
    return Promise.reject(err)
  }
)

export default instance
