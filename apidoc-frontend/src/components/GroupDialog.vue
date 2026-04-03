<template>
  <el-dialog
    v-model="visible"
    :title="isEdit ? '编辑分组' : '新增分组'"
    width="500px"
    @close="handleClose"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入分组名称" />
      </el-form-item>
      <el-form-item label="排序" prop="sortOrder">
        <el-input-number v-model="form.sortOrder" :min="0" style="width: 100%" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { apiGroupApi } from '../api'

const props = defineProps({
  modelValue: Boolean,
  group: Object,
  projectId: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['update:modelValue', 'success'])

const formRef = ref(null)
const submitting = ref(false)

const form = ref({
  name: '',
  sortOrder: 0
})

const rules = {
  name: [{ required: true, message: '请输入分组名称', trigger: 'blur' }]
}

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const isEdit = computed(() => !!props.group?.id)

watch(() => props.group, (newVal) => {
  if (newVal) {
    form.value = {
      id: newVal.id,
      name: newVal.name,
      sortOrder: newVal.sortOrder || 0
    }
  } else {
    form.value = {
      name: '',
      sortOrder: 0
    }
  }
}, { immediate: true })

const handleClose = () => {
  visible.value = false
  formRef.value?.resetFields()
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    const data = {
      ...form.value,
      projectId: props.projectId
    }

    if (isEdit.value) {
      await apiGroupApi.update(data.id, data)
      ElMessage.success('更新成功')
    } else {
      await apiGroupApi.create(data)
      ElMessage.success('创建成功')
    }

    emit('success')
    handleClose()
  } catch (error) {
    console.error('Submit failed:', error)
  } finally {
    submitting.value = false
  }
}
</script>
