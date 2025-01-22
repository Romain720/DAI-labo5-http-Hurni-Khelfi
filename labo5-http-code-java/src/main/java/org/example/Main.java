package org.example;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

public class Main {
    private static final TodoService todoService = new TodoService();

    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.plugins.enableCors(cors -> cors.add(it -> {
                it.anyHost();
            }));
        }).start(8081);

        // CRUD endpoints
        app.get("/api", Main::getAllTodos);
        app.get("/api/{id}", Main::getTodo);
        app.post("/api", Main::createTodo);
        app.put("/api/{id}", Main::updateTodo);
        app.delete("/api/{id}", Main::deleteTodo);
    }

    private static void getAllTodos(Context ctx) {
        ctx.json(todoService.getAllTodos());
    }

    private static void getTodo(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        Todo todo = todoService.getTodo(id);
        if (todo != null) {
            ctx.json(todo);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
        }
    }

    private static void createTodo(Context ctx) {
        Todo todo = ctx.bodyAsClass(Todo.class);
        Todo createdTodo = todoService.createTodo(todo);
        ctx.status(HttpStatus.CREATED).json(createdTodo);
    }

    private static void updateTodo(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        Todo todo = ctx.bodyAsClass(Todo.class);
        Todo updatedTodo = todoService.updateTodo(id, todo);
        if (updatedTodo != null) {
            ctx.json(updatedTodo);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
        }
    }

    private static void deleteTodo(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        if (todoService.deleteTodo(id)) {
            ctx.status(HttpStatus.NO_CONTENT);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
        }
    }
}
