<template>
  <div>

    <div class="card" style="line-height: 30px">
      <div>欢迎您，{{ user.name }} 进入超市管理系统！</div>
      <img src="@/assets/imgs/home.png" alt="" style="margin-top: 20px; width: 70%; height: 400px">
    </div>

  </div>
</template>

<script setup>
  import request from "@/utils/request";
  const user = JSON.parse(localStorage.getItem('user') || '{}')
</script>