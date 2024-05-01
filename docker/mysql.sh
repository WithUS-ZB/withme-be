docker run -d \
--name mysql-withme \
-e MYSQL_ROOT_PASSWORD="rootpassword" \
-e MYSQL_USER="withus" \
-e MYSQL_PASSWORD="withuspassword" \
-e MYSQL_DATABASE="withme" \
-e TZ="Asia/Seoul"
-p 3306:3306 \
mysql:latest