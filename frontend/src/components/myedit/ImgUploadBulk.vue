<template>
  <DropZone class="h-[600px]" @files-dropped="addFiles" #default="{ dropZoneActive }">
    <label for="file-input" class="text-black">
      <span v-if="dropZoneActive">
        <span>
          <div>
            <p class="text-xl pl-80 flex text-black">
              이미지는 한 번에 &nbsp;<span class="highlight flex">24장까지 </span> &nbsp;업로드할 수
              있어요
              <IconExclamationMark />
            </p>
          </div>
        </span>
      </span>
      <span v-else class="">
        <span class="">
          <p class="text-lg flex text-black pl-40">
            업로드할 이미지들을 &nbsp; <strong>드래그 앤 드롭</strong>하세요. 이미지의 크기는 &nbsp;
            <span class="highlight flex"> 정사각형 비율 <IconCropTwo /> </span> &nbsp;로 자동
            조정됩니다.
          </p>
        </span>
        <span class="text-lg ml-[320px] text-black flex">
          또는 <strong><em>이 곳을 클릭</em></strong
          >하면 파일을 업로드 할 수 있어요. <IconSmile />
        </span>
      </span>

      <input type="file" id="file-input" multiple @change="onInputChange" class="opacity-0" />
    </label>
    <ul class="flex flex-wrap" v-show="files.length">
      <FilePreview
        v-for="file of files.slice(0, 24)"
        :key="file.id"
        :file="file"
        tag="li"
        @remove="removeFile"
      />
    </ul>
  </DropZone>
  <div>
    <button
      class="absolute left-[430px] bottom-[-10px] opacity-70 text-[16px] border-[#5da2bd] flex pt-1 rounded-lg hover:opacity-100 border-2 border-solid mb-5 px-10 py-1"
      @click="uploadImage"
    >
      이미지 업로드
    </button>
  </div>
</template>

<script setup>
import useFiles from '@/api/file-list'
import DropZone from '@/components/myedit/DropZone.vue'
import FilePreview from '@/components/myedit/FilePreview.vue'
import IconExclamationMark from '@/icons/result/IconExclamationMark.vue'
import IconCropTwo from '@/icons/result/IconCropTwo.vue'
import IconSmile from '@/icons/result/IconSmile.vue'
import { defineEmits } from 'vue'
import {
  useFormDataStore,
  useImgUploadStore,
  useResultIDStore,
  useGalleryStore
} from '@/stores/result.js'

const emit = defineEmits(['closeModal'])

const { files, addFiles, removeFile } = useFiles()
const resultIDStore = useResultIDStore()
const formDataStore = useFormDataStore()
const imgUploadStore = useImgUploadStore()
const galleryStore = useGalleryStore()

const onInputChange = (e) => {
  addFiles(e.target.files)
  e.target.value = null
}

const closeModal = () => {
  emit('closeModal')
}

const uploadImage = () => {
  photoList()
  closeModal()
}

//사진 추가 Bulk Axios
const photoList = () => {
  console.log('사진 추가 Bulk 데이터 전송 메소드 실행')
  console.log(files.value)
  const formData = new FormData()
  const promises = []
  for (let i = 0; i < files.value.length; i++) {
    const file = files.value[i].file
    const reader = new FileReader()

    promises.push(
      new Promise((resolve) => {
        reader.onload = (event) => {
          const image = new Image()
          image.onload = () => {
            const canvas = document.createElement('canvas')
            const ctx = canvas.getContext('2d')
            const maxSize = Math.min(image.width, image.height)
            const offsetX = (image.width - maxSize) / 2
            const offsetY = (image.height - maxSize) / 2
            canvas.width = maxSize
            canvas.height = maxSize
            ctx.drawImage(image, offsetX, offsetY, maxSize, maxSize, 0, 0, maxSize, maxSize)
            //canvas로 자르고 이미지 파일로 변환
            canvas.toBlob((blob) => {
              const croppedFile = new File([blob], file.name, { type: 'image/jpeg' })
              formDataStore.addFile(croppedFile)
              formData.append('photos', croppedFile)
              galleryStore.addUploadedPhoto(croppedFile)
              resolve()
            }, 'image/jpeg')
          }
          image.src = event.target.result
        }
        reader.readAsDataURL(file)
      })
    )
  }
  //비동기 처리, 이미지 업로드 성공 여부
  Promise.all(promises).then(() => {
    imgUploadStore
      .addPhotos(resultIDStore.getID, formData, ({ res }) => {
        console.log('이미지 업로드 성공')
      })
      .catch((error) => {
        console.log('이미지 업로드 오류:', error)
      })
      .finally(() => {
        formDataStore.clearFormData()
      })
  })

  imgUploadStore.addPhotos(resultIDStore.getID, formData, ({ res }) => {
    for (let i = 0; i < files.value.length; i++) {
      galleryStore.addUploadedPhoto(files.value[i].file)
    }
    console.log('이미지 업로드 성공')
  }),
    (error) => {
      console.log('이미지 업로드 오류:', error)
    }
  formDataStore.clearFormData()
}
</script>

<style scoped>
.highlight {
  box-shadow: inset 0 -3px 0 #5faeb5;
}
</style>
