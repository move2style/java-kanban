package manager;
import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface HistoryManager {

    void addTask(Task task);

    List<Task> getHistory ();

}
