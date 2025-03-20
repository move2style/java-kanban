package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

import org.junit.platform.engine.support.hierarchical.EngineExecutionContext;


public class Task implements EngineExecutionContext {
    protected String name;
    protected String description;
    protected TaskStatus priority;
    protected Integer id;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task() {
    }

    public Task(String name, String description, TaskStatus priority, Duration duration) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.duration = duration;
    }

    public Task(String name, String description, TaskStatus priority, Duration duration, LocalDateTime localDateTime) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.duration = duration;
        this.startTime = localDateTime;
    }

    public Task(String name, String description, TaskStatus priority, Integer id, Duration duration, LocalDateTime localDateTime) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.id = id;
        this.duration = duration;
        this.startTime = localDateTime;
    }

    public Task(String name, String description, TaskStatus priority, Integer id, Duration duration) {
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.id = id;
        this.duration = duration;
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

    public TaskType getType() {
        return TaskType.TASK;
    }

    public TaskStatus getPriority() {
        return priority;
    }

    public void setPriority(TaskStatus priority) {
        this.priority = priority;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + getEndTime() +
                '}';
    }
}

