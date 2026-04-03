import axios from './axios'

export const projectApi = {
  list: () => axios.get('/api/project/list'),
  getById: (id) => axios.get(`/api/project/${id}`),
  create: (data) => axios.post('/api/project', data),
  update: (id, data) => axios.put(`/api/project/${id}`, data),
  delete: (id) => axios.delete(`/api/project/${id}`)
}

export const apiGroupApi = {
  listByProjectId: (projectId) => axios.get(`/api/group/list/${projectId}`),
  getById: (id) => axios.get(`/api/group/${id}`),
  create: (data) => axios.post('/api/group', data),
  update: (id, data) => axios.put(`/api/group/${id}`, data),
  delete: (id) => axios.delete(`/api/group/${id}`)
}

export const apiEndpointApi = {
  page: (params) => axios.get('/api/endpoint/page', { params }),
  listByProjectId: (projectId) => axios.get(`/api/endpoint/list/${projectId}`),
  getById: (id) => axios.get(`/api/endpoint/${id}`),
  create: (data) => axios.post('/api/endpoint', data),
  update: (id, data) => axios.put(`/api/endpoint/${id}`, data),
  delete: (id) => axios.delete(`/api/endpoint/${id}`)
}

export const debugApi = {
  send: (data) => axios.post('/api/debug/send', data)
}
