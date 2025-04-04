/*
 * @Description: 
 * @Author: Rabbiter
 * @Date: 2023-03-12 00:17:38
 */
module.exports = {
  devServer: {
    // 项目运行时候的端口号
    port: 9212
  },
  lintOnSave: false
  pathRewrite: { 
    '^/association': '/association' // 保留路径前缀
  }
}
