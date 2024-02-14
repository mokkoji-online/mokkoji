<template>
  <div>
    <!-- <img :src="imageURL" alt="S3 Image" /> -->
  </div>
</template>
<script setup>
import AWS from 'aws-sdk'
import { onMounted, ref } from 'vue'

const currentPage = ref(1)
const totalPage = ref(20)

AWS.config.update({
  accessKeyId: 'AKIAVHLP3OU66KFNOEEI',
  secretAccessKey: '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',
  region: 'ap-northeast-2'
})

const s3 = new AWS.S3()

const onPageChange = (val) => {
  console.log(val + '번 페이지로 이동 준비 끝!!!')
  // currentPage.value = val
  // param.value.pgno = val
  // getHotArticleList()
}

let imageURL = ref('')

onMounted(() => {
  const params = {
    Bucket: 'mokkoji-bucket',
    Key: '배경지/rolling_baby.png'
  }
  s3.getSignedUrl('getObject', params, (err, url) => {
    if (err) {
      console.error(err)
    } else {
      console.log(url)
      imageURL.value = url
    }
  })
})
</script>
<style scoped>
a {
  cursor: pointer;
  color: black;
}
a:hover {
  color: black;
}
.my.pagination > .active > a,
.my.pagination > .active > span,
.my.pagination > .active > a:hover,
.my.pagination > .active > span:hover,
.my.pagination > .active > a:focus,
.my.pagination > .active > span:focus {
  background: grey;
  border-color: grey;
}
</style>
