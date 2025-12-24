<script setup>
import { onMounted, ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PageContainer from '@/layouts/PageContainer.vue'
import { useHouseStore } from '@/stores/house.js'
import { useUserStore } from '@/stores/user.js'
import AppButton from '@/components/AppButton.vue'
import LeafletMap from '@/components/LeafletMap.vue'
import { useSellerStore } from '@/stores/seller'

const route = useRoute()
const router = useRouter()
const houseStore = useHouseStore()
const sellerStore = useSellerStore()
const userStore = useUserStore()

const form = ref({
    id: null,
    name: '',
    address: '',
    price: 0,
    square: 0,
    description: '',
    developer: '',
    coordinates: null,
    images: [], // List of image URLs
})

// Store newly selected files separately
const newImageFiles = ref([])

const isLoading = computed(() => houseStore.isLoading)
const isEditMode = computed(() => !!route.params.id)
const pageTitle = computed(() => isEditMode.value ? '编辑房源' : '发布房源')

onMounted(async () => {
    const id = route.params.id
    if (id) {
        const house = await houseStore.fetchHouseById(id)
        if (house) {
            form.value = { 
                ...house,
                images: house.images || (house.image ? [house.image] : [])
            }
        }
    }
})

function handleMapUpdate(coords) {
    form.value.coordinates = coords
}

function handleImageSelect(event) {
    const files = Array.from(event.target.files)
    if (!files.length) return

    newImageFiles.value.push(...files)

    // Create local preview URLs
    files.forEach(file => {
        const reader = new FileReader()
        reader.onload = (e) => {
            form.value.images.push(e.target.result)
        }
        reader.readAsDataURL(file)
    })
}

function removeImage(index) {
    form.value.images.splice(index, 1)
    // Note: If we were tracking file objects separately from URLs precisely, we'd need more complex logic.
    // For mock/prototype, this visual removal is sufficient.
    // In real app, we would distinguish between existing URLs and new Files.
}

async function handleSave() {
    try {
        // Prepare data to save. If there's a primary image field, update it with the first image.
        const saveData = { ...form.value }
        if (saveData.images && saveData.images.length > 0) {
            saveData.image = saveData.images[0]
        }
        
        if (isEditMode.value) {
            await sellerStore.updateHouse(saveData.h_id || saveData.id, saveData)
            alert('保存成功')
        } else {
            saveData.sellerId = userStore.userInfo?.id || 1
            await sellerStore.createHouse(saveData)
            alert('发布成功')
        }
        
        router.push('/seller/houses')
    } catch (e) {
        alert((isEditMode.value ? '保存' : '发布') + '失败: ' + e.message)
    }
}
</script>

<template>
  <PageContainer class="my-20">
    <header class="mb-8">
        <h1 class="text-2xl font-bold">{{ pageTitle }}</h1>
    </header>

    <div v-if="isLoading && isEditMode && !form.id" class="text-center py-10">
        加载中...
    </div>

    <form v-else @submit.prevent="handleSave" class="bg-white p-8 rd-lg shadow-sm border border-neutral-200 max-w-2xl mx-auto space-y-6">
        
        <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">房源名称</label>
            <input v-model="form.name" type="text" required class="w-full px-3 py-2 border border-gray-300 rd focus:outline-none focus:ring-2 focus:ring-black" />
        </div>

        <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">地址</label>
            <input v-model="form.address" type="text" required class="w-full px-3 py-2 border border-gray-300 rd focus:outline-none focus:ring-2 focus:ring-black" />
        </div>

        <div class="grid grid-cols-2 gap-4">
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">单价 (¥/m²)</label>
                <input v-model.number="form.price" type="number" min="0" required class="w-full px-3 py-2 border border-gray-300 rd focus:outline-none focus:ring-2 focus:ring-black" />
            </div>
            <div>
                <label class="block text-sm font-medium text-gray-700 mb-1">面积 (m²)</label>
                <input v-model.number="form.square" type="number" min="0" required class="w-full px-3 py-2 border border-gray-300 rd focus:outline-none focus:ring-2 focus:ring-black" />
            </div>
        </div>

        <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">开发商</label>
            <input v-model="form.developer" type="text" class="w-full px-3 py-2 border border-gray-300 rd focus:outline-none focus:ring-2 focus:ring-black" />
        </div>

        <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">描述</label>
            <textarea v-model="form.description" rows="4" class="w-full px-3 py-2 border border-gray-300 rd focus:outline-none focus:ring-2 focus:ring-black"></textarea>
        </div>

        <!-- Coordinates Map Picker -->
        <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">地理位置 (点击地图选择)</label>
            <div class="border border-gray-300 rd overflow-hidden">
                 <LeafletMap 
                    :center="form.coordinates || [39.9042, 116.4074]" 
                    :zoom="12"
                    :editable="true"
                    @update:center="handleMapUpdate"
                />
            </div>
            <p class="text-xs text-neutral-500 mt-1">
                当前坐标: {{ form.coordinates ? `${form.coordinates[0].toFixed(4)}, ${form.coordinates[1].toFixed(4)}` : '未选择' }}
            </p>
        </div>

        <!-- Image Upload -->
        <div>
            <label class="block text-sm font-medium text-gray-700 mb-1">房源图片</label>
            <div class="flex flex-wrap gap-4 mb-4">
                <div v-for="(img, idx) in form.images" :key="idx" class="relative w-24 h-24 rd overflow-hidden border border-neutral-200 group">
                    <img :src="img" class="w-full h-full object-cover" />
                    <button type="button" @click="removeImage(idx)" class="absolute top-0 right-0 bg-red-500 text-white w-5 h-5 flex items-center justify-center text-xs opacity-0 group-hover:opacity-100 transition">×</button>
                </div>
                <label class="w-24 h-24 flex flex-col items-center justify-center border-2 border-dashed border-gray-300 rd cursor-pointer hover:border-gray-400 hover:bg-gray-50 transition text-neutral-400">
                    <span class="text-2xl">+</span>
                    <span class="text-xs">上传图片</span>
                    <input type="file" multiple accept="image/*" class="hidden" @change="handleImageSelect" />
                </label>
            </div>
        </div>

        <div class="flex justify-end gap-4 pt-4 border-t border-gray-100">
            <AppButton type="button" variant="secondary" @click="router.back()">取消</AppButton>
            <AppButton type="submit" :disabled="isLoading">
                {{ isLoading ? (isEditMode ? '保存中...' : '发布中...') : (isEditMode ? '保存修改' : '确认发布') }}
            </AppButton>
        </div>
    </form>
  </PageContainer>
</template>
