import manager.HistoryManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static task.TaskStatus.NEW;

class TaskManagerTest {
    TaskManager taskManager = Managers.getDefaultTaskManager();
    HistoryManager historyManager = Managers.getDefaultHistoryManager();

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        taskManager.createTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getTaskList();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }
    @Test
    void add() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        taskManager.createTask(task);
        historyManager.addTask(task);
        final List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }
    @Test
    void shouldBeEqualsCopyTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        taskManager.createTask(task);

        final int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        Task savedTaskNew = new Task("Test addNewTask", "Test addNewTask description", NEW,task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(savedTask.getId(), savedTaskNew.getId(), "Id не совпадают.");
        assertEquals(savedTask.getDescription(), savedTaskNew.getDescription(), "Description не совпадают.");
        assertEquals(savedTask.getName(), savedTaskNew.getName(), "Name не совпадают.");
        assertEquals(savedTask.getStatus(), savedTaskNew.getStatus(), "Status не совпадают.");
    }
    @Test
    void shouldBeEqualsCopyEpic() {
        Epic newepic1 = new Epic("Epic po spisky#1", "opisanie",TaskStatus.NEW);
        taskManager.createEpic(newepic1);
        final int epicId = newepic1.getId();
        final Epic savedEpic = taskManager.getEpicById(epicId);

        Subtask newsubtask1 = new Subtask("Subtask #1", "opisanie",TaskStatus.NEW,newepic1.getId());
        Subtask newsubtask2 = new Subtask("Subtask #2", "opisanie",TaskStatus.NEW,newepic1.getId());
        taskManager.createSubtask(newsubtask1);
        taskManager.createSubtask(newsubtask2);

        Epic savedEpicNew = new Epic("Epic po spisky#1", "opisanie", NEW,newepic1.getId());
        savedEpicNew.getSubtaskIds().add(newsubtask1.getId());
        savedEpicNew.getSubtaskIds().add(newsubtask2.getId());

        assertNotNull(savedEpic, "Задача не найдена.");
        assertEquals(savedEpic.getId(), savedEpicNew.getId(), "Id не совпадают.");
        assertEquals(savedEpic.getDescription(), savedEpicNew.getDescription(), "Description не совпадают.");
        assertEquals(savedEpic.getName(), savedEpicNew.getName(), "Name не совпадают.");
        assertEquals(savedEpic.getStatus(), savedEpicNew.getStatus(), "Status не совпадают.");
        assertEquals(savedEpic.getSubtaskIds(), savedEpicNew.getSubtaskIds(), "Subtask не совпадают.");
    }
    @Test
    void shouldBeEqualsCopySubtask() {
        Epic newepic1_1 = new Epic("Epic po spisky#1", "opisanie",TaskStatus.IN_PROGRESS);
        taskManager.createEpic(newepic1_1);
        Subtask subtask1 = new Subtask("Subtask #1", "opisanie",TaskStatus.NEW,newepic1_1.getId());
        taskManager.createSubtask(subtask1);
        final int subTaskId = subtask1.getId();
        final Subtask savedSubTask = taskManager.getSubtaskById(subTaskId);
        Subtask newsubtask2 = new Subtask("Subtask #1", "opisanie",TaskStatus.NEW,subtask1.getEpicId(),
                subtask1.getId());

        assertNotNull(savedSubTask, "Задача не найдена.");
        assertEquals(savedSubTask.getId(), newsubtask2.getId(), "Id не совпадают.");
        assertEquals(savedSubTask.getDescription(), newsubtask2.getDescription(), "Description не совпадают.");
        assertEquals(savedSubTask.getName(), newsubtask2.getName(), "Name не совпадают.");
        assertEquals(savedSubTask.getStatus(), newsubtask2.getStatus(), "Status не совпадают.");
        assertEquals(savedSubTask.getEpicId(), newsubtask2.getEpicId(), "EpicId не совпадают.");
    }
    //НАВЕРНОЕ НЕПРАВИЛЬНО РЕАЛИЗОВАНА проверьте, что наследники класса Task равны друг другу, если равен их id;
    @Test
    void shouldBeErrorWhenEpicAddEpicHowSubtask() {
        Epic newepic1_2 = new Epic("Epic po spisky#1_1", "opisanie",TaskStatus.NEW);
        taskManager.createEpic(newepic1_2);
        final int epicId = newepic1_2.getId();
        final Epic savedEpic = taskManager.getEpicById(epicId);

        Subtask newsubtask1 = new Subtask(savedEpic.getName(), savedEpic.getDescription(),savedEpic.getStatus(),
                newepic1_2.getId(),newepic1_2.getId());
        taskManager.createSubtask(newsubtask1);
        final Subtask savedEpicNew = taskManager.getSubtaskById(newsubtask1.getId());

        assertNull(savedEpicNew);
    }
    //НАВЕРНОЕ НЕПРАВИЛЬНО РЕАЛИЗОВАНА проверьте, что объект Epic нельзя добавить в самого себя в виде подзадачи;
    @Test
    void shouldBeErrorWhenSubtaskAddHowEpic() {
        Epic epic = new Epic("Epic po spisky#2", "opisanie",TaskStatus.NEW);
        taskManager.createEpic(epic);
        final int epicId = epic.getId();
        final Epic savedEpic = taskManager.getEpicById(epicId);

        Subtask subtask1 = new Subtask("Subtask #1", "opisanie",TaskStatus.NEW,epic.getId());
        taskManager.createSubtask(subtask1);
        final int subtaskId = subtask1.getId();
        final Subtask savedSubtask = taskManager.getSubtaskById(subtask1.getId());

        Epic newepic = new Epic(subtask1.getName(), subtask1.getDescription(),subtask1.getStatus(),epicId);
        taskManager.updateEpic(newepic);
        final Epic savedEpicNew = taskManager.getEpicById(newepic.getId());
        List<Integer> listIdSubtask = savedEpicNew.getSubtaskIds();

         assertNotEquals(savedEpicNew.getSubtaskIds(),savedEpic.getSubtaskIds(), "IDSubtask равны");
         assertNotEquals(savedSubtask.getId(),savedEpicNew.getSubtaskIds(), "IDSubtask есть в созданном епике" +
                 " равны");
    }
    @Test
    void classManagersAlwaysInitialized() {
        TaskManager taskManager = Managers.getDefaultTaskManager();
        HistoryManager historyManager = Managers.getDefaultHistoryManager();

        assertNotNull(taskManager);
        assertNotNull(historyManager);
    }
    @Test
    void inMemoryTaskManagerAddsAllTypeTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        taskManager.createTask(task);
        final int taskId = task.getId();

        Epic newepic2 = new Epic("Epic po spisky#2", "opisanie",TaskStatus.NEW);
        taskManager.createEpic(newepic2);
        final int epicId = newepic2.getId();

        Subtask subtask1 = new Subtask("Subtask #1", "opisanie",TaskStatus.NEW,newepic2.getId());
        taskManager.createSubtask(subtask1);
        final int subtaskId = subtask1.getId();

        assertNotNull(taskManager.getTaskList());
        assertNotNull(taskManager.getSubtaskList());
        assertNotNull(taskManager.getEpicList());
        assertNotNull(taskManager.getTaskById(taskId));
        assertNotNull(taskManager.getEpicById(epicId));
        assertNotNull(taskManager.getSubtaskById(subtaskId));
    }
    @Test
    void inMemoryTaskManagerAddTaskWithGenerateIdAndValueId() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        taskManager.createTask(task);
        final int taskId = task.getId();

        Task task2 = new Task("Test addNewTask", "Test addNewTask description", NEW,taskId);
        taskManager.createTask(task2);

        assertEquals(1, taskManager.getTaskList().size(), "Неверное количество задач.");
    }
    @Test
    void managerCreatTaskWithoutError() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", NEW);
        taskManager.createTask(task);
        final int taskId = task.getId();
        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task.getId(), taskManager.getTaskById(taskId).getId(), "Id не совпадают.");
        assertEquals(task.getDescription(), taskManager.getTaskById(taskId).getDescription(), "Description не совпадают.");
        assertEquals(task.getName(), taskManager.getTaskById(taskId).getName(), "Name не совпадают.");
        assertEquals(task.getStatus(), taskManager.getTaskById(taskId).getStatus(), "Status не совпадают.");
    }

    @Test
    void removingSubtasksHaveNotOldId() {
        Epic newepic1 = new Epic("Epic po spisky#1", "opisanie",TaskStatus.NEW);
        taskManager.createEpic(newepic1);
        Epic newepic2 = new Epic("Epic po spisky#2", "opisanie",TaskStatus.NEW);
        taskManager.createEpic(newepic2);
        Subtask newsubtask1 = new Subtask("Subtask #1", "opisanie",TaskStatus.NEW,newepic1.getId());
        Subtask newsubtask2 = new Subtask("Subtask #2", "opisanie",TaskStatus.NEW,newepic1.getId());
        Subtask newsubtask3 = new Subtask("Subtask #3", "opisanie",TaskStatus.NEW,newepic1.getId());
        taskManager.createSubtask(newsubtask1);
        taskManager.createSubtask(newsubtask2);
        taskManager.createSubtask(newsubtask3);
        Subtask newsubtask4 = new Subtask("Subtask #4", "opisanie",TaskStatus.NEW,newepic2.getId());
        Subtask newsubtask5 = new Subtask("Subtask #5", "opisanie",TaskStatus.NEW,newepic2.getId());
        taskManager.createSubtask(newsubtask4);
        taskManager.createSubtask(newsubtask5);

        Epic epic = taskManager.getEpicById(newsubtask1.getEpicId());
        List<Integer> subtasks = epic.getSubtaskIds();

        taskManager.deleteSubtask(newsubtask1.getId());
        assertNull(taskManager.getSubtaskById(newsubtask1.getId()), "ID подзадачи найден.");

        epic = taskManager.getEpicById(newsubtask1.getEpicId());
        List<Integer> subtasks2 = epic.getSubtaskIds();
        assertNotEquals(subtasks,subtasks2, "ID подзадача в епике не удалена.");
    }
}