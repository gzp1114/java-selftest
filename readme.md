jvm	jvm	
		类初始化流程
		jvm内存结构
		jvm内存模型
		jvm垃圾回收算法
		jvm垃圾回收器
		cms和g1区别
		日常调试参数
		讲解一下平时项目中调优
java基础	集合	HashMap
		HashTable
		CurrentHashMap（底层实现、线程安全原理）
		多线程的Queue 有界无界
	java io aio nio面试问题	
	多线程	
		创建线程
		线程池（各个参数意义）
		线程池返回值
		线程池顺序执行
		线程池同时执行
		synchinic和volitile
		可重入锁
		原子性
		锁升级
		ThreadLocal用法原理，内存泄漏
		countdownlatch和信号量Semaphore 使用和区别
		synchronized方法时，static加不加有啥区别
		sleep和wait区别
		
框架	spring面试题关键点整理	
		controller是否单例，为什么
		ioc怎么回事，原理
		aop
		过滤器，拦截器，区别 以及aop用法区别
		spring aop 前置后置环绕
	myibatis面试关键点整理	
		原理
		执行流程
		一级缓存二级缓存
		myibatis优点
		关键标签 $和#的区别
数据库		
	mysql	
		搜索引擎级别
		B+tree优势
		索引结构
		聚簇索引，非聚簇索引，覆盖索引
		sql优化 explain
		sql规范
		mysql死锁
		建立索引的选择（场景适合和不适合）
	事务	
		脏读幻读重复读
		事务隔离级别
		spring如何管理事务（代理 同一个类a方法调用b方法事务不生效）
中间件		
	mq面试问题整理	
		消息重复，消息丢失，消息顺序
		mq选型，为啥选择这个或者优缺点对比
	redis	
		基本数据类型和应用场景
		分布式锁，设置过期时间的问题，以及原子性操作（lua）
		缓存穿透，击穿，雪崩是什么，怎么处理
		集群哨兵
		数据持久化
		键过期策略
		内存淘汰策略
		高级用法
		
微服务		
	dubbo源码	
		结构流程图
		关键策略
	dubbo面试整理	
		spi
		lru
		负载均衡策略
		容错策略
		支持的协议
	zookeeper源码阅读	
		源码结构图整理
		源码关键策略和技术点
		源码关键类
	zookeeper面试	
		源码关键原理
		选举机制
		cap（与eruker区别）
		ZAB协议
		分布式锁实现
	springboot springcloud	
		springboot和springcloud区别
		springboot启动过程以及常用注解
		eruker feign congfig gatway robin hitrix 等了解和使用
		springboot项目启动初始化方法
网络		
	http https tcp区别	
		http三次握手四次挥手
		tcp/ip协议
		七层结构，tcp在哪层，http在哪层
		http和https区别，https怎么做到安全的
项目问题		
		怎么进行架构选型
		限流、熔断等的处理






###设计模式练习 org.selftest.interview.design
demo实例：创建订单流程
观察者模式：订单创建成功发送通知 org.selftest.interview.design.Observer
模板模式：创建影票、卖品订单区分 org.selftest.interview.design.template
策略模式：计算订单金额 org.selftest.interview.design.strategy


###分布式锁实现 org.selftest.interview.distributedlock
RedisClient：jedis实现
RedissonLock：Redisson实现
RedisTemplateLock：jedis的spring实现
ZookeeperLock：zookeeper实现

###锁实现


###算法练习 org.selftest.interview.algorithm
link-实现
    1.单链表反转
    2.链表中环的检测
    3.两个有序的链表合并
    4.删除链表倒数第 n 个结点
    5.求链表的中间结点
===================

###zookeeper练习


###多线程练习
