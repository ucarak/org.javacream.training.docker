docker create --name mysql -e MYSQL_ROOT_PASSWORD=javacream -v mysql_data:/var/lib/mysql -p 4306:3306 mysql:5.7.23
