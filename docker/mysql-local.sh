docker run -d \
--name mysql-withme \
-e MYSQL_ROOT_PASSWORD="rootpassword" \
-e MYSQL_USER="withus" \
-e MYSQL_PASSWORD="withuspassword" \
-e MYSQL_DATABASE="withme" \
-p 3310:3310 \
mysql:latest