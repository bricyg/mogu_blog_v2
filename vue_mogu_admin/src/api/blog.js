import request from '@/utils/request'

export function getBlogList(params) {
  return request({
    url: process.env.ADMIN_API + '/blog/getList',
    method: 'get',
    params
  })
}

export function addBlog(params) {
  return request({
    url: process.env.ADMIN_API + '/blog/add',
    method: 'post',
    params
  })
}

export function editBlog(params) {
  return request({
    url: process.env.ADMIN_API + '/blog/edit',
    method: 'post',
    params
  })
}

export function deleteBlog(params) {
  return request({
    url: process.env.ADMIN_API + '/blog/delete',
    method: 'post',
    params
  })
}
