### 1. 核心功能
#### 1.1 提供两个 HTTP 接口
- GET /

返回固定的字符串 "hello world --Version"，用于基础连通性测试。
   
用途：检查服务是否正常运行。

- GET /sip

返回当前服务器的 IP 地址和主机名（通过 InetAddress 获取）。

用途：快速查看服务部署的机器信息。

参数：

req（可选）：未实际使用，可能是预留字段。

#### 1.2 模拟后台日志

启动两个后台线程（LogWorker 和 ErrorLogWorker），定期生成模拟的系统日志和错误日志：

LogWorker：每 2~5 秒随机打印一条普通日志（70% 概率为 INFO，30% 概率为 WARN）。

日志内容来自预定义的 RANDOM_MESSAGES 数组（如“系统运行正常”“CPU负载: 中等”）。

ErrorLogWorker：每 5~15 秒随机打印一条错误日志（30% 概率为 ERROR，70% 概率为 WARN）。

日志内容来自 ERROR_MESSAGES 数组（如“数据库连接失败”“权限不足”），并附带模拟的异常。

### 2. 关键设计

日志模拟：

通过随机数和休眠（TimeUnit.SECONDS.sleep）模拟真实系统的日志行为。

使用 SLF4J 日志框架记录日志，便于集成日志收集系统（如 ELK）。

线程管理：

在 @PostConstruct 初始化方法中启动线程，确保服务启动后自动运行。

线程为无限循环，但通过 InterruptedException 处理中断（规范做法）。

随机性：

通过 Random 控制日志频率、类型和内容，增加真实性。

### 3. 适用场景

开发/测试环境：

快速验证日志监控系统（如 Prometheus + Grafana）的配置。

模拟服务运行时的日志输出，测试日志收集和分析工具。

教学示例：

展示 Spring Boot 的基本用法、多线程日志记录和异常处理。