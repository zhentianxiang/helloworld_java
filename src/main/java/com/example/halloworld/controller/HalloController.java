package com.example.halloworld.controller;
import java.util.Random;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class HalloController {
	private static final Logger logger = LoggerFactory.getLogger(HalloController.class);

	@GetMapping("/sip")
	@ResponseBody
	public String test(@RequestParam(value = "req", required = false) String req) {
        InetAddress addr=null;  
        String ip="";  
        String hostname="";  
        try{  
            addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress().toString(); //获得IP地址　　  
            hostname= addr.getHostName().toString(); //获得主机名
            //logger.info("ip:"+ip+"-"+"hostname:"+hostname);
        }catch(Exception e){  
            e.printStackTrace();  
        }  
		return "ip:"+ip +",hostname:"+hostname;
	}
	
    @GetMapping("/")
	@ResponseBody
	public String helloworld(){
		//logger.info("");
		return "hello world --Version 1";
	}


	@PostConstruct
	public void log() {
		new Thread(new Work()).start();
	}
	
	class Work implements Runnable{
		private  Random r = new Random(1);
		@Override
		public void run() {
			while(true) {
				try {
					Thread.sleep(2000);
					logger.info("打印随机数: "+r.nextInt(1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
}
