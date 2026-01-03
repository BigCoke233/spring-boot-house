<script setup>
import { ref, watch } from 'vue'
import LeafletMap from '@/components/LeafletMap.vue'
import { Delete, Upload } from '@element-plus/icons-vue'
import { getImageUrl } from '@/utils/imageUrl.js'

const props = defineProps({
    initialData: {
        type: Object,
        default: () => ({})
    },
    loading: {
        type: Boolean,
        default: false
    }
})

const emit = defineEmits(['submit'])

// Initialize form data
const formData = ref({
    h_id: null,
    h_name: '',
    h_describe: '',
    h_address: '',
    h_detail_address: '',
    h_price: null,
    h_square: null,
    h_longitude: null,
    h_latitude: null,
    picturePaths: [] // Added picturePaths
})

// Initialize map coordinates
const mapCoordinates = ref(null)
const uploadLoading = ref(false)

// Watch for initialData changes to populate form
watch(() => props.initialData, (newData) => {
    if (newData) {
        formData.value = { ...formData.value, ...newData }
        // Ensure picturePaths is an array
        if (!formData.value.picturePaths) {
            formData.value.picturePaths = []
        }

        // Map backend fields to frontend logic if needed
        if (newData.h_latitude && newData.h_longitude) {
            mapCoordinates.value = [newData.h_latitude, newData.h_longitude]
        }
    }
}, { immediate: true, deep: true })

function handleMapUpdate(coords) {
    if (coords) {
        mapCoordinates.value = coords
        formData.value.h_latitude = coords[0]
        formData.value.h_longitude = coords[1]
    }
}

async function handleFileUpload(event) {
    const file = event.target.files[0]
    if (!file) return

    uploadLoading.value = true
    const uploadData = new FormData()
    uploadData.append('file', file)

    try {
        const response = await fetch('http://localhost:8080/api/upload', {
            method: 'POST',
            body: uploadData,
            credentials: 'include'
        })

        if (!response.ok) throw new Error('Upload failed')

        const url = await response.text()
        formData.value.picturePaths.push(url)
    } catch (error) {
        console.error('Upload error:', error)
        alert('上传失败，请重试')
    } finally {
        uploadLoading.value = false
        // Reset input
        event.target.value = ''
    }
}

function removeImage(index) {
    formData.value.picturePaths.splice(index, 1)
}

function handleSubmit() {
    emit('submit', { ...formData.value })
}

defineExpose({
    submit: handleSubmit
})
</script>

<template>
    <form @submit.prevent="handleSubmit" class="space-y-6">
        <!-- Basic Information -->
        <div class="space-y-4">
            <h3 class="text-lg font-medium text-gray-900">基本信息</h3>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                    <label class="block text-sm font-medium text-gray-700">房源名称</label>
                    <input v-model="formData.h_name" type="text" required
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm border p-2"
                        placeholder="请输入房源名称" />
                </div>

                <div>
                    <label class="block text-sm font-medium text-gray-700">价格 (元)</label>
                    <input v-model.number="formData.h_price" type="number" required min="0"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm border p-2"
                        placeholder="请输入价格" />
                </div>

                <div>
                    <label class="block text-sm font-medium text-gray-700">面积 (平米)</label>
                    <input v-model.number="formData.h_square" type="number" required min="0"
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm border p-2"
                        placeholder="请输入面积" />
                </div>

                <div>
                    <label class="block text-sm font-medium text-gray-700">区域/城市</label>
                    <input v-model="formData.h_address" type="text" required
                        class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm border p-2"
                        placeholder="例如：北京市朝阳区" />
                </div>
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">详细地址</label>
                <input v-model="formData.h_detail_address" type="text"
                    class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm border p-2"
                    placeholder="请输入详细街道地址" />
            </div>

            <div>
                <label class="block text-sm font-medium text-gray-700">房源描述</label>
                <textarea v-model="formData.h_describe" rows="4"
                    class="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500 sm:text-sm border p-2"
                    placeholder="请输入房源详细描述"></textarea>
            </div>
        </div>

        <!-- Image Upload -->
        <div class="space-y-4">
            <h3 class="text-lg font-medium text-gray-900">房源图片</h3>
            <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                <div v-for="(path, index) in formData.picturePaths" :key="index" class="relative group aspect-square rounded-lg overflow-hidden border border-gray-200">
                    <img :src="getImageUrl(path)" class="w-full h-full object-cover" />
                    <button type="button" @click="removeImage(index)"
                        class="absolute top-2 right-2 p-1 bg-red-600 text-white rounded-full opacity-0 group-hover:opacity-100 transition-opacity">
                        <Delete class="w-4 h-4" />
                    </button>
                </div>

                <label class="flex flex-col items-center justify-center aspect-square border-2 border-dashed border-gray-300 rounded-lg cursor-pointer hover:border-blue-500 hover:bg-blue-50 transition-colors">
                    <div v-if="uploadLoading" class="text-sm text-gray-500">上传中...</div>
                    <div v-else class="flex flex-col items-center">
                        <Upload class="w-8 h-8 text-gray-400 mb-2" />
                        <span class="text-sm text-gray-500">点击上传图片</span>
                    </div>
                    <input type="file" class="hidden" accept="image/*" @change="handleFileUpload" :disabled="uploadLoading" />
                </label>
            </div>
        </div>

        <!-- Location Map -->
        <div class="space-y-4">
            <h3 class="text-lg font-medium text-gray-900">地理位置</h3>
            <p class="text-sm text-gray-500">请在地图上点击选择房源的具体位置</p>
            <div class="h-96 w-full rounded-lg overflow-hidden border border-gray-300">
                <LeafletMap
                    :center="mapCoordinates || [39.9042, 116.4074]"
                    :zoom="12"
                    :editable="true"
                    @update:center="handleMapUpdate"
                />
            </div>
            <div class="grid grid-cols-2 gap-4 text-sm text-gray-500">
                <div>经度: {{ formData.h_longitude || '-' }}</div>
                <div>纬度: {{ formData.h_latitude || '-' }}</div>
            </div>
        </div>

        <!-- Submit Button -->
        <div class="flex justify-end pt-4">
            <button type="submit" :disabled="loading"
                class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 disabled:bg-blue-300">
                {{ loading ? '保存中...' : '保存房源' }}
            </button>
        </div>
    </form>
</template>
