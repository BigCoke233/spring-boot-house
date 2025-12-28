import { ElMessage, ElMessageBox } from 'element-plus'

export function useMessage() {
  const showMessage = (message, type = 'success') => {
    ElMessage({
      message,
      type,
      duration: 3000,
      showClose: true,
    })
  }

  const showSuccess = (message) => showMessage(message, 'success')
  const showWarning = (message) => showMessage(message, 'warning')
  const showInfo = (message) => showMessage(message, 'info')
  const showError = (message) => showMessage(message, 'error')

  const showConfirm = (message, title = '提示') => {
    return ElMessageBox.confirm(message, title, {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
  }
  
  const showAlert = (message, title = '提示') => {
      return ElMessageBox.alert(message, title, {
          confirmButtonText: '确定'
      })
  }

  return {
    showMessage,
    showSuccess,
    showWarning,
    showInfo,
    showError,
    showConfirm,
    showAlert
  }
}
