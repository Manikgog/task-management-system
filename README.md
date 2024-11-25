Для того чтобы запустить приложение нужно, чтобы на Вашей машине был уставлен<br> 
Docker Desktop и запустить его. Далее необходимо заполнить файл .env своими<br>
данными:<br>
DATABASE_URL=jdbc:postgresql://tasks-db:5432/taskdb<br>
DATABASE_PASSWORD=password<br>
DATABASE_USERNAME=username<br>
POSTGRES_DB=taskdb<br>
POSTGRES_USER=username<br>
POSTGRES_PASSWORD=password<br>
POSTGRES_PORT=5432<br>
JWT_SECRET_PHRASE=ovgoemvgoaeogumeaougeaourgmougmepau<br>
CORS_ALLOWED_ORIGINS=none<br>
CORS_ALLOWED_METHODS=none<br>
APPLICATION_PORT=8081<br>

Затем необходимо выполнить команду maven clean install для сборки исполняемого 
файла.<br>

После этого можно запускать файл docker-compose.yml открыв консоль в корневой
папке проекта.<br>

Теперь можно открыть браузер и ввести в адресную строку:<br>
http://localhost:8081/swagger-ui/index.html#<br>

