# 1.项目基本概述


## 1.1 完成情况

```
本项目完成了需求文档中的所有基本内容，并完成了附加项目中的（项目说明文档 + 好友用户实时聊天 + 云部署）共30分的内容.
```

## 1.2 整体架构

```
本项目采用了Vue + JavaEE的前后端分离式开发。前端主要负责用户逻辑、UI、发送接收请求，实现用户的注册、登录，图片的收藏、上传、删除，同时采用了WebSocket技术实现了好友用户实时聊天；后端主要负责数据库逻辑，采用MVC设计模式，由具体业务类和响应的数据访问对象（DAO）来完成对数据库的操作工作，再由服务层负责处理前端发送的请求并作出正确的响应。
```

## 1.3 数据库

```
本项目在数据库的处理上采用了6个数据表：User、City、Country、Image、Friend、Favor
User：字段（UID、Email、UserName、Pass、SignUpDate）
City：字段（CityCode、Name、CountryCode）
Country：字段（CountryCode、Name）
Image：字段（ImageID、Title、Description、CityCode、CountryCode、UID、PATH、Content、Date、FavorNumber）
Friend：字段（FriendID、UID1、UID2、State）
Favor：字段（FavorID、UID、ImageID）
```
```
具体组织结构：其中类之间的关系依赖于数据库之间相同的字段
+-----------------+
| Site Components |
+---------+-------+
          |
    +-----+----+
    |          |
+---+---+ +----+---+
| Favor | | Friend |
+---+---+ +----+---+
    |          |
+---+          +---+
|                  |
+-------------+    |
|             |    |
+-------+ +---+--+ |
| Image | | User +-+
+---+---+ +------+
    |
    +---------+
    |         |
+---+--+ +----+----+
| City | | Country |
+------+ +---------+
```


## 1.4 单元测试

```
本项目有着较为完备的单元测试。前端有Vue自带的测试类，后端在src/test下有测试DAO运行而编写的测试用例，通过运行测试用例可以一定程度地检验代码的正确性
```

# 2.后端接口API

## 2.0 接口处的对象说明

```
class Image：

int imageID;
String title;
String description;
String city;
int uid;
String path;
String content;
String date;
int favorNumber;
```

```
class User:

int uid;
String email;
String username;
String password;
String signUpDate;
```

## 2.1 注册

```
/Register
method: POST
Params:
    username: String
    email: String
    password: String
# 表单内容完整、密码和确认密码、邮箱格式的检查在前端进行
response:
    String "success"/"error message"
```

## 2.2 登录
```
/Login
method: POST
Params:
    username: String
    password: String
# 先检查用户名是否存在，再检查密码是否在正确
response：
    String "success"/"error massage"
```
```
/GetIdByUsername
method: GET
Params:
    username: String
reponse:
    int uid
```

## 2.3 首页

```
/GetHottestImg
method: GET
URL Params：
    count: int
# 返回数据库中喜欢总数最多的n张图片
response:
    Image[]
```

```
/GetLatestImg
method: GET
URL Params：
    count: int
# 返回数据库中上传时间最晚的的n张图片
response:
    Image[]
```

## 2.4 详情页
```
/GetImgByImageID
method: GET
URL Params:
    id: int
# id为图片id
response:
    Image
```

```
/GetUsernameById
method: GET
URL Params:
    id: int
# id为用户id
reponse:
    String "username"
```

```
/CheckFavor
method: GET
URL Params:
    uid: int
    imageID: int
# 检查当前用户是否收藏了这张图片
reponse:
    String "true"/"false"

/AddFavor
method: GET
URL Params:
    uid: int
    imageID: int
reponse:
    String "success"/"error: message"

/DeleteFavor
method: GET
URL Params:
    uid: int
    imageID: int
reponse:
    String "success"/"error: message"
```

## 2.5 搜索页

```
/SearchByTitle
method: GET
URL Params:
    keyword: String
    order: "favor"/"date"
# 以标题为关键字进行搜索
response:
    Image[]
```
```
/SearchByDescription
method: GET
URL Params:
    keyword: String
    order: "favor"/"date"
# 以描述为关键字进行搜索
response:
    Image[]
```

## 2.6 上传页

```
GetCountry
method: GET
# 获取所有国家的名字
response:
    String[]
```

```
GetCity
method: GET
URL Params:
    country: String
# 获取一个国家的所有城市
response:
    String[]
```

```
/CreateImg
method: POST
Params:
    uid: int
    title: String
    description: String
    country: String
    city: String
    content: String
    img: file
    imageID: int #新创建的图片则不设置此字段
response:
    String "success"/"error message"
```

## 2.7 我的照片
```
/GetPhotoByUid
Params:
    uid: int
response:
    Image[]
```
```
/DeleteImg
method: GET
URL Params:
    imageID: int
response:
    String "success"/"error message"
```

## 2.8 查看收藏

```
/GetFavorImg
Params:
    uid1: int
    uid2: int
# 查看自己的收藏时设置uid1=uid2
# 查看好友的收藏时设置uid1为自己的id，uid2为好友的id
response:
    Image[] / String "denied“
```

## 2.9 好友列表

```
/GetFriendList
Params:
    state: String
    uid: int
# 返回特定好友状态的用户列表
# unlimited 无限制好友
# limited   限制ta访问我的好友
# inviting  我正在邀请的用户
# invited   正在邀请我的用户
response:
    User[]
```
```
/RequestExecute
Params:
    action: String
    uid1: int
    uid2: int
# uid1为我的id，uid2为对象id，action为动作
# send      发送请求
# accept    接受请求
# reject    拒绝请求
reponse:
    String "success"/"error message"
```
```
/FriendExecute
Params:
       action: String
    uid1: int
    uid2: int
# uid1为我的id，uid2为对象id，action为动作
# send      发送请求
# accept    接受请求
# reject    拒绝请求
reponse:
    String "success"/"error message"
```

# 3.项目部署说明

## 3.1 前端

```
文件目录：/usr/local/nginx/html/whz-web-pj2/dist/
url：http://114.115.151.236:8001
配置文件：前端根目录/src/assets/config.js
步骤：安装nginx -> vue项目打包 npm run build，上传dist文件夹 -> 修改nginx配置文件增加节点 -> 修改前端配置文件 -> 重启nginx
```

## 3.2 后端

```
文件目录：/var/www/html/whz-web-pj2/backend/
url：http://114.115.151.236:8002/whz-web-pj2/backend/
配置文件：后端根目录/src/config/jdbc.properties
步骤：安装tomcat以及mysql扩展 -> 查看配置文件找到根目录 -> 上传文件夹到根目录 -> 修改后端配置文件 -> 重启tomcat
```

## 3.3 数据库

```
导入文件：travel.sql
配置文件：后端根目录/src/config/jdbc.properties
步骤：创建travel数据库，导入后更新配置文件mysql用户信息
```

## 3.4 图片存放目录

```
url：http://114.115.151.236:8002/image/
```

## 3.5 聊天室Web Socket地址

```
url：http://114.115.151.236:8003
```