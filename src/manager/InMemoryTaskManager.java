package manager;

import task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

public class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    protected final HashMap<Integer, Task> taskMap = new HashMap<>();
    protected final HashMap<Integer, Epic> epicMap = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtaskMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    private TreeSet<Task> prioritizedTasks = new TreeSet<>(new TaskComparator());

    @Override
    public void createTask(Task task) {

        if (Objects.nonNull(task.getId())) {
            return;
        }

        if (taskMap.size() > 0) {
            Stream<Task> stream = getPrioritizedTasks().stream();
            boolean overlaps = stream.anyMatch(existingTask -> tasksOverlapTime(task, existingTask));

            if (overlaps) {
                return;
            }
        }


        int newId = generateId();
        task.setId(newId);
        taskMap.put(task.getId(), task);

        if (task.getStartTime() != null) {
            Task taskToRemove = prioritizedTasks.stream()
                    .filter(t -> t.getId() == task.getId())
                    .findFirst()
                    .orElse(null);

            if (taskToRemove != null) {
                prioritizedTasks.remove(taskToRemove);
            }
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateTask(Task task) {
        if (Objects.isNull(task.getId())) {
            return;
        }

        if (!taskMap.containsKey(task.getId())) {
            return;
        }

        if (taskMap.size() > 0) {
            Stream<Task> stream1 = getPrioritizedTasks().stream();
            boolean overlaps = stream1.anyMatch(existingTask -> tasksOverlapTime(task, existingTask));

            if (overlaps) {
                Task taskToRemove = prioritizedTasks.stream()
                        .filter(t -> t.getId() == task.getId())
                        .findFirst()
                        .orElse(null);

                if (taskToRemove != null) {
                    prioritizedTasks.remove(taskToRemove);
                }
                taskMap.remove(task.getId());
                return;
            }
        }

        taskMap.put(task.getId(), task);

        if (task.getStartTime() != null) {
            Task taskToRemove = prioritizedTasks.stream()
                    .filter(t -> t.getId() == task.getId())
                    .findFirst()
                    .orElse(null);

            if (taskToRemove != null) {
                prioritizedTasks.remove(taskToRemove);
            }
            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateTaskLoadFromFile(Task task) {
        if (Objects.isNull(task.getId())) {
            return;
        }

        taskMap.put(task.getId(), task);

        if (task.getStartTime() != null) {
            Task taskToRemove = prioritizedTasks.stream()
                    .filter(t -> t.getId() == task.getId())
                    .findFirst()
                    .orElse(null);

            if (taskToRemove != null) {
                prioritizedTasks.remove(taskToRemove);
            }

            prioritizedTasks.add(task);
        }
    }

    @Override
    public void updateSubtaskLoadFromFile(Subtask subtask) {
        if (Objects.isNull(subtask.getEpicId())) {
            return;
        }

        if (subtask.getEpicId() == 0) {
            return;
        }

        subtaskMap.put(subtask.getId(), subtask);
        if (subtask.getStartTime() != null) {
            Task taskToRemove = prioritizedTasks.stream()
                    .filter(t -> t.getId() == subtask.getId())
                    .findFirst()
                    .orElse(null);

            if (taskToRemove != null) {
                prioritizedTasks.remove(taskToRemove);
            }
            prioritizedTasks.add(subtask);
        }

        Epic epic = epicMap.get(subtask.getEpicId());
        epic.setStatus(TaskStatus.IN_PROGRESS);

        List<Subtask> subtasksListEpic = getSubtaskListForEpic(epic.getId());
        Stream<TaskStatus> stream = subtasksListEpic.stream()
                .map(subtask1 -> subtask1.getStatus());
        boolean result = stream.allMatch(status -> status.equals(TaskStatus.DONE));

        if (result) {
            epic.setStatus(TaskStatus.DONE);

            LocalDateTime endTime = subtasksListEpic.stream()
                    .filter(Objects::nonNull)
                    .map(subtaskStream -> subtaskStream.getEndTime())
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            epic.setEndTime(endTime);
        }
    }

    @Override
    public void updateEpicLoadFromFile(Epic epic) {

        if (epicMap.containsKey(epic.getId())) {
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

        epicMap.put(epic.getId(), epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {

        if (Objects.nonNull(subtask.getId())) {
            return;
        }

        if (subtask.getEpicId() == 0) {
            return;
        }

        if (subtaskMap.size() > 0) {
            Stream<Task> stream1 = getPrioritizedTasks().stream();
            boolean overlaps = stream1.anyMatch(existingTask -> tasksOverlapTime(subtask, existingTask));

            if (overlaps) {
                return;
            }
        }

        int newId = generateId();
        subtask.setId(newId);
        subtaskMap.put(subtask.getId(), subtask);

        if (subtask.getStartTime() != null) {
            Task taskToRemove = prioritizedTasks.stream()
                    .filter(t -> t.getId() == subtask.getId())
                    .findFirst()
                    .orElse(null);

            if (taskToRemove != null) {
                prioritizedTasks.remove(taskToRemove);
            }

            prioritizedTasks.add(subtask);
        }

        Epic epic = epicMap.get(subtask.getEpicId());
        epic.getSubtaskIds().add(newId);

        List<Subtask> subtasksListEpic = getSubtaskListForEpic(epic.getId());

        Duration totalDuration = subtasksListEpic.stream()
                .map(subtaskStream -> subtaskStream.getDuration())
                .reduce(Duration.ZERO, Duration::plus);
        epic.setDuration(totalDuration);

        LocalDateTime startDate = subtasksListEpic.stream()
                .filter(Objects::nonNull)
                .map(subtaskStream -> subtaskStream.getStartTime())
                .min(LocalDateTime::compareTo)
                .orElse(null);
        epic.setStartTime(startDate);

        if (subtask.getStatus().equals(TaskStatus.DONE) && epic.getStatus().equals(TaskStatus.DONE)) {
            LocalDateTime endTime = subtasksListEpic.stream()
                    .filter(Objects::nonNull)
                    .map(subtaskStream -> subtaskStream.getEndTime())
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            epic.setEndTime(endTime);
        }
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

        if (subtaskMap.size() > 0) {
            Stream<Task> stream1 = getPrioritizedTasks().stream();
            boolean overlaps = stream1.anyMatch(existingTask -> tasksOverlapTime(subtask, existingTask));
            if (overlaps) {
                Task taskToRemove = prioritizedTasks.stream()
                        .filter(t -> t.getId() == subtask.getId())
                        .findFirst()
                        .orElse(null);

                if (taskToRemove != null) {
                    prioritizedTasks.remove(taskToRemove);
                }
                subtaskMap.remove(subtask.getId());
                return;
            }
        }

        subtaskMap.put(subtask.getId(), subtask);
        if (subtask.getStartTime() != null) {
            Task taskToRemove = prioritizedTasks.stream()
                    .filter(t -> t.getId() == subtask.getId())
                    .findFirst()
                    .orElse(null);

            if (taskToRemove != null) {
                prioritizedTasks.remove(taskToRemove);
            }
            prioritizedTasks.add(subtask);
        }

        Epic epic = epicMap.get(subtask.getEpicId());
        epic.setStatus(TaskStatus.IN_PROGRESS);

        List<Subtask> subtasksListEpic = getSubtaskListForEpic(epic.getId());
        Stream<TaskStatus> stream = subtasksListEpic.stream()
                .map(subtask1 -> subtask1.getStatus());
        boolean result = stream.allMatch(status -> status.equals(TaskStatus.DONE));

        if (result) {
            epic.setStatus(TaskStatus.DONE);

            LocalDateTime endTime = subtasksListEpic.stream()
                    .filter(Objects::nonNull)
                    .map(subtaskStream -> subtaskStream.getEndTime())
                    .max(LocalDateTime::compareTo)
                    .orElse(null);
            epic.setEndTime(endTime);
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

        if (epicMap.containsKey(epic.getId())) {
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

        epicMap.put(epic.getId(), epic);
    }

    @Override
    public boolean tasksOverlapTime(Task task1, Task task2) {
        if (task1.getStartTime() == null || task2.getStartTime() == null || task1.getDuration() == null || task2.getDuration() == null) {
            return false;
        }

        if (task1.getEndTime() == null || task2.getEndTime() == null) {
            return false;
        }

        return task1.getStartTime().isBefore(task2.getEndTime()) && task2.getStartTime().isBefore(task1.getStartTime());
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
    public Subtask getSubtaskByIdForStream(Integer idSubtask) {
        Subtask subtask = subtaskMap.get(idSubtask);
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
    public void removeTasksPrioritizedTasks() {
        prioritizedTasks.removeIf(task -> task.getType().equals(TaskType.TASK));
    }

    @Override
    public void removeSubtask() {
        subtaskMap.clear();
    }

    @Override
    public void removeSubtasksPrioritizedTasks() {
        prioritizedTasks.removeIf(task -> task.getType().equals(TaskType.SUBTASK));
    }

    @Override
    public void removeEpicMap() {
        epicMap.clear();
    }

    @Override
    public void removeEpicsPrioritizedTasks() {
        prioritizedTasks.removeIf(task -> task.getType().equals(TaskType.EPIC));
    }

    @Override
    public void deleteTask(Integer idTask) {
        if (taskMap.get(idTask) != null) {
            taskMap.remove(idTask);

            historyManager.remove(idTask);
            prioritizedTasks.removeIf(task -> task.getId() == idTask);
        }
    }

    @Override
    public void deleteSubtask(Integer idSubtask) {
        if (subtaskMap.get(idSubtask) != null) {
            List<Subtask> subtaskList = new ArrayList<>(getSubtaskListForEpic(getSubtaskById(idSubtask).getEpicId()));
            subtaskList.remove(getSubtaskById(idSubtask));
            List<Integer> idSubtasks = subtaskList.stream().map(t -> t.getId()).toList();
            Epic updateEpic = getEpicById(getSubtaskById(idSubtask).getEpicId());
            updateEpic.setSubtaskIds(idSubtasks);
            updateEpic(updateEpic);

            subtaskMap.remove(idSubtask);
            prioritizedTasks.removeIf(task -> task.getId() == idSubtask);
            historyManager.remove(idSubtask);
        }
    }

    @Override
    public void deleteEpic(Integer idEpic) {
        if (epicMap.get(idEpic) != null) {
            epicMap.remove(idEpic);
            prioritizedTasks.removeIf(task -> task.getId() == idEpic);
            historyManager.remove(idEpic);
        }
    }

    @Override
    public List getSubtaskListForEpic(Integer idEpic) {
        Epic epic = epicMap.get(idEpic);

        List<Subtask> getSubtaskListForEpic = epic.getSubtaskIds().stream()
                .map(id -> getSubtaskByIdForStream(id))
                .toList();
        return getSubtaskListForEpic;
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
