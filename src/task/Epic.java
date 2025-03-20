package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subtaskIds = new ArrayList<>();
    protected LocalDateTime endTime;

    public Epic(String name, String description, TaskStatus priority, Duration duration, LocalDateTime localDateTime) {
        super(name, description, priority, duration, localDateTime);
    }

    public Epic(String name, String description, TaskStatus priority, Duration duration) {
        super(name, description, priority, duration);
    }

    public Epic(String name, String description, TaskStatus priority, Integer id,Duration duration, LocalDateTime localDateTime) {
        super(name, description, priority, id, duration, localDateTime);
    }

    public Epic(String name, String description, TaskStatus priority, Integer id,Duration duration) {
        super(name, description, priority, id, duration);
    }

    public Epic(String name, String description, TaskStatus priority, Integer id, List<Integer> subtaskIds,Duration duration, LocalDateTime localDateTime) {
        super(name, description, priority, id, duration, localDateTime);
        this.subtaskIds = subtaskIds;
    }

    public Epic() {

    }

    public void setSubtaskIds(List<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }

    public List<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", subtaskIds=" + subtaskIds +
                ", duration=" + duration +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
