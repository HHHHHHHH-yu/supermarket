<template>
  <div class="login-container">
    <div class="login-box">
      <div style="text-align: center; height:60px; font-size:20px; font-weight: bold; color: whitesmoke ">欢迎登录超市管理系统</div>
      <el-form :model="data.form"  ref="formRef" :rules="data.rules">
        <el-form-item prop="username">
          <el-input :prefix-icon="User" size="large" v-model="data.form.username" placeholder="请输入账号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input :prefix-icon="Lock" size="large" v-model="data.form.password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button size="large" type="primary" style="width: 100%" @click="login">登 录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import {reactive, ref} from "vue"

import request from "@/utils/request";
import {ElMessage} from "element-plus";
import router from "@/router";
import {User, Lock} from "@element-plus/icons-vue";

const data = reactive({
  form: {},
  rules:{
    username: [
      { required: true, message: '请输入用户名', trigger: 'blur' },
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' },
    ]
  }
})
const formRef = ref()

const login = () =>{
  formRef.value.validate((valid) => {
    if(valid){
      request.post('/admin/login',data.form).then(res=>{
        if(res.code ==='200'){
          ElMessage.success('登录成功')
          localStorage.setItem('user',JSON.stringify(res.data))
          router.push('/home')
        }else{
          ElMessage.error(res.msg)
        }
      })
    }
  })
}
</script>

<style scoped>

.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background-image: url('@/assets/imgs/loginBackground.jpg');
  background-size: cover;
}
.login-box {
  background-color: rgba(255,255,255,0.3); /* 透明0.1 */
  padding: 20px 50px;
  border-radius: 5px;
  box-shadow: 0 0 10px rgba(0,0,0,0.1);
}

</style>