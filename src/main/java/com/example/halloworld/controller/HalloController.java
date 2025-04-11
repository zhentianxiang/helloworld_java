package com.example.halloworld.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;

@RestController
public class HalloController {
	private static final Logger logger = LoggerFactory.getLogger(HalloController.class);
	private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");

	private final Random random = new Random();
	private ExecutorService executorService;

	@Value("${log.simulate.enabled:true}")
	private boolean logSimulateEnabled;

	@Value("${log.normal.interval.min:2}")
	private int normalLogMinInterval;

	@Value("${log.normal.interval.max:5}")
	private int normalLogMaxInterval;

	@Value("${log.error.interval.min:5}")
	private int errorLogMinInterval;

	@Value("${log.error.interval.max:15}")
	private int errorLogMaxInterval;

	private static final String[] RANDOM_MESSAGES = {
			"系统运行正常",
			"检测到新版本可用",
			"内存使用率: 45%",
			"CPU负载: 中等",
			"数据库连接池活跃数: 12",
			"正在处理用户请求",
			"缓存命中率: 78%",
			"执行定时任务中",
			"垃圾回收已完成",
			"网络延迟: 23ms"
	};

	private static final String[] ERROR_MESSAGES = {
			"数据库连接失败",
			"文件系统空间不足",
			"外部API调用超时",
			"无效的用户输入",
			"身份验证失败",
			"空指针异常",
			"数据格式错误",
			"资源未找到",
			"权限不足",
			"并发修改异常"
	};

	@GetMapping("/sip")
	public String getServerInfo() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			return String.format("{\"ip\":\"%s\",\"hostname\":\"%s\"}",
					addr.getHostAddress(),
					addr.getHostName());
		} catch (Exception e) {
			return "{\"error\":\"无法获取主机信息\"}";
		}
	}

	@GetMapping("/")
	public String helloWorld() {
		return "hello world --Version 1.0.0";
	}

	@GetMapping("/health")
	public HealthInfo healthCheck() {
		MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();

		return new HealthInfo(
				"running",
				Runtime.getRuntime().availableProcessors(),
				osBean.getSystemLoadAverage(),
				memoryBean.getHeapMemoryUsage().getUsed() / (1024 * 1024),
				memoryBean.getHeapMemoryUsage().getMax() / (1024 * 1024),
				System.currentTimeMillis()
		);
	}

	@PostConstruct
	public void init() {
		executorService = Executors.newFixedThreadPool(2);
		if (logSimulateEnabled) {
			executorService.submit(new LogWorker());
			executorService.submit(new ErrorLogWorker());
		}
		logger.info("日志模拟器已启动，正常日志间隔: {}-{}秒，错误日志间隔: {}-{}秒",
				normalLogMinInterval, normalLogMaxInterval,
				errorLogMinInterval, errorLogMaxInterval);
	}

	@PreDestroy
	public void cleanup() {
		if (executorService != null) {
			executorService.shutdownNow();
			try {
				if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
					logger.warn("日志模拟器未正常关闭，强制终止");
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		logger.info("日志模拟器已关闭");
	}

	class LogWorker implements Runnable {
		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					int delay = getRandomInterval(normalLogMinInterval, normalLogMaxInterval);
					TimeUnit.SECONDS.sleep(delay);

					String message = RANDOM_MESSAGES[random.nextInt(RANDOM_MESSAGES.length)];
					if (random.nextDouble() < 0.7) {
						logger.info("[{}] {}", Thread.currentThread().getName(), message);
					} else {
						logger.warn("[{}] {}", Thread.currentThread().getName(), message);
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					logger.debug("LogWorker被中断");
				}
			}
		}
	}

	class ErrorLogWorker implements Runnable {
		@Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
					int delay = getRandomInterval(errorLogMinInterval, errorLogMaxInterval);
					TimeUnit.SECONDS.sleep(delay);

					String errorMessage = ERROR_MESSAGES[random.nextInt(ERROR_MESSAGES.length)];
					if (random.nextDouble() < 0.3) {
						logger.error("[{}] {} - 模拟错误",
								Thread.currentThread().getName(),
								errorMessage,
								new Exception("模拟错误堆栈"));
					} else {
						logger.warn("[{}] {}",
								Thread.currentThread().getName(),
								errorMessage);
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					logger.debug("ErrorLogWorker被中断");
				}
			}
		}
	}

	private int getRandomInterval(int min, int max) {
		return min + random.nextInt(max - min + 1);
	}


	public static class HealthInfo {
		private String status;
		private int availableProcessors;
		private double systemLoad;
		private long usedMemoryMB;
		private long maxMemoryMB;
		private long timestamp;

		public HealthInfo(String status, int availableProcessors, double systemLoad,
						  long usedMemoryMB, long maxMemoryMB, long timestamp) {
			this.status = status;
			this.availableProcessors = availableProcessors;
			this.systemLoad = systemLoad;
			this.usedMemoryMB = usedMemoryMB;
			this.maxMemoryMB = maxMemoryMB;
			this.timestamp = timestamp;
		}

		// Getters
		public String getStatus() { return status; }
		public int getAvailableProcessors() { return availableProcessors; }
		public double getSystemLoad() { return systemLoad; }
		public long getUsedMemoryMB() { return usedMemoryMB; }
		public long getMaxMemoryMB() { return maxMemoryMB; }
		public long getTimestamp() { return timestamp; }
	}
}