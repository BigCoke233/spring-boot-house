<script setup></script>

<template>
  <footer class="footer-pattern w-full h-[20rem]">
    <div class="relative z-1 mx-auto max-w-7xl px-6 py-10 text-neutral-700">
      <div class="grid grid-cols-2 md:grid-cols-3 gap-8">
        <div>
          <h3 class="text-lg font-semibold">商品房售卖系统</h3>
          <p class="mt-2 text-sm">公司简介占位，用于说明品牌与价值主张。系统聚焦商品房销售全流程，提供透明房源信息、便捷的线上咨询与高效交易体验。</p>
          <p class="mt-2 text-sm">通过前后端分离架构保障性能与安全，支持合同管理、账号服务、房源对比与售后支持，助力数字化营销与合规运营。</p>
        </div>

        <div>
          <h4 class="text-base font-semibold">标题A</h4>
          <ul class="mt-3 space-y-2 text-sm">
            <li><a href="#">链接2</a></li>
            <li><a href="#">链接2</a></li>
            <li><a href="#">链接2</a></li>
            <li><a href="#">链接2</a></li>
          </ul>
        </div>

        <div>
          <h4 class="text-base font-semibold">标题A</h4>
          <ul class="mt-3 space-y-2 text-sm">
            <li><a href="#">链接2</a></li>
            <li><a href="#">链接2</a></li>
            <li><a href="#">链接2</a></li>
            <li><a href="#">链接2</a></li>
          </ul>
        </div>
      </div>

      <div class="mt-10 h-px bg-neutral-100"></div>

      <div class="mt-6 flex flex-col md:flex-row justify-between items-center gap-3 text-sm text-neutral-600">
        <div>© 2025 公司名称。保留所有权利。</div>
        <div class="flex flex-wrap items-center gap-4">
          <a href="#">隐私政策</a>
          <a href="#">服务条款</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer-pattern {
  background-image: url("data:image/svg+xml,%3Csvg width='42' height='44' viewBox='0 0 42 44' xmlns='http://www.w3.org/2000/svg'%3E%3Cg id='Page-1' fill='none' fill-rule='evenodd'%3E%3Cg id='brick-wall' fill='%23eee' fill-opacity='0.4'%3E%3Cpath d='M0 0h42v44H0V0zm1 1h40v20H1V1zM0 23h20v20H0V23zm22 0h20v20H22V23z'/%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
}
.footer-pattern::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 3rem;
  background: linear-gradient(to bottom, rgba(255, 255, 255, 1), rgba(255, 255, 255, 0));
  pointer-events: none;
  z-index: 0;
}
</style>
