<template>
  <div>
    <div style="width: 50%;margin: 50px auto">
      <el-form :model="data.user" ref="formRef" :rules="rules" label-width="100px" label-position="right" style="padding-right: 40px">

        <el-form-item label="新密码" prop="newPassword">
          <el-input show-password v-model="data.user.newPassword" placeholder="建议8-16位（包含2种以上不同类型字符）" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input show-password v-model="data.user.confirmPassword" placeholder="请再次输入密码" autocomplete="off"/>
        </el-form-item>

        <div style="text-align: center; margin-top: 40px">
          <el-button type="primary" style="padding: 20px 40px" @click="save" >修改</el-button>
        </div>
      </el-form>

    </div>
  </div>
</template>

<script setup>
import {reactive,ref} from "vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from '@element-plus/icons-vue'
import router from "@/router";


const data = reactive({
  user : JSON.parse(localStorage.getItem('user') || '{}')
})

const rules = reactive ({
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
  ],
  confirmPassword: [
    { required: true, message: '请确认新密码', trigger: 'blur' },
  ]
})

const formRef = ref()

const save = () => {
  formRef.value.validate((valid) => {
    if(valid){
      request.post('/admin/updatePassword',data.user).then(res =>{
        if(res.code === '200'){
          ElMessage.success('修改成功')
          router.push('/login')
        }else{
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

</script>