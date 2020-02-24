# 秒杀系统
## 项目简介
若鱼老师的《Java秒杀系统方案优化 高性能高并发实战》项目：https://coding.imooc.com/class/168.html
## 快速开始
### 1. 首先你要有一台服务器
### 2. 在服务器上安装redis
1.下载redis上传到服务器  

可以到官网下载：https://redis.io/download  

也可以到我百度网盘下载：
>链接：https://pan.baidu.com/s/1gUbvKyVXg0ayTqDFm4Gfjg  
提取码：kysx  

2.解压缩
```
tar -zvxf 文件名
```
3.修改配置文件

src文件夹下有一个文件redis.conf，修改它：  
- 允许外部服务器连接：bind 127.0.0.1改为bind 0.0.0.0
- 允许后台执行：daemonize no改为daemonize yes

4.启动，指定配置文件
```
redis-server ./redis.conf
```

### 3. 在服务器上安装rabbitmq
**安装erlang**  
1.下载源文件包，上传  
2.安装一个依赖：yum install ncurses-devel  
3.解压源文件包：tar xf otp_src_21.0.tar.gz  
4.切到解压完的目录里去：cd otp_src_21.0  
5. 安装到/usr/local/erlang21目录下：./configure --prefix=/usr/local/erlang21 --enable-hipe --enable-threads --enable-smp-support --enable-kernel-poll --without-javac  
6. make  
7. make install  

**安装RabbitMQ**  
1.安装依赖python：yum install python -y  
2.安装依赖xmlto：yum install xmlto -y  
3.安装依赖simplejson：yum install python-simplejson -y  
4.解压rabbitmq文件，一开始是个.xz文件：xz -d 文件名  
5.解压，回来是个.tar文件：tar xf 文件名  
6.启动rabbitmq：在文件夹的sbin文件夹内启动rabbitmq-server：./rabbitmq-server  
8.关闭rabbitmq：在文件夹的sbin文件夹内：./rabbitmqctl stop  
### 4. 运行
下载后导入IDEA，修改配置文件，运行。
