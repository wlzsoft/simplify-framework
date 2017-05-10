Simplfiy框架快速入门
====================

框架简述
---------------------

框架主要用于简化和加快服务端应用开发：适用 “单web应用架构”，“分布式服务架构”，“微服务架构” 等。
从字面意义可以看出，目的就要精简：不过度设计，易于使用，不失功能的情况下，权衡性能和易用性。
框架整体基于组件化，插件化设计，组件和插件粒度尽量的细，并提供灵活的扩展机制。
为了更轻量级，框架在不影响性能的情况下，极度削减第三方库依赖，减少框架体积。

### 开发环境

#####依赖的软件及版本
> jdk版本：1.8.0以上  
> maven版本：3.3.3以上
> gradle版本 2.14以上(可选)
     
######开发工具
> eclipse 或 intellij idea 常规开发建议使用后者
     
### 单web应用开发

注：开发过程基于idea工具完成
> 1.创建基于maven的项目，不依赖于模版

> 2.在pom.xml中增加以下配置：

```xml
  <dependency>
    <groupId>vip.simplify</groupId>
    <artifactId>mvc</artifactId>
    <version>1.2.3-SNAPSHOT</version>
  </dependency>
```
> 3.创建一个Controller类名为TestController,并补充基础代码，如下：
```java
   package vip.simplify.demo;
   import vip.simplify.mvc.model.Model;
   import vip.simplify.ioc.annotation.Bean;
   import javax.servlet.http.HttpServletRequest;
   import javax.servlet.http.HttpServletResponse;
   import vip.simplify.mvc.annotation.RequestMap;
   import vip.simplify.dto.Result;
   import vip.simplify.dto.ResultFactory;
   @Bean
   public class TestController extends BaseController<Model>  {
       @RequestMap
       public Result test(HttpServletRequest request, HttpServletResponse response, Model model) {
            return ResultFactory.success("测试成功!");
       }
   }
```

### 分布式web应用开发

> 依赖dubbo服务，预先安装zookeeper用于注册 和 dubbo-admin用于监控管理，dubbo-monitor可选安装

#### 单web应用改造开发

#### rpc客户端api库开发

#### rpc服务端应用开发

以上例子工程下载地址：

正式开发可以基于相应例子工程修改来快速搭建初始项目。

