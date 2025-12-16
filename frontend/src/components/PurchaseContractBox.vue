<script setup>
const props = defineProps({
  partyA: {
    type: Object,
    default: () => ({ name: '甲方', id: '', phone: '', address: '' }),
  },
  partyB: {
    type: Object,
    default: () => ({ name: '乙方', id: '', phone: '', address: '' }),
  },
  contract: {
    type: Object,
    default: () => ({
      contractId: null,
      houseName: '',
      address: '',
      totalPrice: 0,
      payWay: 'full',
      totalPeriods: 0,
      paidCount: 0,
      deliveryEnding: null,
    }),
  },
})
</script>

<template>
  <div class="bg-white w-200 mx-auto rd shadow p-8 md:p-12 font-serif text-base md:text-lg leading-relaxed">
    <header>
      <h1 class="text-2xl font-bold text-center">购房合同</h1>
    </header>

    <section class="mt-4 space-y-3">
      <p class="indent-8">甲方：{{ props.partyA.name }}，身份证：{{ props.partyA.id }}，电话：{{ props.partyA.phone }}，地址：{{ props.partyA.address }}</p>
      <p class="indent-8">乙方：{{ props.partyB.name }}，身份证：{{ props.partyB.id }}，电话：{{ props.partyB.phone }}，地址：{{ props.partyB.address }}</p>
    </section>

    <section class="mt-4 space-y-3">
      <p class="indent-8">标的房屋：{{ props.contract.houseName }}（地址：{{ props.contract.address }}）。双方确认标的房屋信息准确无误，对房屋现状及相关权属状况已充分了解。</p>
      <p class="indent-8">总价：¥{{ props.contract.totalPrice }}。双方同意以该价格为本合同的最终成交价，结算方式以本合同约定为准。</p>
      <p class="indent-8">支付方式：{{ props.contract.payWay === 'installment' ? '分期' : '全款' }}。<span v-if="props.contract.payWay === 'installment'">分期：总期数 {{ props.contract.totalPeriods }}，已付 {{ props.contract.paidCount }}。</span></p>
      <p class="indent-8">预计交付截止：{{ props.contract.deliveryEnding ?? '—' }}。双方确认按照约定时间办理交付手续，如遇不可抗力或政策原因，双方应协商调整相关安排。</p>
    </section>

    <section class="mt-6">
      <h2 class="text-lg font-semibold">法律责任声明</h2>
      <div class="mt-2 space-y-3">
        <p class="indent-8">双方确认，在签署本合同前均已完整阅读并理解全部条款，对合同内容不存在理解或异议问题。双方保证所提供信息真实、完整、合法。</p>
        <p class="indent-8">甲乙双方承诺遵守国家法律法规及地方政策，按照合同约定履行付款、交房及相关义务。任何一方因违约导致对方损失的，应依法承担违约责任并赔偿损失。</p>
        <p class="indent-8">双方同意，如因不可抗力导致合同无法按约定履行，双方应本着诚实信用原则及时协商处理；协商不成的，可向有管辖权的人民法院提起诉讼或依约选择仲裁。</p>
        <p class="indent-8">本合同一经双方签字或盖章即生效；合同的变更、解除须经双方书面确认。与本合同相关的附件、补充协议均构成本合同不可分割的组成部分，具有同等法律效力。</p>
      </div>
    </section>

    <section class="mt-6 grid grid-cols-2 gap-6 text-sm">
      <div>
        <div>甲方签字：</div>
        <div class="mt-10 b-b-1 b-neutral-300"></div>
        <div class="mt-2">日期：__________</div>
      </div>
      <div>
        <div>乙方签字：</div>
        <div class="mt-10 b-b-1 b-neutral-300"></div>
        <div class="mt-2">日期：__________</div>
      </div>
    </section>
  </div>
</template>
