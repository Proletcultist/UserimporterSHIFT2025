# Инструкция по запуску

## Требования к окружению

- Установленный и запущенный **Docker** и **docker-compose**
- Установленная **Java 17**

## Установка Docker

### Windows

- Требуется установленный **Docker Desktop**:  
  [Скачать Docker Desktop для Windows](https://www.docker.com/products/docker-desktop/)

- Во время установки Docker Desktop потребуется включить поддержку WSL 2. Следуйте инструкциям установщика.

### macOS

- Установите Docker Desktop:  
  [Скачать Docker Desktop для Mac](https://www.docker.com/products/docker-desktop/)

- Также можно установить через Homebrew:

  ```bash
  brew install --cask docker
  ```

### Linux

- Установите Docker и docker-compose через пакетный менеджер (пример для Ubuntu/Debian):

  ```bash
  sudo apt update
  sudo apt install docker.io docker-compose -y
  sudo systemctl start docker
  sudo systemctl enable docker
  ```

## Запуск контейнеров

В корне проекта находится файл docker-compose.yml.

Для запуска всех сервисов выполните команду:

```
  docker-compose up -d
```

После запуска будут доступны:

- PostgreSQL — порт 5432
- PgAdmin — порт 5050

## Доступ к сервисам

### PgAdmin

- URL: http://localhost:5050
- Логин: admin
- Пароль: admin

### PostgreSQL

- Хост: localhost
- Порт: 5432
- База данных: mydb
- Пользователь: admin
- Пароль: admin

## Важная информация

Конфликт портов с локальной PostgreSQL
Если у вас уже установлена локальная версия PostgreSQL, которая использует порт 5432, необходимо изменить внешний порт контейнера.

Для этого откройте файл docker-compose.yml и измените строку:

```
ports:
  - "5432:5432"
```

например, на:

```
ports:
  - "5433:5432"
```

Теперь контейнер будет доступен по порту 5433.

## Остановка контейнеров

Для остановки всех сервисов выполните:

```
docker-compose down
```

## Хранение данных

Контейнеры используют постоянные тома:

- ./var/postgres_data — данные PostgreSQL
- ./var/pgadmin_data — данные PgAdmin

Данные сохраняются между перезапусками контейнеров.