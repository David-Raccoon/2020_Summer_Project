# 后端接口API

## 0.对象说明

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

## 1.注册

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

## 2.登录
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

## 3.首页

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

## 4.详情页
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

## 5.搜索页

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
response:
    Image[]
```

## 6.上传页

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

## 7.我的照片
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

## 8.查看收藏

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

## 9.好友列表

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