# Java课设————思维导图
Java面向对象程序设计课设，基于swing库的思维导图编辑工具

### 项目结构

> ### core包：核心包
> 
> > MindMap类：为思维导图主要操作的控制类  
> > Node类：为思维导图节点的控制类
> 
> ---
> 
> ### ui包
> 
> > Display类：展示思维导图类，后期替换成Swing展示页面  
> > Main类：初始化类  
> > Control类：为目前页面的输入操作控制类，后期替换  
> > &#8195;&#8195;Init方法：为登录注册操作  
> > &#8195;&#8195;startUserInterface方法：为用户操作思维导图操作
> 
> ---
> 
> ### user包：用户信息相关包
> 
> > nowUser类：当前用户信息，存储用户的username和userid信息  
> > userWork类：用户登陆注册的JDBC操作
> 
> ---
> 
> ### util包：工具包
> 
> > connectDB类：连接数据库类  
> > #
> > #

### 接口设置

| 包   | 类   | 方法  | 作用  | 参数  | 返回  |
| --- | --- | --- | --- | --- | --- |
| user | userWord | login | 用户登录 | username，password，nowUser | true：登录成功，false：登录失败 |
| user | userWord | register | 用户注册 | username，password | 0：用户名已存在，1：注册失败，2：成功 |
| core | MindMap | addNode | 添加子节点 | parentId，newData | void |
| core | MindMap | removeNode | 移除节点 | nodeId | void |
| core | MindMap | changeNodeData | 修改节点内容 | nodeId,newData | void |
| core | MapJDBC | changeTitle | 修改思维导图标题 | mindMap，newTitle | void |
| core | MapJDBC | getMaps | 获取单一用户的maps | nowUser | Map<String,Integer> |
| core | MapJDBC | loadMap | 从数据库中读取单个思维导图 | title，mapId | MindMap |
| core | MapJDBC | save | 保存思维导图 | mindMap，nowUser | -1：必须先修改思维导图标题，0：未知原因保存错误，1：保存成功 |
