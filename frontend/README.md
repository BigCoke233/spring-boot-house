# 前端说明

## 架构

- 使用 Vue 作为 Web 框架，Vite 作为构建工具
- ESLint 为代码自动审查工具，Prettifier 为代码美化工具
- 使用 pnpm 作为包管理器

## 开发环境

1. 需[安装 Node.js](https://nodejs.org/en/download)
    - Windows:
    ```shell
    # Download and install Chocolatey:
    powershell -c "irm https://community.chocolatey.org/install.ps1|iex"
    # Download and install Node.js:
    choco install nodejs --version="24.11.1"
    # Verify the Node.js version:
    node -v 
    # Verify npm version:
    npm -v 
    ```
    - macOS:
    ```shell
    # Download and install nvm:
    curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.3/install.sh | bash
    # in lieu of restarting the shell
    \. "$HOME/.nvm/nvm.sh"
    # Download and install Node.js:
    nvm install 24
    # Verify the Node.js version:
    node -v 
    # Verify npm version:
    npm -v 
    ```
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
