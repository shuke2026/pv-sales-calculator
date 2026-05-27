# Git 上传步骤指南

## 1. 切换到项目目录并初始化 Git

在命令提示符或 PowerShell 中执行：

```bash
# 切换到项目目录
cd e:\guangfu\guangfu

# 查看当前目录（确认是否正确）
dir

# 初始化 Git 仓库
git init
```

## 2. 配置 Git 用户信息

```bash
# 配置用户名（替换为您的 GitHub 用户名）
git config user.name "Your Name"

# 配置邮箱（替换为您的 GitHub 邮箱）
git config user.email "your.email@example.com"
```

## 3. 添加项目文件

```bash
# 添加所有文件
git add .

# 查看已添加的文件（确认是否正确）
git status
```

## 4. 提交代码

```bash
# 提交
git commit -m "Initial commit: 光伏销售端智能测算系统"
```

## 5. 创建 GitHub 仓库

1. 访问 https://github.com/new
2. 仓库名称：`pv-sales-calculator`
3. 保持其他选项默认
4. 点击 **Create repository**

## 6. 连接远程仓库并推送

```bash
# 添加远程仓库（替换为您的 GitHub 用户名）
git remote add origin https://github.com/yourusername/pv-sales-calculator.git

# 推送到 GitHub
git branch -M main
git push -u origin main
```

## 7. 查看自动构建

推送到 GitHub 后：
1. 访问您的 GitHub 仓库
2. 点击 **Actions** 标签
3. 等待构建完成（约 5-10 分钟）
4. 在 **Artifacts** 中下载 APK 文件
5. 在 **Releases** 中查看发布的版本

## 常见问题

### 如果提示需要身份验证
```bash
# 使用 Personal Access Token
git push -u origin main
# 输入 GitHub 用户名
# 输入 Personal Access Token（不是密码）
```

### 如果推送失败
```bash
# 强制推送（谨慎使用）
git push -u origin main --force
```

### 查看远程仓库配置
```bash
git remote -v
```

### 修改远程仓库地址
```bash
git remote set-url origin https://github.com/yourusername/pv-sales-calculator.git
```