package task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description, TaskStatus priority, int epicId, Duration duration, LocalDateTime localDateTime) {
        super(name, description, priority, duration, localDateTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus priority, int epicId, Duration duration) {
        super(name, description, priority, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus priority, int epicId, int id, Duration duration, LocalDateTime localDateTime) {
        super(name, description, priority, duration, localDateTime);
        this.epicId = epicId;
        this.id = id;
    }

    public Subtask(String name, String description, TaskStatus priority, int epicId, int id, Duration duration) {
        super(name, description, priority, duration);
        this.epicId = epicId;
        this.id = id;
    }

    public Subtask() {

    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public Duration getDuration() {
        return super.getDuration();
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", priority=" + priority +
                ", description='" + description + '\'' +
                ", epicId=" + epicId +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + getEndTime() +
                '}';
    }
}
