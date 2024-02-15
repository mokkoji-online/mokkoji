// import { localAxios } from '@/util/http-commons'

// const local = localAxios()

import axiosJwt from '@/services/api'
import axios from 'axios'

const { VITE_API_URL_LOCAL } = import.meta.env
const { VITE_API_URL } = import.meta.env
const { VITE_SERVER } = import.meta.env

// const url = '/api/v1/results'

// async function eventList(success, fail) {
//   await local.get(`${url}/lists`).then(success).catch(fail)
// }

// async function memoryEditData(id, success, fail) {
//   await local.get(`${url}/${id}/memories`).then(success).catch(fail)
// }

// function addPhotos(id, formData, success, fail) {
//   console.log(id)
//   console.log(formData)
//   local
//     .post(`${url}/${id}/memories/photos`, formData, {
//       headers: {
//         'Content-Type': 'multipart/form-data'
//       }
//     })
//     .then(success)
//     .catch(fail)
// }
// export { eventList, memoryEditData, addPhotos }
