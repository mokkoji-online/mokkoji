import AWS from 'aws-sdk'

AWS.config.update({
  accessKeyId: 'AKIAVHLP3OU66KFNOEEI',
  secretAccessKey: '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',
  region: 'ap-northeast-2'
})

const s3 = new AWS.S3()

export const decodeS3Image = (key) => {
  const params = {
    Bucket: 'mokkoji-bucket',
    Key: key
  }

  return new Promise((resolve, reject) => {
    s3.getSignedUrl('getObject', params, (err, url) => {
      if (err) {
        console.error(err)
        reject(err)
      } else {
        console.log(url)
        resolve(url)
      }
    })
  })
}

export const getImages = (start, end, arr) => {
  let photos = []
  for (var i = start; i < end; i++) {
    photos.push(arr[i])
  }
  return photos
}
