##swagger
- swagger链接:http://121.48.163.57:18080/FaceRecognition/swagger/#/

##服务器相关配置
服务器：121.48.163.57
端口：40404
用户名：cv
密码：cvgo2urlife

服务器tomact：/usr/local/tomact/apache-tomcat-9.0.40/

war包暂存位置：
/home/cv/FaceRecognition/

war包复制到tomact命令：(/home/cv/FaceRecognition/目录下使用)
cp FaceRecognition.war /usr/local/tomact/apache-tomcat-9.0.40/webapps/


##MongoDB配置
Mongo配置文件位置：/etc/mongodb.conf
启动命令：mongod -f mongodb.conf(以配置文件方式启动)
mongo用户：
db.createUser({user:"root",pwd:"password",roles:["root"]})

db.createUser(  
  {  
    user: "admin",  
    pwd: "password",  
    roles: [{role: "userAdminAnyDatabase", db: "admin"}]  
  }  

use admin
db.auth("admin","password");
use FaceRecognition
db.createUser({
    user: "admin",
    pwd: "cilab@uestc",
    roles: [{role: "readWrite",db: " FaceRecognition
})


