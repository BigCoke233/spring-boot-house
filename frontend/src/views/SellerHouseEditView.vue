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
    <PageContainer>
        <div class="max-w-4xl mx-auto py-8">
            <h1 class="text-2xl font-bold mb-6">{{ pageTitle }}</h1>
            
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
