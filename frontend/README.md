# 前端说明

## 架构

- **框架**：Vue.js
- **打包构建**：Vite
- **代码格式化**：ESLint + Prettifier
- **包管理器：**pnpm（不要使用 npm）

### 其他技术栈

- 使用 [UnoCSS](https://unocss.dev/guide/) 提供原子化 CSS，**尽量少编写 CSS 代码**，使用 UnoCSS 提供的工具类，如 `text-lg` `overflow-hidden`
- 使用 [Lucide](https://lucide.dev/guide/) 提供图标。

## 开发环境

1. 需[安装 Node.js](https://nodejs.org/en/download)
2. 需[安装 pnpm](https://pnpm.io/installation#using-npm)
    ```shell
    npm install -g pnpm@latest-10
    ```

## 开发

1. 安装前端依赖项
    ```sh
    pnpm install
    ```
2. 开发，运行命令后会在本地启动一个 HTTP 服务器，能够实时看到前端代码修改后的结果
    ```sh
    pnpm dev
    ```
3. 编译构建
    ```sh
    pnpm build
    ```
4. 格式化代码并排查问题
    ```sh
    pnpm lint
    ```
