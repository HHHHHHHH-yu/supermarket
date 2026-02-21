<template>
  <div>
    <div class="card" style="margin-bottom: 10px;">
      <el-input prefix-icon="Search" style="width: 300px; margin-right: 10px" v-model="data.name" placeholder="请输入名称查询"></el-input>
      <el-button type="primary" plain round @click="load">查询</el-button>
      <el-button type="info" plain round style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 10px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" plain round @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="data.tableData" style="width: 100%">
        <el-table-column prop="id" label="序号" width="70"/>
        <el-table-column prop="img" label="参考图">
          <template #default="scope">
            <el-image
                :src="scope.row.img"
                style="width: 60px; height: 60px; border-radius: 5px"
                :preview-src-list="[scope.row.img]"
                preview-teleported
                v-if="scope.row.img">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" label="商品名称"/>
        <el-table-column prop="price" label="价格"/>
        <el-table-column prop="categoryName" label="商品分类"/>
        <el-table-column prop="supplierName" label="供货商"/>
        <el-table-column prop="count" label="库存"/>
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
    <el-dialog width="35%" v-model="data.formVisible" title="商品信息" destroy-on-close>
      <el-form :model="data.form" ref="formRef" :rules="rules" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="商品图片" prop="img">
          <el-upload
              class="upload-demo"
              :action="'http://localhost:9090/files/upload'"
              list-type="picture"
              :on-success="handleFileUpload"
          >
            <el-button type="primary">点击上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="商品价格" prop="price">
          <el-input v-model="data.form.price" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="商品分类" prop="categoryId">
          <el-select
              v-model="data.form.categoryId"
              placeholder="请选择商品分类"
              style="width: 100%"
          >
            <el-option
                v-for="item in data.categoryData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="供货商" prop="supplierId">
          <el-select
              v-model="data.form.supplierId"
              placeholder="请选择供货商"
              style="width: 100%"
          >
            <el-option
                v-for="item in data.supplierData"
                :key="item.id"
                :label="item.name"
                :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="库存" prop="count">
          <el-input v-model="data.form.count" autocomplete="off"/>
        </el-form-item>
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
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {},
  categoryData: [],
  supplierData: [],
  name:null
})

const rules = reactive ({
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
  ],
  price: [
    { required: true, message: '请输入商品价格', trigger: 'blur' },
  ]
})

const formRef = ref()

const load = () => {
  request.get('/goods/selectPage',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
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
    request.delete('/goods/deleteById/' + id).then(res => {
      if (res.code === '200') {
        load()
        ElMessage.success("删除成功")
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(res => {})
}
const update = () => {
  if (data.form.id) {
    //编辑
    request.put('/goods/update', data.form).then(res => {
      if (res.code === '200') {
        ElMessage.success("更新成功")
        data.formVisible = false
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }
}
  const add = () => {
      //新增
      request.post('/goods/add', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success("新增成功")
          data.formVisible = false
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
  }
const save = () =>{
      formRef.value.validate((valid) => {
        if (valid) {
          // 调用接口，处理逻辑
          if (data.form.id) {
            update()  // 有 id -> 编辑
          } else {
            add()     // 无 id -> 新增
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
const handleFileUpload = (res) => {
  data.form.img = res.data
  console.log(data.form.img)
}

const loadCategory = () => {
  request.get('/category/selectAll').then(res =>{
    if(res.code === '200'){
      data.categoryData = res.data
    }else{
      ElMessage.error(res.msg)
    }
  })
}

const loadSupplier = () => {
  request.get('/supplier/selectAll').then(res =>{
    if(res.code === '200'){
      data.supplierData = res.data
    }else{
      ElMessage.error(res.msg)
    }
  })
}
loadCategory()
loadSupplier()
load()

</script>