const parseJwt = (token) => {
  const base64Url = token.split('.')[1]
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/')
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split('')
      .map(function (c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2)
      })
      .join('')
  )
  return JSON.parse(jsonPayload)
}

class TokenService {

  getLocalRefreshToken = () => {
    return $cookies.get('user') ? $cookies.get('refresh-token') : null
  }

  getLocalAccessToken = () => {
    return $cookies.get('user') ? $cookies.get('token') : null
  }

  setLocalRefreshToken = (token) => {
    $cookies.set('refresh-token', token);
  }

  setLocalAccessToken = (token) => {
    $cookies.set('token', token);
  }

  updateLocalAccessToken = (token) => {
    $cookies.set('token', token);
  }

  getUser = () => {
    return $cookies.get('user')
  }

  setUser = (user) => {
    $cookies.set('user', user)
  }

  removeUser = () => {
    $cookies.remove('user');
    $cookies.remove('token');
    try{
      $cookies.remove('refresh-token');
    }
    catch(err) {
      console.log(err);
    }
  }

  expiredToken = () => {
    return parseJwt($cookies.get('token')).exp * 1000 <= Date.now()
  }
}

export default new TokenService()