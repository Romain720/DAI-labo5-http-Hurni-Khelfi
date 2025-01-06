package org.example;

import io.javalin.Javalin;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create().start(7000);

        List<String> todos = new ArrayList<>();

        app.get("/todos", ctx -> ctx.json(todos)); // Lire tous les éléments
        app.post("/todos", ctx -> { // Créer un nouvel élément
            String todo = ctx.body();
            todos.add(todo);
            ctx.status(201);
        });
        app.put("/todos/{id}", ctx -> { // Mettre à jour un élément
            int id = Integer.parseInt(ctx.pathParam("id"));
            String updatedTodo = ctx.body();
            todos.set(id, updatedTodo);
            ctx.status(204);
        });
        app.delete("/todos/{id}", ctx -> { // Supprimer un élément
            int id = Integer.parseInt(ctx.pathParam("id"));
            todos.remove(id);
            ctx.status(204);
        });
    }
}
