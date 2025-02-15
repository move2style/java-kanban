import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    TaskManager taskManager = Managers.getDefaultTaskManager();

    @Test
    void HistoryManagerTests() {
        Task newtask1 = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW);
        taskManager.createTask(newtask1);
        Task newtask2 = new Task("Zadacha po spisky#2", "opisanie", TaskStatus.NEW);
        taskManager.createTask(newtask2);
        Task newtask1_1 = new Task("Zadacha po spisky#1_1", "opisanie", TaskStatus.IN_PROGRESS, 1);
        taskManager.updateTask(newtask1_1);
        Epic newepic1 = new Epic("Epic po spisky#1", "opisanie", TaskStatus.NEW);
        taskManager.createEpic(newepic1);
        Epic newepic1_1 = new Epic("Epic po spisky#1", "opisanie", TaskStatus.IN_PROGRESS, 3);
        taskManager.updateEpic(newepic1_1);
        Epic newepic1_2 = new Epic("Epic po spisky#1_1", "opisanie", TaskStatus.NEW, 3);
        taskManager.updateEpic(newepic1_2);
        Epic newepic2 = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW);
        taskManager.createEpic(newepic2);
        Subtask newsubtask1 = new Subtask("Subtask #1", "opisanie", TaskStatus.NEW, newepic1.getId());
        Subtask newsubtask2 = new Subtask("Subtask #2", "opisanie", TaskStatus.NEW, newepic1.getId());
        Subtask newsubtask3 = new Subtask("Subtask #3", "opisanie", TaskStatus.NEW, newepic1.getId());
        taskManager.createSubtask(newsubtask1);
        taskManager.createSubtask(newsubtask2);
        taskManager.createSubtask(newsubtask3);
        Subtask newsubtask4 = new Subtask("Subtask #4", "opisanie", TaskStatus.NEW, newepic2.getId());
        Subtask newsubtask5 = new Subtask("Subtask #5", "opisanie", TaskStatus.NEW, newepic2.getId());
        taskManager.createSubtask(newsubtask4);
        taskManager.createSubtask(newsubtask5);
        Subtask newsubtask5_1 = new Subtask("Subtask #5_1", "opisanie 5_1", TaskStatus.NEW, newepic2.getId(), 9);
        taskManager.updateSubtask(newsubtask5_1);
        Subtask newsubtask5_2 = new Subtask("Subtask #5_1", "opisanie 5_1", TaskStatus.DONE, newepic2.getId(), 9);
        taskManager.updateSubtask(newsubtask5_2);
        Subtask newsubtask4_1 = new Subtask("Subtask #5_1", "opisanie 5_1", TaskStatus.DONE, newepic2.getId(), 8);
        taskManager.updateSubtask(newsubtask4_1);
        newsubtask5_2 = new Subtask("Subtask #5_1", "opisanie 5_1", TaskStatus.NEW, newepic2.getId(), 9);
        taskManager.updateSubtask(newsubtask5_2);
        newsubtask4_1 = new Subtask("Subtask #5_1", "opisanie 5_1", TaskStatus.NEW, newepic2.getId(), 8);
        taskManager.updateSubtask(newsubtask4_1);

        taskManager.getEpicById(4);
        List<Task> history = taskManager.getHistory();
        taskManager.getEpicById(4);
        //проверить удаление и запись в истории, если в истории только 1 элемент
        assertEquals(history.size(), taskManager.getHistory().size(), "Неверное количество задач.");
        assertEquals(history.getFirst(), taskManager.getHistory().getFirst(), "Не совпадают.");

        //проверить запись новых задач и повторный просмотр задачи из центра списка + запись в конец списка
        Task firstReplic = taskManager.getTaskById(1);
        taskManager.getSubtaskById(8);
        history = taskManager.getHistory();
        taskManager.getTaskById(1);
        assertEquals(history.size(), taskManager.getHistory().size(), "Неверное количество задач.");
        assertEquals(firstReplic, taskManager.getHistory().getLast(), "Объекты не равны");
    }

}