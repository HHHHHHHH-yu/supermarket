<template>
  <div>
    <div style="margin-bottom: 10px;">
      <el-input prefix-icon="Search" style="width: 300px; margin-right: 10px" v-model="data.name" placeholder="请输入名称查询"></el-input>
      <el-button type="primary" plain round @click="load">查询</el-button>
      <el-button type="info" plain round style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div style="width: 50%;margin: 50px auto">
      <el-form :model="data.user" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="头像" prop="img">
           <div style="display: flex; align-items: center; gap: 20px;">
            <!-- 显示当前头像 -->
            <div v-if="data.user.avatar">
              <el-avatar :size="100" :src="data.user.avatar" />
            </div>
            <div v-else>
              <el-avatar :size="100" :icon="UserFilled" />
            </div>

          <el-upload
              class="upload-demo"
              :action="'http://localhost:9090/files/upload'"
              list-type="picture"
              :on-success="handleFileUpload"
          >
            <el-button type="primary">点击上传</el-button>
          </el-upload>
           </div>
        </el-form-item>

        <el-form-item label="用户名" prop="username">
          <el-input disabled="" v-model="data.user.username" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input v-model="data.user.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="data.user.phone" autocomplete="off"/>
        </el-form-item>
    <!--    <el-form-item label="头像">
          <el-upload action="http://localhost:9090/files/upload" list-type="picture" :on-success="handleImgUploadSuccess">
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>-->
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="data.user.email" autocomplete="off"/>
        </el-form-item>
        <div style="text-align: center; margin-top: 40px">
          <el-button type="primary" style="padding: 20px 40px" @click="save" >保存</el-button>
        </div>
      </el-form>

    </div>
  </div>
</template>

<script setup>
import {reactive,ref} from "vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import { UserFilled } from '@element-plus/icons-vue'
import {Delete, Edit} from '@element-plus/icons-vue'
import router from "@/router";


const data = reactive({
  user : JSON.parse(localStorage.getItem('user') || '{}')
})

const save = () => {
  request.put('/admin/update',data.user).then(res =>{
    if(res.code === '200'){
      ElMessage.success('保存成功')
      updateLocalStorage()
    //router.push('/login')
    }else{
      ElMessage.error(res.msg)
    }
  })
}

const handleFileUpload = (res) => {
  if (res.code === '200') {
    data.user.avatar = res.data
    updateLocalStorage()
    ElMessage.success('上传成功')
  } else {
    ElMessage.error(res.msg || '上传失败')
  }
}

const updateLocalStorage = () => {
  localStorage.setItem('user', JSON.stringify(data.user))
}

</script>