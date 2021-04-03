<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/othneildrew/Best-README-Template">
    <img src="https://cdn.jsdelivr.net/gh/kongmoumou/gallery/imgs/jiucai.jpg" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">韭菜商城</h3>

  <p align="center">
    <img src="https://img.shields.io/github/v/release/kongmoumou/jiucai-service" alt="GitHub release"></img>
    <img src="https://github.com/kongmoumou/jiucai-service/actions/workflows/ci.yml/badge.svg" alt="GitHub Workflow Status"></img>
  </p>
</p>

<!-- GETTING STARTED -->
## 上手

### 准备工作

<details>
  <summary>配置 maven 阿里云镜像仓库加速（可选）</summary>
  <p>

修改 maven 配置参考下面示例

* `settings.xml` 地址 `${user.home}/.m2/settings.xml`

```xml
<settings ...>
  ...
  <!-- copy this -->
  <mirrors>
    <mirror>
      <id>aliyunmaven</id>
      <mirrorOf>*</mirrorOf>
      <name>阿里云公共仓库</name>
      <url>https://maven.aliyun.com/repository/public</url>
    </mirror>
  </mirrors>
  ...
</settings>
```

  </p>
</details>

### 用法

```bash
# ⚠ windows 下使用 mvnw.cmd
# ⚠ linux 下使用 mvnw

# 运行项目
./mvnw spring-boot:run

# 打 jar 包
./mvnw -B  package --file pom.xml
```

<!-- CONTRIBUTING -->
## 开发

1. 创建分支
1. 提交（符合 `conventional-changelog` 规范，推荐使用 [commitizen](https://github.com/commitizen/cz-cli#conventional-commit-messages-as-a-global-utility) cli 工具）
2. 推送远程仓库 (`git push origin <feature-branch>`)
3. 合并到主分支

## 启动

```bash
# use jar
java -jar app.jar

# use docker
# download image archive from github release page
docker load -i jiucai-service.tar
# run docker
docker run -d \
-p 8080:8080 \
-e 'DB_USER=<your_db_user>' \
-e 'DB_PWD=<your_db_password>' \
-e 'DB_URL=<your_db_url>' \
jiucai-service
```
