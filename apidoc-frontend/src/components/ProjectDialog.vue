<template>
  <el-dialog
    v-model="visible"
    :title="isEdit ? '编辑项目' : '新增项目'"
    width="500px"
    @close="handleClose"
  >
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
      <el-form-item label="名称" prop="name">
        <el-input v-model="form.name" placeholder="请输入项目名称" />
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入项目描述" />
      </el-form-item>
      <el-form-item label="基础URL" prop="baseUrl">
        <el-input v-model="form.baseUrl" placeholder="请输入基础URL，如 http://api.example.com" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
          <el-option :label="'已完成'" :value="1" />
          <el-option :label="'开发中'" :value="2" />
          <el-option :label="'已废弃'" :value="3" />
        </el-select>
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
import { projectApi } from '../api'

const props = defineProps({
  modelValue: Boolean,
  project: Object
})

const emit = defineEmits(['update:modelValue', 'success'])

const formRef = ref(null)
const submitting = ref(false)

const form = ref({
  name: '',
  description: '',
  baseUrl: '',
  status: 1
})

const rules = {
  name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }]
}

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val)
})

const isEdit = computed(() => !!props.project?.id)

watch(() => props.project, (newVal) => {
  if (newVal) {
    form.value = {
      id: newVal.id,
      name: newVal.name,
      description: newVal.description || '',
      baseUrl: newVal.baseUrl || '',
      status: newVal.status || 1
    }
  } else {
    form.value = {
      name: '',
      description: '',
      baseUrl: '',
      status: 1
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

    if (isEdit.value) {
      await projectApi.update(form.value.id, form.value)
      ElMessage.success('更新成功')
    } else {
      await projectApi.create(form.value)
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
