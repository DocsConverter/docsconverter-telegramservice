## Docs Converter convert Image, Docs, Photo to different format

### It's deployed to [Heroku](https://docsconverter-telegramservice)

Use modular architecture:
1. docsconverter-telegramservice receive task and send as message json on RabbitMQ queue.
2. docsconverter-converterservice receive message, download file and convert it. Then send result message with output link on server.
3. docsconverter-telegramservice download file to telegram from docsconverter-converterservice by http. And displayed result to user.

Possible any count of individual modules in order to increase productivity and fault tolerance.

Project stack:
- Spring Boot 2
- RabbitMQ
- Jackson
- Commons IO
- [Telegram Bot Library for Spring Boot](https://github.com/xabgesagtx/telegram-spring-boot-starter)

[Document icon by Icons8](https://icons8.com/icons/set/document)