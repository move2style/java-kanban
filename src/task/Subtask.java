package task;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description, TaskStatus priority, int epicId) {
        super(name, description,  priority);
        this.epicId=epicId;
    }

    public Subtask(String name, String description, TaskStatus priority, int epicId,int id) {
        super(name, description,  priority);
        this.epicId=epicId;
        this.id=id;
    }

    public Subtask() {

    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + epicId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", id=" + id +
                '}';
    }
}
