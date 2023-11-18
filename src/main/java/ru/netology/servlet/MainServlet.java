package ru.netology.servlet;

import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
    private PostController controller;
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";

    private static final String validPath = "/api/posts/";


    @Override
    public void init() {
        final var repository = new PostRepository();
        //создаем хранилище
        final var service = new PostService(repository);
        //создаем сервис (бизнес логика) и записываем данные из хранилища
        controller = new PostController(service);
        //в контроллер передаем информацию для отправки пользователю обработанную сервисом
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        // если деплоились в root context, то достаточно этого
        try {
            final var path = req.getRequestURI();
            final var method = req.getMethod();
            // primitive routing
            if (method.equals(GET) && path.equals("/api/posts")) {
                controller.all(resp);
                return;
            }
            if (method.equals(GET) && path.matches(validPath + "\\d+")) {
                // easy way
                final var id = parserId(path);
                controller.getById(id, resp);
                return;
            }
            if (method.equals(POST) && path.equals("/api/posts")) {
                controller.save(req.getReader(), resp);
                return;
            }
            if (method.equals(DELETE) && path.matches(validPath + "\\d+")) {
                // easy way
                final var id = parserId(path);
                controller.removeById(id, resp);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    protected Long parserId(String path) {
        return Long.parseLong(path.substring(path.lastIndexOf("/")));
    }
}

