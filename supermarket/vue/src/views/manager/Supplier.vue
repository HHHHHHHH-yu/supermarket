<template>
  <div>

    <div class="card" style="margin-bottom: 10px;">
      <el-input prefix-icon="Search" style="width: 300px; margin-right: 10px" v-model="data.name" placeholder="请输入供货商名称查询"></el-input>
      <el-button type="primary" plain round @click="load">查询</el-button>
      <el-button type="info" plain round style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 10px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" plain round @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="data.tableData" style="width: 100%">
        <el-table-column prop="id" label="序号" width="70"/>
        <el-table-column prop="name" label="供货商名称"/>
        <el-table-column prop="content" label="供货商说明"/>
        <el-table-column label="操作" width="180">
          <template #default="scope">
            <el-button supplier="primary" :icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
            <el-button supplier="danger" :icon="Delete" circle @click="handleDel(scope.row.id)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="card" v-if="data.total">
      <el-pagination @current-change="changePage" background layout="prev, pager, next" :page-size="data.pageSize" v-model:current-page="data.pageNum" :total="data.total"/>
    </div>

    <el-dialog width="35%" v-model="data.formVisible" title="供货商信息" destroy-on-close>
      <el-form :model="data.form" ref="formRef" :rules="rules" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="供货商名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="供货商说明" prop="content">
          <el-input supplier="textarea" :rows="4" v-model="data.form.content" autocomplete="off"/>
        </el-form-item>
      </el-form>
      <template #footer>
    <span class="dialog-footer">
      <el-button @click="data.formVisible = false">取 消</el-button>
      <el-button supplier="primary" @click="save">保 存</el-button>
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
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {},
  name:null,
})

const rules = reactive ({
  name: [
    { required: true, message: '请输入供货商名称', trigger: 'blur' },
  ]
})

const formRef = ref()

const load = () => {
  request.get('/supplier/selectPage',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      name:data.name,
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
    request.delete('/supplier/deleteById?id=' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success("删除成功")
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(res => {})
}

const update = (row) => {
  //编辑
  request.put('/supplier/update',data.form).then(res =>{
    if(res.code === '200'){
      ElMessage.success("更新成功")
      data.formVisible = false
      load()
    }else {
      ElMessage.error(res.msg)
    }
  })
}

const save = () =>{
  formRef.value.validate((valid) => {
    if (valid) {
      // 调用接口，处理逻辑
      if(data.form.id){
        update()
      }else
      {
        //新增
        request.post('/supplier/add',data.form).then(res =>{
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
  })

}

const changePage =(pageNum)=>{
  data.pageNum = pageNum
  load()
}

const reset = () =>{
  data.name = null
  load()
}
load()

</script>