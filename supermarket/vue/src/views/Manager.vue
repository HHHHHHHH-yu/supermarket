<template>
  <div>
    <div style="height: 60px; background-color: #ecf4df; display: flex; align-items: center; border-bottom: 1px solid #ddd">
      <div style="flex: 1">
        <div style="padding-left: 20px; display: flex; align-items: center">
          <img src="@/assets/imgs/logo.png" alt="" style="width: 30px; border-radius: 50%">
          <div style="font-weight: bold; font-size: 20px; margin-left: 10px; color: #0769fc">超市管理系统</div>
        </div>
      </div>
      <div style="width: fit-content; padding-right: 10px; display: flex; align-items: center;">
       <!-- <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="" style="width: 40px; height: 40px"> -->
     <el-avatar :size="36" :src="data.user.avatar" v-if="data.user.avatar"
                   style="margin-right: 10px;" />
        <el-avatar :size="36" :icon="UserFilled" v-else style="margin-right: 10px;" />
        <span style="margin-left: 5px">{{data.user.name}}</span>
      </div>
    </div>

    <div style="display: flex">
      <div style="width: 200px; border-right: 1px solid #ddd; min-height: calc(100vh - 60px)">
        <el-menu
            router
            style="border: none"
            :default-active="$route.path"
            :default-openeds="['/home', 'info','2']"
        >
          <el-menu-item index="/home">
            <el-icon><HomeFilled /></el-icon>
            <span>系统首页</span>
          </el-menu-item>
          <el-sub-menu index="info">
            <template #title>
              <el-icon><DataAnalysis /></el-icon>
              <span>信息管理</span>
            </template>
            <el-menu-item index="/category">
              <el-icon><DocumentRemove /></el-icon>
              <span>分类管理</span>
            </el-menu-item>
            <el-menu-item index="/goods">
              <el-icon><Goods /></el-icon>
              <span>商品信息管理</span>
            </el-menu-item>
            <el-menu-item index="/supplier">
              <el-icon><User /></el-icon>
              <span>供货商管理</span>
            </el-menu-item>
            <el-menu-item index="/orders">
              <el-icon><ShoppingCart /></el-icon>
              <span>订单管理</span>
            </el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="user">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/admin">
              <el-icon><UserFilled /></el-icon>
              <span>管理员信息</span>
            </el-menu-item>
          </el-sub-menu>
          <el-menu-item index="/person">
            <el-icon><User /></el-icon>
            <span>个人资料</span>
          </el-menu-item>
          <el-menu-item index="/password">
            <el-icon><Lock /></el-icon>
            <span>修改密码</span>
          </el-menu-item>
          <el-menu-item index="login" @click="logout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出系统</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div style="flex: 1; width: 0; background-color: #f8f8ff; padding: 10px">
        <router-view />
      </div>
    </div>

  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import {User, UserFilled} from "@element-plus/icons-vue";
import {reactive} from "vue";
const $route = useRoute()
console.log($route.path)

const data = reactive({
  user: JSON.parse(localStorage.getItem('user') || '{}')
})
const logout = () => {
  localStorage.removeItem('supermarket-user')
}
</script>

<style scoped>
.el-menu-item.is-active {
  background-color: #ecf4df !important;
}
.el-menu-item:hover {
  background-color: #ecf4df !important;
  color: #966ce5;
}
:deep(th)  {
  color: #333;
}
</style>