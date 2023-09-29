# Chat-functionality
Реализация функциональности чата путем использования ключевых механизмов работы с СУБД PostgreSQL через JDBC.

## Introduction
- Для написания программ использовалась версия Java 8.
- Отладка кода воспроизводилась на Intellij IDEA CE.
- Правила форматирования кода соответствуют общепринятым стандартам [Oracle](https://www.oracle.com/java/technologies/javase/codeconventions-namingconventions.html).

## Contents
1. [Exercise 00](#exercise-00)
2. [Exercise 01](#exercise-01)
3. [Exercise 02](#exercise-02)
4. [Exercise 03](#exercise-03)
5. [Exercise 04](#exercise-04)

### Exercise 00

- Программа расположена в директории: ex00;
- Корневая папка проекта: Chat.

В этом проекте реализовывается функциональность чата. В чате пользователь может создать или выбрать существующий чат. В каждом чате может быть несколько пользователей, обменивающихся сообщениями.

Ключевые модели предметной области, для которых были реализованы как таблицы SQL, так и классы Java:

- User
    -	User ID
    - Login
    -	Password
    -	List of created rooms
    -	List of chatrooms where a user socializes
- Chatroom
    -	Chatroom id
    - Chatroom name
    - Chatroom owner
    - List of messages in a chatroom
- Message
    - Message id
    - Message author
    - Message room
    - Message text
    - Message date/time

Создан файл schema.sql, в котором описаны операции DROP SCHEMA, CREATE SCHEMA и CREATE TABLE для удаления схемы, создания схемы и таблиц для проекта. И также создан файл data.sql с запросами INSERT INTO для вставки данных в таблицы.

- Для реализации реляционных ссылок использовался тип ссылки «один-ко-многим».
- Идентификаторы являются числовыми значениями.
- Идентификаторы генерируются СУБД.
- equals(), hashCode() и toString() были переопределены внутри классов Java.

Структура проекта упражнения:

- Chat
    -	src
        -	main
            - java
              -	edu.school21.chat
                 -	models - domain knowledge models
            - resources
                -	schema.sql
                -	data.sql
    -	pom.xml

### Exercise 01

- Программа расположена в директории: ex01;
- Использованная библиотека: HikariCP;
- Корневая папка проекта: Chat.

В этом проекте реализован интерфейс MessagesRepository, содержащий один метод `Optional<Message> findById(Long id)` и его реализация описана в классе MessagesRepositoryJdbcImpl. MessagesRepository написан по шаблону дизайна Data Access Object (DAO, Repository), который отделяет ключевую бизнес-логику от логики обработки данных в приложении.

Этот метод возвращает объект Message, в котором указаны автор и чат. В свою очередь, нет необходимости выводить подсущности (список чатов, создатель чата и т.п.) для автора и чата.

MessagesRepositoryJdbcImpl принимает интерфейс DataSource пакета java.sql в качестве параметра конструктора.

Для реализации DataSource была использована библиотека HikariCP — пул подключений к базе данных.

Код реализован в классе Program.java. Пример работы программы следующий:

```
$ java Program
Enter a message ID
-> 5
Message : {
  id=5,
  author={id=7,login=“user”,password=“user”,createdRooms=null,rooms=null},
  room={id=8,name=“room”,creator=null,messages=null},
  text=“message”,
  dateTime=01/01/01 15:69
}
```

Структура проекта упражнения:
- Chat
  -	src
      -	main
        - java
          -	edu.school21.chat
             - models - domain knowledge models
              -	repositories - repositories
              -	app
                  - Program.java
        - resources
          -	schema.sql
          -	data.sql
  -	pom.xml

### Exercise 02

- Программа расположена в директории: ex02;
- Корневая папка проекта: Chat.

Проект описывает реализацию метода save(Message message) для MessagesRepository.

Были опеределены следующие подсущности для сохраняемой сущности — автора сообщения и чат. Также чату и автору были присвоены идентификаторы, существующие в базе данных.

Таким образом, метод save присваивает значение идентификатора входящей модели после сохранения данных в базе данных.

Если у автора и комнаты нет идентификатора в назначенной базе данных или эти идентификаторы имеют значение NULL, выводится реализованное исключение NotSavedSubEntityException.

Созданный метод save(Message message) тестируется в классе Program.java:

```java
    public static void main(String[] args) throws SQLException {
        ...
        User creator = new User(1L, "user", "user", new ArrayList<>(), new ArrayList<>());
        User author = creator;
        Chatroom room = new Chatroom(1L, "room", creator, new ArrayList<>());
        Message message = new Message(null, author, room, "Hello!", LocalDateTime.now());

        MessagesRepository repository = new MessagesRepositoryJdbcImpl(ds);
        repository.save(message);

        System.out.println(message.getId());
        ...
    }
```

В этом примере в столбце автор будет сохранен идентификатор со значением 1, в столбце комната будет сохранен идентификатор со значением 1, в столбце text будет сохранено "Hello!" и в столбце dateTime будет сохранено текущее время.

### Exercise 03

- Программа расположена в директории: ex03;
- Корневая папка проекта: Chat.

Проект описывает создание метода update(Message message) в MessageRepository. Этот метод полностью обновляет существующий объект в базе данных. Если новое значение поля обновляемого объекта равно NULL, это значение должно быть сохранено в базе данных.

Созданный метод update(Message message) тестируется в классе Program.java:

```java
    public static void main(String[] args) throws SQLException {
        ...
        MessagesRepository messagesRepository = new MessagesRepositoryJdbcImpl(ds);
        Optional<Message> messageOptional = messagesRepository.findById(11L);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            message.setText("Bye");
            message.setDateTime(null);
            messagesRepository.update(message);
        }
        ...
    }
```

В этом примере значение столбца, в котором хранится текст сообщения, будет изменено, а время сообщения будет равно нулю.

### Exercise 04

- Программа расположена в директории: ex04;
- Корневая папка проекта: Chat.

К предыдущим проектам был дополнительно реализован интерфейс UsersRepository и класс UsersRepositoryJdbcImpl, используя метод `List<User> findAll(int page, int size)`.

Этот метод возвращает список пользователей, показанных на странице с указанным номером страницы и ограниченным количеством пользователей в списке. Метод реализован с помощью одного запроса к БД, для этого используется CTE PostgreSQL.

Таким образом, СУБД делит общий набор на страницы, каждая из которых содержит записи о размере. Например, если набор содержит 20 записей со страницей = 3 и размером = 4, вы получаете пользователей с 12 по 15 (нумерация пользователей и страниц начинается с 0).

В этой задаче каждый пользователь в результирующем списке должен включать зависимости — список чатов, созданных этим пользователем, а также список чатов, в которых участвует пользователь.

Не имеет смысла включать зависимости каждой подсущности, т.е. список сообщений внутри каждой комнаты должен быть NULL.

UsersRepositoryJdbcImpl принимает интерфейс DataSource пакета java.sql в качестве параметра конструктора.

Реализованный метод findAll(int page, int size) тестируется в классе Program.java:

```java
    public static void main(String[] args) throws SQLException {
        ...
        UsersRepository usersRepository = new UsersRepositoryJdbcImpl(ds);
        List<User> users = usersRepository.findAll(0, 2);
        for(User user : users) {
            System.out.println(user);
        }
        ...
    }
```

В этом примере будут возвращены 2 пользователя с 0 страницы.