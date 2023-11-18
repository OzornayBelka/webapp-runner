package ru.netology.controller;

import com.google.gson.Gson;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;

//прием запросов и подготовка ответов
public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;//переменная для записи данных сервиса

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);//в ответе устанавливаем тип файла (заголовок)
        final var data = service.all();//получаем все даннеы из репозитория обработанные сервисом
        final var gson = new Gson();//создаем обьект
        response.getWriter().print(gson.toJson(data));
        //записываем данные в json и записываем в тело запроса файл
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        // TODO: deserialize request & serialize response
        response.setContentType(APPLICATION_JSON);
        final var data = service.getById(id);
        final var gson = new Gson();
        response.getWriter().print(gson.toJson(data));
    }

    //готовим ответ на запрос сохранить пост
    public void save(Reader body, HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON);
        final var gson = new Gson();
        final var post = gson.fromJson(body, Post.class);//конвертируем файл в обьект
        final var data = service.save(post);//передаем обьект пост в сервисб результат записываем в переменную
        response.getWriter().print(gson.toJson(data));//записываем значение переменной и файл в ответ
    }

    public void removeById(long id, HttpServletResponse response) {
        // TODO: deserialize request & serialize response
        service.removeById(id);
        response.setStatus(SC_NO_CONTENT);
    }
}
