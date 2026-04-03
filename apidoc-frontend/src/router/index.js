import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/projects'
  },
  {
    path: '/projects',
    name: 'ProjectList',
    component: () => import('../views/ProjectList.vue')
  },
  {
    path: '/projects/:id/apis',
    name: 'ApiList',
    component: () => import('../views/ApiList.vue')
  },
  {
    path: '/apis/:id',
    name: 'ApiDetail',
    component: () => import('../views/ApiDetail.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
