# udp-sender

## 简介
udp-sender发送小工具,可以读取本地文件，然后以指定的速率向目的udp server发送指定量的日志。

应用于一下场景：
- udp server压测
- udp丢包测试
- 更多

## 使用说明
```bash
 java -jar udp-sender-1.0-SNAPSHOT-jar-with-dependencies.jar                                             
The following options are required: [-f], [-p]
Usage: java -jar  udp-sender-1.0-SNAPSHOT-jar-with-dependencies.jar [options]
  Options:
    -h
      logServerHost
      Default: 127.0.0.1
  * -p
      logServerPort
      Default: 0
    -r
      permitsPerSecond
      Default: 100
    -m
      maxSize
      Default: 10000
  * -f
      filePath
    --version
      UDP Sender 1.0
    --noLogMode
      noLogMode
      Default: false
    --help

```

