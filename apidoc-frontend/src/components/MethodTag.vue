<template>
  <el-tag :type="tagType" size="small">{{ method }}</el-tag>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  method: {
    type: String,
    required: true
  }
})

const tagType = computed(() => {
  const map = {
    'GET': 'success',
    'POST': 'primary',
    'PUT': 'warning',
    'DELETE': 'danger'
  }
  return map[props.method] || 'info'
})
</script>
