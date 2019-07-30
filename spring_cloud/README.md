## Требования
* java 1.8+
* gradle - для сборки

## Сборка:
* gradle clean
* gradle build -x test (-x test) - сборка без запуска тестов

## Docker
* docker build . --tag=cloud
* docker run -p 8080:8080 -d --name cloud cloud

##### PS: имя конфигурационнго файла не должно содержать точек
##### разделение с помощью знака "-"

## Открыть в idea
* Импортировать как gradle project
* Edit Run/Debug configuration
* в **application.properties** в поле **file.upload.dir** указать путь для хранения изображений

## Загрузка файла на сервер
* host:port/upload - POST запрос form-data
#### параметры: 
* имя парметра - путь к файлу, знчаение - файл
* keep_name сохранить оригинальное имя (true, false) необязательно, по умолчанию false
* ответ json:
```json
[
    {
        "name": "1563033255151.jpg",
        "uri": "http://localhost:8080/load/verification/passport/2019/6/14/1563033255151.jpg",
        "fileType": "image/jpeg"
    },
    {
        "name": "1563033255152.jpg",
        "uri": "http://localhost:8080/load/user/avatar/2019/6/14/1563033255152.jpg",
        "fileType": "image/jpeg"
    }
]
```
* name - имя файла
* uri - uri к файлу
* fileType - тип файла
