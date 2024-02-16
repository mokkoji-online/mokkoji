import { defineStore } from 'pinia'
import axiosJwt from '@/services/api'

const { VITE_API_URL } = import.meta.env
const { VITE_SERVER } = import.meta.env

export const useUserNameStore = defineStore('username', {
  persist: {
    storage: localStorage //쓰고싶은 스토리지(세션 또는 로컬)
  },
  state: () => ({
    name: null
  }),
  actions: {
    setName(name) {
      this.name = name
    }
  },
  getters: {
    getName() {
      return this.name
    }
  }
})

export const useMemoriesStore = defineStore('memoriesData', {
  persist: {
    storage: localStorage //쓰고싶은 스토리지(세션 또는 로컬)
  },
  state: () => ({
    memories: []
  }),
  actions: {
    setMemories(memorydata) {
      this.memories = memorydata
    }
  },
  getters: {
    getMemories() {
      return this.memories
    }
  }
})

export const useRecollectionsStore = defineStore('recollectionData', {
  persist: {
    storage: localStorage //쓰고싶은 스토리지(세션 또는 로컬)
  },
  state: () => ({
    recollections: []
  }),
  actions: {
    setRecollections(recollectionData) {
      this.recollectionData = recollectionData
    }
  },
  getters: {
    getRecollections() {
      return this.recollectionData
    }
  }
})
export const useResultIDStore = defineStore('resultID', {
  persist: {
    storage: localStorage //쓰고싶은 스토리지(세션 또는 로컬)
  },
  state: () => ({
    resultID: null
  }),
  actions: {
    assignID(id) {
      this.resultID = id
    }
  },
  getters: {
    getID() {
      return this.resultID
    }
  }
})

export const useMainImageStore = defineStore('mainImg', {
  state: () => ({
    mainImage: null
  }),
  actions: {
    selectedImage(image) {
      this.mainImage = image
    },

    clearSelectedImage() {
      this.mainImage = null
    }
  },
  getters: {
    getSelectedImage() {
      return this.mainImage
    }
  }
})

export const useGalleryStore = defineStore('gallery', {
  state: () => ({
    uploadedPhotos: []
  }),
  actions: {
    setUploadedPhotos(photos) {
      this.uploadedPhotos = photos
    },
    addUploadedPhoto(photo) {
      this.uploadedPhotos.push(photo)
    },
    removeUploadedPhoto(index) {
      this.uploadedPhotos.splice(index, 1)
    }
  }
})

export const useFormDataStore = defineStore('formData', {
  state: () => ({
    formData: new FormData()
  }),
  actions: {
    addFile(file) {
      this.formData.append('file', file)
      console.log(this.formData)
    },
    clearFormData() {
      this.formData = new FormData()
    }
    // You can add more actions here as needed
  }
})

export const useImgUploadStore = defineStore('imgUpload', () => {
  const addPhotos = async (id, formData, success, fail) => {
    // console.log(id)
    // console.log(formData)
    await axiosJwt
      .post(VITE_API_URL + VITE_SERVER + `/results/${id}/memories/photos`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      })
      .then(success)
      .catch(fail)
  }

  return {
    addPhotos
  }
})

export const useEventList = defineStore('userEvents', () => {
  const eventList = async (success, fail) => {
    await axiosJwt
      .get(VITE_API_URL + VITE_SERVER + '/results/lists')
      .then(success)
      .catch(fail)
  }
  return {
    eventList
  }
})

export const useEditMemory = defineStore('editMemoryData', () => {
  const editMemory = async (id, success, fail) => {
    await axiosJwt
      .get(VITE_API_URL + VITE_SERVER + `/results/${id}/memories`)
      .then(success)
      .catch(fail)
  }
  return { editMemory }
})

export const useSaveTemplate = defineStore('saveTemplateData', () => {
  const saveTemplate = async (id, template, success, fail) => {
    await axiosJwt
      .patch(
        VITE_API_URL + VITE_SERVER + `/results/${id}/memories/rollingpaper`,
        JSON.stringify(template),
        {
          headers: {
            'Content-Type': 'application/json;charset=utf-8'
          }
        }
      )
      .then(success)
      .catch(fail)
  }
  return { saveTemplate }
})

export const useSaveThumbnail = defineStore('saveThumbnailData', () => {
  const saveThumbnail = async (id, url, success, fail) => {
    await axiosJwt
      .patch(VITE_API_URL + VITE_SERVER + `/results/${id}/memories`, url, {
        headers: {
          'Content-Type': 'application/json;charset=utf-8'
        }
      })
      .then(success)
      .catch(fail)
  }
  return { saveThumbnail }
})

export const useToRecollection = defineStore('ToRecollection', () => {
  const toRecollection = async (id, cardData, success, fail) => {
    await axiosJwt
      .post(VITE_API_URL + VITE_SERVER + `/results/${id}`, cardData, {
        headers: {
          'Content-Type': 'application/json;charset=utf-8'
        }
      })
      .then(success)
      .catch(fail)
  }
  return { toRecollection }
})

export const useRecollection = defineStore('RecollectionData', () => {
  const RecollectionData = async (id, page, success, fail) => {
    await axiosJwt
      .get(VITE_API_URL + VITE_SERVER + `/results/${id}/recollections?page=${page}`)
      .then(success)
      .catch(fail)
  }
  return { RecollectionData }
})

export const useDownloadThumbnail = defineStore('DownloadThumbnail', () => {
  const DownloadThumbnail = async (id, success, fail) => {
    await axiosJwt
      .get(VITE_API_URL + VITE_SERVER + `/results/${id}/thumbnail`)
      .then(success)
      .catch(fail)
  }
  return { DownloadThumbnail }
})

export const useDownloadPhotomosaic = defineStore('DownloadPhotomosaic', () => {
  const DownloadPhotomosaic = async (id, success, fail) => {
    await axiosJwt
      .get(VITE_API_URL + VITE_SERVER + `/results/photomosaic/${id}`)
      .then(success)
      .catch(fail)
  }
  return { DownloadPhotomosaic }
})

export const useShareImage = defineStore('ShareImage', () => {
  const ShareImage = async (id, success, fail) => {
    await axiosJwt
      .get(VITE_API_URL + VITE_SERVER + `/results/${id}/sharing/thumbnail`)
      .then(success)
      .catch(fail)
  }
  return { ShareImage }
})

export const useSharePhotomosaic = defineStore('SharePhotomosaic', () => {
  const SharePhotomosaic = async (id, success, fail) => {
    await axiosJwt
      .get(VITE_API_URL + VITE_SERVER + `/results/${id}/sharing/photomosaic`)
      .then(success)
      .catch(fail)
  }
  return { SharePhotomosaic }
})

export const useCreatePhotomosaic = defineStore('createPhotomosaic', () => {
  const CreatePhotomosaic = async (id, success, fail) => {
    await axiosJwt
      .put('http://localhost:8080/' + VITE_SERVER + `/results/${id}/photomosaic`)
      .then(success)
      .catch(fail)
  }
  return { CreatePhotomosaic }
})
