package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TodoService {
    private final Map<Long, Todo> todos = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong();

    public TodoService() {
        // Add some sample data
        createTodo(new Todo(null, "Learn Javalin", "Complete the DAI lab", false));
        createTodo(new Todo(null, "Write tests", "Test the Todo API", false));
    }

    public List<Todo> getAllTodos() {
        return new ArrayList<>(todos.values());
    }

    public Todo getTodo(Long id) {
        return todos.get(id);
    }

    public Todo createTodo(Todo todo) {
        Long id = idCounter.incrementAndGet();
        todo.setId(id);
        todos.put(id, todo);
        return todo;
    }

    public Todo updateTodo(Long id, Todo todo) {
        if (todos.containsKey(id)) {
            todo.setId(id);
            todos.put(id, todo);
            return todo;
        }
        return null;
    }

    public boolean deleteTodo(Long id) {
        return todos.remove(id) != null;
    }
}
