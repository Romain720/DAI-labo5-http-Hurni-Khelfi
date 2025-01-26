package org.example;

/**
 * Classe modèle représentant une tâche (todo).
 * Cette classe définit la structure des données pour chaque todo dans l'application.
 */
public class Todo {
    // Identifiant unique du todo
    private Long id;
    // Titre de la tâche
    private String title;
    // Description détaillée de la tâche
    private String description;
    // État de complétion de la tâche
    private boolean completed;

    /**
     * Constructeur par défaut requis pour la désérialisation JSON
     */
    public Todo() {
    }

    /**
     * Constructeur avec tous les champs
     * @param id Identifiant unique du todo
     * @param title Titre de la tâche
     * @param description Description détaillée
     * @param completed État de complétion
     */
    public Todo(Long id, String title, String description, boolean completed) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
