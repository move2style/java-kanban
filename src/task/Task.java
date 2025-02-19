package task;

import java.util.Objects;
import org.junit.platform.engine.support.hierarchical.EngineExecutionContext;


public class Task implements EngineExecutionContext {
    protected String name;
    protected String description;
    protected TaskStatus priority;
    protected Integer id;

    public Task() {
    }

    public Task(String name, String description, TaskStatus priority) {
        this.name = name;
        this.description = description;
        this.priority = priority;

    }

    public Task(String name, String description, TaskStatus priority, Integer id) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return priority;
    }

    public void setStatus(TaskStatus priority) {
        this.priority = priority;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", id=" + id +
                '}';
    }
}
