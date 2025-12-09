## 项目结构说明

本项目采用前后端分离架构，`frontend` 为前端（Vue + Vite），`backend` 为后端（Spring Boot）。下方用目录树展示主要结构，便于组员快速定位代码位置。

```text
JavaEE-final/
├─ backend/                      # 后端服务（Spring Boot）
│  ├─ pom.xml                    # Maven 项目配置=
│  └─ src/
│     ├─ main/
│     │  ├─ java/com/zgqf/house/
│     │  │  ├─ mapper/
│     │  │  ├─ service/
│     │  │  ├─ controller/
│     │  │  ├─ entity/
│     │  │  ├─ HouseApplication.java      # 应用入口
│     │  │  └─ ServletInitializer.java    # Servlet 初始化（WAR 部署等）
│     │  └─ resources/
│     │     └─ application.properties     # 应用配置
│     └─ test/                            # 测试
│
├─ frontend/                     # 前端应用（Vue 3 + Vite）
│  ├─ package.json               # 前端依赖与脚本
│  ├─ vite.config.js             # Vite 与插件配置
│  ├─ index.html                 # 应用入口 HTML
│  ├─ src/
│  │  ├─ main.js                 # 启动入口，挂载 Vue 应用
│  │  ├─ App.vue                 # 根组件
│  │  ├─ router/
│  │  │  └─ index.js            # 路由配置
│  │  ├─ views/                 # 视图组件目录
│  │  ├─ components/            # 复用组件目录
│  │  └─ assets/                # 静态资源
│  └─ public/
│
├─ .gitattributes
├─ .gitignore
└─ README.md                     # 根目录说明（当前文件）
```

### 前后端分离说明

- 开发与部署可独立进行：前端在 `frontend` 下运行与构建，后端在 `backend` 下运行与构建，双方通过 HTTP 接口对接。
- 代码职责清晰：UI、路由与静态资源在前端；业务逻辑、数据访问与配置在后端。

### 约定与提示

- 前端入口为 `frontend/index.html` 与 `frontend/src/main.js`，路由在 `frontend/src/router/index.js`。
- 后端入口为 `backend/src/main/java/com/zgqf/house/HouseApplication.java`，配置在 `backend/src/main/resources/application.properties`。
- 统一在各自目录内执行相关命令（如安装依赖、启动开发服务、打包构建等）。

## 关于 Git

- 克隆到本地后请创建自己的分支
```bash
git checkout -b <分支名>
```
- 提交代码时请使用有意义的提交信息
```bash
git commit -m "feat: 新增登录功能"
```
- 推送代码时请将自己的分支推送到远程仓库
```bash
git push origin <分支名>
```
- 合并代码时请使用 Pull Request 进行合并，方便其他人审查，避免直接合并到主分支
