package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private final HashMap<Integer, Task> taskMap = new HashMap<>();
    private final HashMap<Integer, Epic> epicMap = new HashMap<>();
    private final HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    @Override
    public void createTask(Task task) {

        if (Objects.nonNull(task.getId())) {
            return;
        }

        int newId = generateId();
        task.setId(newId);
        taskMap.put(task.getId(), task);
    }

    @Override
    public void updateTask(Task task) {
        if (Objects.isNull(task.getId())) {
            return;
        }

        if (!taskMap.containsKey(task.getId())) {
            return;
        }
        taskMap.put(task.getId(), task);
    }

    @Override
    public void createSubtask(Subtask subtask) {

        if (Objects.nonNull(subtask.getId())) {
            return;
        }

        if (subtask.getEpicId() == 0) {
            return;
        }

        if (!epicMap.containsKey(subtask.getEpicId())) {
            return;
        }

        int newId = generateId();
        subtask.setId(newId);
        subtaskMap.put(subtask.getId(), subtask);

        Epic epic = epicMap.get(subtask.getEpicId());
        epic.getSubtaskIds().add(newId);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (Objects.isNull(subtask.getEpicId())) {
            return;
        }

        if (subtask.getEpicId() == 0) {
            return;
        }

        if (!epicMap.containsKey(subtask.getEpicId())) {
            return;
        }

        subtaskMap.put(subtask.getId(), subtask);

        Epic epic = epicMap.get(subtask.getEpicId());
        epic.setStatus(TaskStatus.IN_PROGRESS);

        int countDone = 0;
        int countNew = 0;

        for (Integer id : epic.getSubtaskIds()) {
            Subtask oldSubtask = subtaskMap.get(id);
            if ((subtask.getStatus().equals(TaskStatus.DONE) && oldSubtask.getStatus().equals(TaskStatus.DONE))) {
                countDone++;
            }

            if (countDone == epic.getSubtaskIds().size()) {
                epic.setStatus(TaskStatus.DONE);
            }

            if ((subtask.getStatus().equals(TaskStatus.NEW) && oldSubtask.getStatus().equals(TaskStatus.NEW))) {
                countNew++;
            }

            if (countNew == epic.getSubtaskIds().size()) {
                epic.setStatus(TaskStatus.NEW);
            }
        }
    }

    @Override
    public void createEpic(Epic epic) {

        if (Objects.nonNull(epic.getId())) {
            return;
        }

        int newId = generateId();
        epic.setId(newId);
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic oldEpic = epicMap.get(epic.getId());
        if (epic.getStatus() != oldEpic.getStatus()) {
            return;
        }

        if (!Objects.nonNull(epic.getId())) {
            return;
        }

        if (!epicMap.containsKey(epic.getId())) {
            return;
        }
        epicMap.put(epic.getId(), epic);
    }

    @Override
    public List getTaskList() {
        return new ArrayList<>(taskMap.values());
    }

    @Override
    public List getSubtaskList() {
        return new ArrayList<>(subtaskMap.values());
    }

    @Override
    public List getEpicList() {
        return new ArrayList<>(epicMap.values());
    }

    @Override
    public Task getTaskById(Integer idTask) {

        Task task = taskMap.get(idTask);
        historyManager.addTask(task);
        return task;
    }

    @Override
    public Subtask getSubtaskById(Integer idSubtask) {
        Subtask subtask = subtaskMap.get(idSubtask);
        historyManager.addTask(subtask);
        return subtask;
    }

    @Override
    public Epic getEpicById(Integer idEpic) {
        Epic epic = epicMap.get(idEpic);
        historyManager.addTask(epic);
        return epic;
    }

    @Override
    public void removeTaskMap() {
        taskMap.clear();
    }

    @Override
    public void removeSubtask() {
        subtaskMap.clear();
    }

    @Override
    public void removeEpicMap() {
        epicMap.clear();
    }

    @Override
    public void deleteTask(Integer idTask) {
        if (taskMap.get(idTask) != null) {
            taskMap.remove(idTask);
        }
    }

    @Override
    public void deleteSubtask(Integer idSubtask) {
        if (subtaskMap.get(idSubtask) != null) {
            subtaskMap.remove(idSubtask);
        }
    }

    @Override
    public void deleteEpic(Integer idEpic) {
        if (epicMap.get(idEpic) != null) {
            epicMap.remove(idEpic);
        }
    }

    @Override
    public List getSubtaskListForEpic(Integer idEpic) {
        Epic epic = epicMap.get(idEpic);
        List<Subtask> getSubtaskListForEpic = new ArrayList<>();

        for (Subtask subtask : subtaskMap.values()) {
            for (Integer id : epic.getSubtaskIds()) {
                if (subtask.getId() == id) {
                    getSubtaskListForEpic.add(subtask);
                }
            }
        }
        return getSubtaskListForEpic;
    }

    private int generateId() {
        return ++id;
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "id=" + id +
                ", taskList=" + taskMap +
                ", epicList=" + epicMap +
                ", subtaskList=" + subtaskMap +
                '}';
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
