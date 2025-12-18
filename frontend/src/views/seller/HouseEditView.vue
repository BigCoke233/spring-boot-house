<script setup>
const house = {
    id: 1,
    name: '房屋名称',
    price: 1000000,
    address: '房屋地址',
    description: '房屋描述',
}
</script>

<template>
    <PageContainer class="my-20">
        <HouseEditForm :house="house" />
    </PageContainer>
</template>