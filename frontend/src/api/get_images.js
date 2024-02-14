import AWS from 'aws-sdk'
import { ref } from 'vue'

AWS.config.update({
  accessKeyId: 'AKIAVHLP3OU66KFNOEEI',
  secretAccessKey: '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',
  region: 'ap-northeast-2'
})

const s3 = new AWS.S3()

const decodeS3Image = (key) => {
  let imageURL = ref('')
  const params = {
    Bucket: 'mokkoji-bucket',
    Key: key
  }
  s3.getSignedUrl('getObject', params, (err, url) => {
    if (err) {
      console.error(err)
    } else {
      console.log(url)
      imageURL.value = url
    }
  })
  return imageURL
}

const getImages = (start, end, arr) => {
  let photos = []
  for (var i = start; i < end; i++) {
    photos.push(arr[i])
  }
  return photos
}

export default getImages
