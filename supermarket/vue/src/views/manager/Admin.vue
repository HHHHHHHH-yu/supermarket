<template>
  <div>

    <div class="card" style="margin-bottom: 10px;">
      <el-input prefix-icon="Search" style="width: 300px; margin-right: 10px" v-model="data.username" placeholder="请输入账号查询"></el-input>
      <el-input prefix-icon="Search" style="width: 300px; margin-right: 10px" v-model="data.name" placeholder="请输入姓名查询"></el-input>
      <el-button type="primary" plain round @click="load">查询</el-button>
      <el-button type="info" plain round style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 10px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" plain round @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="data.tableData" style="width: 100%">
        <el-table-column prop="id" label="序号" width="70"/>
        <el-table-column prop="username" label="账号"/>
        <el-table-column prop="name" label="姓名"/>
        <el-table-column prop="phone" label="电话"/>
        <el-table-column prop="email" label="邮箱"/>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button type="primary" :icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button type="danger" :icon="Delete" circle @click="handleDel(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card" v-if="data.total">
      <el-pagination @current-change="changePage" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog width="35%" v-model="data.formVisible" title="管理员信息" destroy-on-close>
      <el-form :model="data.form" ref="formRef" :rules="rules" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="账号" prop="username">
          <el-input v-model="data.form.username" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="姓名">
          <el-input v-model="data.form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="data.form.phone" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="data.form.email" autocomplete="off"/>
        </el-form-item>
    <!--    <el-form-item label="头像">
          <el-upload action="http://localhost:9090/files/upload" list-type="picture" :on-success="handleImgUploadSuccess">
            <el-button type="primary">上传头像</el-button>
          </el-upload>
        </el-form-item>-->
      </el-form>
      <template #footer>
    <span class="dialog-footer">
      <el-button @click="data.formVisible = false">取 消</el-button>
      <el-button type="primary" @click="save">保 存</el-button>
    </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive,ref} from "vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from '@element-plus/icons-vue'
const data = reactive({
  tableData: [
    { id: 1, name: '管理员', username: 'admin' },
  ],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {},
  username:null,
  name:null
})

const rules = reactive ({
  username: [
    { required: true, message: '请输入账号', trigger: 'blur' },
  ]
})

const formRef = ref()

const load = () => {
  request.get('/admin/selectPage',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      username:data.username,
      name:data.name
    }
  }).then(res =>{
    if(res.code === '200'){
      data.tableData = res.data?.list || []
      data.total= res.data?.total
    }else{
      ElMessage.error(res.msg)
    }
  })
}

const handleAdd = () => {
  data.form ={}
  data.formVisible = true
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const handleDel= (id) => {
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/admin/deleteById?id=' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success("删除成功")
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(res => {})
}

const save = () =>{
  if(data.form.id){
    //编辑
    request.put('/admin/update',data.form).then(res =>{
      if(res.code === '200'){
        ElMessage.success("更新成功")
        data.formVisible = false
        load()
      }else {
        ElMessage.error(res.msg)
      }
    })
  }else
  {
    //新增
    request.post('/admin/add',data.form).then(res =>{
      if(res.code === '200'){
        ElMessage.success("新增成功")
        data.formVisible = false
        load()
      }else {
        ElMessage.error(res.msg)
      }
    })
  }
}

const changePage =(pageNum)=>{
  data.pageNum = pageNum
  load()
}

const reset = () =>{
  data.username = null
  data.name = null
  load()
}
load()

</script>