package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Service gérant les opérations CRUD sur les todos.
 * Utilise une ConcurrentHashMap pour le stockage thread-safe des todos en mémoire.
 */
public class TodoService {
    // Stockage thread-safe des todos avec leur ID comme clé
    private final Map<Long, Todo> todos = new ConcurrentHashMap<>();
    // Générateur d'ID thread-safe
    private final AtomicLong idCounter = new AtomicLong();

    /**
     * Constructeur qui initialise le service avec des données d'exemple
     */
    public TodoService() {
        // Ajout de données d'exemple
        createTodo(new Todo(null, "Learn Javalin", "Complete the DAI lab", false));
        createTodo(new Todo(null, "Write tests", "Test the Todo API", false));
    }

    /**
     * Récupère la liste de tous les todos
     * @return Liste de tous les todos stockés
     */
    public List<Todo> getAllTodos() {
        return new ArrayList<>(todos.values());
    }

    /**
     * Récupère un todo par son ID
     * @param id ID du todo à récupérer
     * @return Le todo correspondant ou null si non trouvé
     */
    public Todo getTodo(Long id) {
        return todos.get(id);
    }

    /**
     * Crée un nouveau todo
     * @param todo Le todo à créer (sans ID)
     * @return Le todo créé avec son ID généré
     */
    public Todo createTodo(Todo todo) {
        Long id = idCounter.incrementAndGet();
        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    /**
     * Met à jour un todo existant
     * @param id ID du todo à mettre à jour
     * @param todo Nouvelles données du todo
     * @return Le todo mis à jour ou null si non trouvé
     */
    public Todo updateTodo(Long id, Todo todo) {
        if (todos.containsKey(id)) {
            todo.setId(id);
            todos.put(id, todo);
            return todo;
        }
        return null;
    }

    /**
     * Supprime un todo par son ID
     * @param id ID du todo à supprimer
     * @return true si le todo a été supprimé, false si non trouvé
     */
    public boolean deleteTodo(Long id) {
        return todos.remove(id) != null;
    }
}
