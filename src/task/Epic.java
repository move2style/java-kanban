package task;

import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subtaskIds = new ArrayList<>();

    public Epic(String name, String description, TaskStatus priority) {
        super(name, description, priority);
    }

    public Epic(String name, String description, TaskStatus priority,Integer id) {
        super(name, description, priority, id);
    }

    public Epic(String name, String description, TaskStatus priority,Integer id, List<Integer> subtaskIds) {
        super(name, description, priority, id);
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

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskIds=" + subtaskIds +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + getStatus() +
                ", id=" + id +
                '}';
    }
}
