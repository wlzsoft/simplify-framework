这个人模块可以剥离出三块东西
1.AopClassFileTransformer和AopAgentMain 独立成一个模块，在使用javaagent使用修改字节码==>>已测试，已通过[已经剥离到weaving模块中] 2016/3/1
2.拦截器相关的模块可以剥离出来==>>已测试，已通过 [保留在当前模块中 aop模块] 2016/3/1
3.具体的实现，比如cache和log或sql可以独立自己的模块中，比如cache，log中，dao中==>>已测试，已通过 2016/3/1