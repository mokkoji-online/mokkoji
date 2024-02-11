import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import MyPageView from '@/views/MyPageView.vue'
import MyPage from '@/components/mypage/MyPage.vue'
import EventListPage from '@/views/EventListView.vue'
import MyDetail from '@/components/mypage/MyDetail.vue'
import MainMeetingView from '@/views/MainMeetingView.vue'
import GroupMeetingView from '@/views/GroupMeetingView.vue'
import Error404 from '@/components/common/Error404.vue'
import ErrorSession from '@/components/common/ErrorSession.vue'
import EditPage from '@/views/EditView.vue'
import HandleCallback from '@/components/common/HandleCallback.vue'
import RollingPaper from '@/views/RollingPaper.vue'
import PhotoMosaic from '@/views/PhotoMosaic.vue'
import AudioRecorderModal from '@/components/modal/meeting/AudioRecorderModal.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/oauth2/:pathMatch(.*)*',
      component: HandleCallback
    },
    {
      path: '/signup',
      name: 'signup',
      component: () => import('@/components/common/SignUp.vue')
    },
    {
      path: '/mypage',
      name: 'mypage',
      component: MyPageView,
      children: [
        {
          path: '',
          name: 'mypage',
          component: MyPage
        },
        {
          path: 'detail',
          name: 'mydetail',
          component: MyDetail
        }
      ]
    },
    {
      path: '/meetings',
      name: 'mainmeeting',
      component: MainMeetingView,
      props: true
    },
    {
      path: '/meetings/:groupNumber',
      name: 'groupmeeting',
      component: GroupMeetingView,
      props: true
    },

    {
      path: '/eventlist',
      name: 'eventlist',
      component: EventListPage
    },
    {
      path: '/editpage',
      name: 'editpage',
      component: EditPage
    },
    {
      path: '/:pathMatch(.*)*',
      redirect: '404'
    },
    {
      path: '/404',
      name: 'Error404',
      component: Error404
    },
    {
      path: '/meetings'
    },
    {
      path: '/rollingpaper',
      name: 'rollingpaper',
      component: RollingPaper
    },
    {
      path: '/photomosaic',
      name: 'photomosaic',
      component: PhotoMosaic
    },
    {
      path: '/audiorecordingmodal',
      name: 'audiorecordingmodal',
      component: AudioRecorderModal
    }
  ]
})

export default router
