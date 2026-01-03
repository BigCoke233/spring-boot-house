<script setup>
import { onMounted, ref, computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import PageContainer from '@/layouts/PageContainer.vue'
import { useHouseStore } from '@/stores/house.js'
import { useSellerStore } from '@/stores/seller'
import { useMessage } from '@/composables/useMessage'
import HouseEditForm from '@/components/HouseEditForm.vue'

const route = useRoute()
const router = useRouter()
const houseStore = useHouseStore()
const sellerStore = useSellerStore()
const { showSuccess, showError } = useMessage()

const initialData = ref(null)
const isLoading = computed(() => houseStore.isLoading || sellerStore.isLoading)
const isEditMode = computed(() => !!route.params.id)
const pageTitle = computed(() => isEditMode.value ? '编辑房源' : '发布房源')

onMounted(async () => {
    const id = route.params.id
    if (id) {

      try {
            // Use seller store to fetch house details (allows viewing non-approved/delisted houses)
            const house = await sellerStore.fetchSellerHouseById(id)
            if (house) {
                // Map HouseResultDTO to House entity fields
                initialData.value = {
                    h_id: house.id,
                    h_name: house.name,
                    h_describe: house.description,
                    h_address: house.address,
                    h_detail_address: house.detailAddress,
                    h_price: house.price,
                    h_square: house.square,
                    h_longitude: house.longitude,
                    h_latitude: house.latitude,
                    h_checked: house.checked, // Add checked status
                    // Map picturePaths
                    picturePaths: house.picturePaths || []
                }
            }
        } catch (e) {
            showError('加载房源信息失败: ' + e.message)
        }
    }
})

async function handleSave(formData) {
    try {
        if (isEditMode.value) {
            await sellerStore.updateHouse(formData.h_id, formData)
            showSuccess('保存成功')
        } else {
            await sellerStore.createHouse(formData)
            showSuccess('发布成功')
        }
        router.push('/seller/houses')
    } catch (e) {
        showError((isEditMode.value ? '保存' : '发布') + '失败: ' + e.message)
    }
}
</script>

<template>
    <PageContainer class="my-20">
        <div class="max-w-4xl mx-auto py-8">
            <h1 class="text-2xl font-bold mb-6">{{ pageTitle }}</h1>

            <div v-if="initialData && initialData.h_checked === 2" class="bg-yellow-50 border-l-4 border-yellow-400 p-4 mb-6">
                <div class="flex">
                    <div class="flex-shrink-0">
                        <svg class="h-5 w-5 text-yellow-400" viewBox="0 0 20 20" fill="currentColor">
                            <path fill-rule="evenodd" d="M8.257 3.099c.765-1.36 2.722-1.36 3.486 0l5.58 9.92c.75 1.334-.213 2.98-1.742 2.98H4.42c-1.53 0-2.493-1.646-1.743-2.98l5.58-9.92zM11 13a1 1 0 11-2 0 1 1 0 012 0zm-1-8a1 1 0 00-1 1v3a1 1 0 002 0V6a1 1 0 00-1-1z" clip-rule="evenodd" />
                        </svg>
                    </div>
                    <div class="ml-3">
                        <p class="text-sm text-yellow-700">
                            注意：该房源当前处于已下架/已售出状态。
                        </p>
                    </div>
                </div>
            </div>

            <div class="bg-white rounded-lg shadow p-6">
                <HouseEditForm
                    :initial-data="initialData"
                    :loading="isLoading"
                    @submit="handleSave"
                />
            </div>
        </div>
    </PageContainer>
</template>
