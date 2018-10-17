一.JAVA编译器								
	eclipse		https://www.eclipse.org/downloads/					
	IDEA Ultimate		https://www.jetbrains.com/idea/download/#section=windows					
	IDEA破解		https://blog.csdn.net/weixin_42190794/article/details/80475076					
								
二.数据库								
	默认使用服务器数据库，如需本地安装							
	mongodb	https://www.mongodb.com/download-center?jmp=nav#enterprise						
	可视化工具							
		自带compass		随软件安装				
		nosqlbooster		https://nosqlbooster.com/downloads				
		nosqlbooster破解方法						
			1.删除注册表HKEY_CURRENT_USER\Software\NoSQL Manager Group					
			2.删除文件夹X:\ProgramData\NoSQL Manager Group					
								
	默认使用服务器数据库，如需本地安装							
	redis		https://redis.io/download					
	可视化工具							
		RedisDesktopManager		https://redisdesktop.com/download				
								
三，其他软件								
	jdk10 		https://www.oracle.com/technetwork/java/javase/downloads/jdk10-downloads-4416644.html					
	若使用eclipse，可选安SPRING插件，STS。			http://spring.io/tools/sts				
	GIT		https://git-scm.com/download/win					
	GIT可视化工具torisegit		https://tortoisegit.org/					
	tortoisesvn		https://tortoisesvn.net/downloads.html					


代码简介：
    本工程使用springboot作为基础框架，使用自带@ControllerAdvice注解，处理接口全局异常，使用aop切面，实现接口访问日志，数据库使用mongodb.