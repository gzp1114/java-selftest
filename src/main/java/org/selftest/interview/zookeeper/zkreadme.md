###Zookeeper数据模型
ZNode结构：stat（版本、权限等信息），data-关联数据，children-子节点
数据访问：原子性操作，每个节点acl（访问控制列表）
节点类型：临时+永久
顺序节点：路径结尾递增计数，格式为"%10d"(10位数字，没有数值的数位用0补充，
例如"0000000001")，最大2的32次幂-1，超过计数器溢出
观察：watch机制，被触发时，仅能发送一次通知，因此watch只能被触发一次

###Zookeeper中的时间
每个对节点的改变都将产生一个唯一的Zxid（针对于事务操作）
ZooKeeper的每个节点维护者三个Zxid值，为别为：cZxid、mZxid、pZxid
Zxid为64位，低32位为递增计数
版本号：
① version：节点数据版本号
② cversion：子节点版本号
③ aversion：节点所拥有的ACL版本号

### zookeeper服务中操作
9中基础操作（create、delete、exists、setData/getData、setACL/getACL、getChildren、sync）
更新ZooKeeper操作是非阻塞式的

### 监听机制
watch触发器：
可以为所有读操作设置触发器，此为一次性的
数据类型：数据watch、孩子watch
① 一个成功的setData操作将触发Znode的数据watch
② 一个成功的create操作将触发Znode的数据watch以及孩子watch
③ 一个成功的delete操作将触发Znode的数据watch以及孩子watch

① exists操作上的watch，在被监视的Znode创建、删除或数据更新时被触发。
② getData操作上的watch，在被监视的Znode删除或数据更新时被触发。在被创建时不能被触发，因为只有Znode一定存在，getData操作才会成功。
③ getChildren操作上的watch，在被监视的Znode的子节点创建或删除，或是这个Znode自身被删除时被触发。可以通过查看watch事件类型来区分是Znode，
还是他的子节点被删除：NodeDelete表示Znode被删除，NodeDeletedChanged表示子节点被删除。

监听工作原理：
ZooKeeper 的 Watcher 机制主要包括客户端线程、客户端 WatcherManager、Zookeeper 服务器三部分。客户端在向 ZooKeeper 服务器注册的同时，
会将 Watcher 对象存储在客户端的 WatcherManager 当中。当 ZooKeeper 服务器触发 Watcher 事件后，会向客户端发送通知， 客户端线程从 WatcherManager 中取出对应的 Watcher 对象来执行回调逻辑

### ZooKeeper 特点/设计目的
数据复制和同步：
好处：容错、扩展性、高性能
设计目的：
1、最终一致性：client不论连接到哪个Server，展示给它都是同一个视图，这是zookeeper最重要的性能。 
2、可靠性：具有简单、健壮、良好的性能，如果消息被到一台服务器接受，那么它将被所有的服务器接受。 
3、实时性：Zookeeper保证客户端将在一个时间间隔范围内获得服务器的更新信息，或者服务器失效的信息。但由于网络延时等原因，Zookeeper不能保证两个客户端能同时得到刚更新的数据，如果需要最新数据，应该在读数据之前调用sync()接口。 
4、等待无关（wait-free）：慢的或者失效的client不得干预快速的client的请求，使得每个client都能有效的等待。 
5、原子性：更新只能成功或者失败，没有中间状态。 
6、顺序性：包括全局有序和偏序两种：全局有序是指如果在一台服务器上消息a在消息b前发布，则在所有Server上消息a都将在消息b前被发布；偏序是指如果一个消息b在消息a后被同一个发送者发布，a必将排在b前面。

### ZooKeeper 典型应用场景
命名服务:
配置管理：项目中阿里sentinel使用zookeeper配置管理规则
分布式锁：项目中使用
队列管理


### Zookeeper节点数据操作流程
1.在Client向Follwer发出一个写的请求
　　2.Follwer把请求发送给Leader
　　3.Leader接收到以后开始发起投票并通知Follwer进行投票
　　4.Follwer把投票结果发送给Leader
　　5.Leader将结果汇总后如果需要写入，则开始写入同时把写入操作通知给Leader，然后commit;
　　6.Follwer把请求结果返回给Client
 Follower主要有四个功能：
　　1. 向Leader发送请求（PING消息、REQUEST消息、ACK消息、REVALIDATE消息）；
　　2 .接收Leader消息并进行处理；
　　3 .接收Client的请求，如果为写请求，发送给Leader进行投票；
　　4 .返回Client结果。

### ZAB 协议（Paxos算法）
分布式系统节点通信模型：共享内存和消息传递    
Paxos 算法解决的问题是在一个可能发生上述异常的分 布式系统中如何就某个值达成一致，保证不论发生以上任何异常，都不会破坏决议一致性。
ZAB 协议两种模式：崩溃恢复模式和原子广播模式

1、选举线程由当前 Server 发起选举的线程担任，其主要功能是对投票结果进行统计，并选 出推荐的 Server

2、选举线程首先向所有 Server 发起一次询问(包括自己)

3、选举线程收到回复后，验证是否是自己发起的询问(验证 zxid 是否一致)，然后获取对方 的 serverid(myid)，并存储到当前询问对象列表中，最后获取对方提议的 leader 相关信息 (serverid,zxid)，并将这些信息存储到当次选举的投票记录表中

4、收到所有 Server 回复以后，就计算出 id 最大的那个 Server，并将这个 Server 相关信息设 置成下一次要投票的 Server

5、线程将当前 id 最大的 Server 设置为当前 Server 要推荐的 Leader，如果此时获胜的 Server 获得 n/2 + 1 的 Server 票数， 设置当前推荐的 leader 为获胜的 Server，将根据获胜的 Server 相关信息设置自己的状态，否则，继续这个过程，直到 leader 被选举出来。


### ZooKeeper 的全新集群选主
以一个简单的例子来说明整个选举的过程：假设有五台服务器组成的 zookeeper 集群，它们 的 serverid 从 1-5，同时它们都是最新启动的，也就是没有历史数据，在存放数据量这一点 上，都是一样的。假设这些服务器依序启动，来看看会发生什么

　　1、服务器 1 启动，此时只有它一台服务器启动了，它发出去的报没有任何响应，所以它的 选举状态一直是 LOOKING 状态

　　2、服务器 2 启动，它与最开始启动的服务器 1 进行通信，互相交换自己的选举结果，由于 两者都没有历史数据，所以 id 值较大的服务器 2 胜出，但是由于没有达到超过半数以上的服务器都同意选举它(这个例子中的半数以上是 3)，所以服务器 1、2 还是继续保持 LOOKING 状态

 　   3、服务器 3 启动，根据前面的理论分析，服务器 3 成为服务器 1,2,3 中的老大，而与上面不 同的是，此时有三台服务器(超过半数)选举了它，所以它成为了这次选举的 leader

　　4、服务器 4 启动，根据前面的分析，理论上服务器 4 应该是服务器 1,2,3,4 中最大的，但是 由于前面已经有半数以上的服务器选举了服务器 3，所以它只能接收当小弟的命了

　　5、服务器 5 启动，同 4 一样，当小弟

### ZooKeeper 的非全新集群选主
需要加入数据 version、serverid 和逻辑时钟。

　　数据 version：数据新的 version 就大，数据每次更新都会更新 version

　　server id：就是我们配置的 myid 中的值，每个机器一个

　　逻辑时钟：这个值从 0 开始递增，每次选举对应一个值，也就是说：如果在同一次选举中， 那么这个值应该是一致的；逻辑时钟值越大，说明这一次选举 leader 的进程更新，也就是 每次选举拥有一个 zxid，投票结果只取 zxid 最新的

　　选举的标准就变成：

　　　　1、逻辑时钟小的选举结果被忽略，重新投票

　　　　2、统一逻辑时钟后，数据 version 大的胜出

　　　　3、数据 version 相同的情况下，server id 大的胜出






