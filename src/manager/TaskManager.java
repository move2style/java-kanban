package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.List;

public interface TaskManager {
    void createTask(Task task);

    void updateTask(Task task);

    void createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void createEpic(Epic epic);

    void updateEpic(Epic epic);

    List getTaskList();

    List getSubtaskList();

    List getEpicList();

    Task getTaskById(Integer idTask);

    Subtask getSubtaskById(Integer idSubtask);

    Epic getEpicById(Integer idEpic);

    void removeTaskMap();

    void removeTasksPrioritizedTasks();

    void removeSubtask();

    void removeSubtasksPrioritizedTasks();

    void removeEpicMap();

    void removeEpicsPrioritizedTasks();

    void deleteTask(Integer idTask);

    void deleteSubtask(Integer idSubtask);

    void deleteEpic(Integer idEpic);

    List getSubtaskListForEpic(Integer idEpic);

    List getHistory();

    List<Task> getPrioritizedTasks();

    @Override
    String toString();

}
