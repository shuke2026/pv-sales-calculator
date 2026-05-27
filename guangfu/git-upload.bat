@echo off
chcp 65001 >nul
echo ========================================
echo 光伏测算系统 - Git 上传脚本
echo ========================================
echo.

cd /d e:\guangfu\guangfu

echo [1/7] 当前目录:
cd
echo.

echo [2/7] 检查项目文件...
if exist "build.gradle.kts" (
    echo ✓ 找到项目文件
) else (
    echo ✗ 未找到项目文件，请确认目录正确
    pause
    exit /b 1
)
echo.

echo [3/7] 初始化 Git 仓库...
git init
echo.

echo [4/7] 配置 Git 用户信息...
set /p username="shuke2026 "
set /p email="1594118857@qq.com "
git config user.name "%username%"
git config user.email "%email%"
echo ✓ Git 用户信息已配置
echo.

echo [5/7] 添加项目文件...
git add .
echo ✓ 文件已添加
echo.

echo [6/7] 提交代码...
git commit -m "Initial commit: 光伏销售端智能测算系统"
echo ✓ 代码已提交
echo.

echo [7/7] 连接远程仓库...
set /p repo="请输入您的 GitHub 仓库地址 (例如: https://github.com/username/pv-sales-calculator.git): "
git branch -M main
git remote add origin %repo%
echo.

echo ========================================
echo 准备推送到 GitHub...
echo ========================================
echo.
echo 请按任意键开始推送...
pause >nul

git push -u origin main

echo.
echo ========================================
echo ✓ 上传完成！
echo ========================================
echo.
echo 请访问您的 GitHub 仓库查看：
echo 1. Actions 页面 - 查看 APK 构建进度
echo 2. Releases 页面 - 下载构建好的 APK 文件
echo.
pause