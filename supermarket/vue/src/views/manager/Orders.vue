<template>
  <div>

    <div class="card" style="margin-bottom: 10px;">
      <el-input prefix-icon="Search" style="width: 300px; margin-right: 10px" v-model="data.orderNo" placeholder="请输入订单编号查询"></el-input>
      <el-button type="primary" plain round @click="load">查询</el-button>
      <el-button type="info" plain round style="margin: 0 10px" @click="reset">重置</el-button>
    </div>

    <div class="card" style="margin-bottom: 10px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" plain round @click="handleAdd">新增</el-button>
      </div>
      <el-table :data="data.tableData" style="width: 100%">
        <el-table-column prop="id" label="序号" width="70"/>
        <el-table-column prop="name" label="商品名称"/>
        <el-table-column prop="orderNo" label="订单编号"/>
        <el-table-column prop="count" label="商品数量"/>
        <el-table-column prop="price" label="商品单价"/>
        <el-table-column prop="total" label="订单总价"/>
        <el-table-column prop="supplierName" label="供货商"/>
        <el-table-column prop="time" label="订单时间"/>
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

    <el-dialog width="35%" v-model="data.formVisible" title="商品订单信息" destroy-on-close>
      <el-form :model="data.form" ref="formRef" :rules="rules" label-width="100px" label-position="right" style="padding-right: 40px">
        <el-form-item label="商品名称" prop="name">
          <el-input v-model="data.form.name" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="商品数量" prop="count">
          <el-input-number v-model="data.form.count" autocomplete="off"/>
        </el-form-item>
        <el-form-item label="商品单价" prop="price">
        <!--  <el-input v-model="data.form.price" autocomplete="off"/>  -->
          <el-input-number
              v-model="data.form.price"
              :min="0"
              :precision="2"
              :step="0.1"
              @blur="calculateTotal"
              style="width: 100%"
              placeholder="请输入商品单价"
          >
            <template #prefix>¥</template>
          </el-input-number>
        </el-form-item>
        <el-form-item label="订单总价" prop="total">
         <!-- <el-input  autocomplete="off"/> -->
          <el-input-number
              v-model="data.form.total"
              :min="0"
              :precision="2"
              :step="0.1"
              placeholder="自动计算，也可手动修改"
              style="width: 100%"
              @change = "handleTotalChange"
          >
            <template #prefix>¥</template>
          </el-input-number>
          <div style="font-size: 12px; color: #999; margin-top: 5px;">
            小提示：自动计算 = 数量 × 单价，您也可以手动修改
            <br>
            <span v-if="data.form.count && data.form.price" style="color: #409eff; margin-left: 10px;">
              当前自动计算值: ¥{{ (data.form.count * data.form.price).toFixed(2) }}
            </span>
            <el-button
                v-if="showResetButton"
                type="text"
                size="small"
                @click="resetToCalculated"
                style="margin-left: 10px;"
            >
              恢复自动计算值
            </el-button>
          </div>
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
        <el-form-item label="订单时间" prop="time">
          <el-date-picker style="width:100%"
              v-model="data.form.time"
              type="date"
              placeholder="请选择日期"
              value-format="YYYY-MM-DD"
          />
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
import {reactive,ref,watch,computed,nextTick} from "vue"
import request from "@/utils/request";
import {ElMessage, ElMessageBox} from "element-plus";
import {Delete, Edit} from '@element-plus/icons-vue'
const data = reactive({
  tableData: [],
  total: 0,
  pageNum: 1,  // 当前的页码
  pageSize: 5,  // 每页的个数
  formVisible: false,
  form: {
    manualTotal:null
  },
  orderNo:null,
  supplierData:[]
})

const rules = reactive ({
  name: [
    { required: true, message: '请输入商品名称', trigger: 'blur' },
  ],
  count: [
    { required: true, message: '请输入商品数量', trigger: 'blur' },
  ],
  price: [
    { required: true, message: '请输入商品单价', trigger: 'blur' },
  ],
  total: [
    { required: true, message: '请输入订单总价', trigger: 'blur' },
  ],
  supplierId: [
    { required: true, message: '请选择供货商', trigger: 'blur' },
  ]
})

const formRef = ref()


// 计算当前自动计算的总价
const calculatedTotal = computed(() => {
  if (data.form.count && data.form.price) {
    return (data.form.count * data.form.price).toFixed(2)
  }
  return '0.00'
})


const calculateTotal = () => {
  setTimeout(() => {
    if (data.form.count && data.form.price) {
      const newTotal = parseFloat((data.form.count * data.form.price).toFixed(2))
      const currentTotal = data.form.total || 0

      if (currentTotal === 0 || Math.abs(currentTotal - newTotal) < 0.01) {
        data.form.total = newTotal
      }
    }
  }, 100)
}


// 总价输入框变化处理
const handleTotalChange = (value) => {
  console.log('总价被修改为:', value)
}

// 是否显示恢复按钮（当手动修改时显示）
const showResetButton = computed(() => {
  if (!data.form.count || !data.form.price || !data.form.total) {
    return false
  }
  const calc = parseFloat(calculatedTotal.value)
  const current = parseFloat(data.form.total)
  return Math.abs(calc - current) > 0.01
})


// 恢复自动计算
const resetToCalculated = () => {
  data.form.total = parseFloat(calculatedTotal.value)
}

const load = () => {
  request.get('/orders/selectPage',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      orderNo:data.orderNo,
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
  data.form ={
    total:0
  }
  data.formVisible = true

  nextTick(() => {
    // 计算并设置初始总价
    const total = data.form.count * data.form.price
    data.form.total = parseFloat(total.toFixed(2))
  })
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))
  data.formVisible = true
}

const handleDel= (id) => {
  ElMessageBox.confirm('删除数据后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/orders/deleteById?id=' + id).then(res => {
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
  request.put('/orders/update',data.form).then(res =>{
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
        request.post('/orders/add',data.form).then(res =>{
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
  data.orderNo = null
  load()
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

loadSupplier()
load()

</script>