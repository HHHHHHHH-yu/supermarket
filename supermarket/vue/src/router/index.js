import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Manager',
      component: () => import('@/views/Manager.vue'),
      redirect: '/home',
      children: [
        { path: 'home', name: 'Home', component: () => import('@/views/manager/Home.vue')},
        { path: 'admin', name: 'Admin', component: () => import('@/views/manager/Admin.vue')},
        { path: 'category', name: 'Category', component: () => import('@/views/manager/Category.vue')},
        { path: 'goods', name: 'Goods', component: () => import('@/views/manager/Goods.vue')},
        { path: 'supplier', name: 'Supplier', component: () => import('@/views/manager/Supplier.vue')},
        { path: 'orders', name: 'Orders', component: () => import('@/views/manager/Orders.vue')},
        { path: 'person', name: 'Person', component: () => import('@/views/manager/Person.vue')},
        { path: 'password', name: 'Password', component: () => import('@/views/manager/Password.vue')}
      ]
    },
    {path: '/login',
      component: () => import('@/views/Login.vue')}
  ]
})

export default router
