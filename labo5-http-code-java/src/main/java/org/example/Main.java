package org.example;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;

/**
 * Classe principale qui configure et démarre le serveur Javalin.
 * Gère les endpoints REST pour l'API Todo.
 */
public class Main {
    // Service singleton pour gérer les opérations sur les todos
    private static final TodoService todoService = new TodoService();

    public static void main(String[] args) {
        // Configuration et démarrage du serveur Javalin
        Javalin app = Javalin.create(config -> {
            // Activation du CORS pour permettre les requêtes cross-origin
            config.plugins.enableCors(cors -> cors.add(it -> {
                it.anyHost();
            }));
        }).start(8081);

        // Configuration des endpoints CRUD pour l'API
        app.get("/api", Main::getAllTodos);          // Récupérer tous les todos
        app.get("/api/{id}", Main::getTodo);         // Récupérer un todo par son ID
        app.post("/api", Main::createTodo);          // Créer un nouveau todo
        app.put("/api/{id}", Main::updateTodo);      // Mettre à jour un todo existant
        app.delete("/api/{id}", Main::deleteTodo);   // Supprimer un todo
    }

    /**
     * Récupère et renvoie la liste de tous les todos
     */
    private static void getAllTodos(Context ctx) {
        ctx.json(todoService.getAllTodos());
    }

    /**
     * Récupère un todo spécifique par son ID
     * Renvoie 404 si le todo n'existe pas
     */
    private static void getTodo(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        Todo todo = todoService.getTodo(id);
        if (todo != null) {
            ctx.json(todo);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Crée un nouveau todo à partir des données reçues
     * Renvoie le todo créé avec un statut 201 (Created)
     */
    private static void createTodo(Context ctx) {
        Todo todo = ctx.bodyAsClass(Todo.class);
        Todo createdTodo = todoService.createTodo(todo);
        ctx.status(HttpStatus.CREATED).json(createdTodo);
    }

    /**
     * Met à jour un todo existant
     * Renvoie 404 si le todo n'existe pas
     */
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

    /**
     * Supprime un todo par son ID
     * Renvoie 204 si supprimé avec succès, 404 si non trouvé
     */
    private static void deleteTodo(Context ctx) {
        Long id = ctx.pathParamAsClass("id", Long.class).get();
        if (todoService.deleteTodo(id)) {
            ctx.status(HttpStatus.NO_CONTENT);
        } else {
            ctx.status(HttpStatus.NOT_FOUND);
        }
    }
}
