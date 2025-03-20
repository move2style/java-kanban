import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Test
    void HistoryManagerTests() {
        Task newtask1 = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());
        taskManager.createTask(newtask1);
        Task newtask2 = new Task("Zadacha po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(15));
        taskManager.createTask(newtask2);
        Task newtask1_1 = new Task("Zadacha po spisky#1_1", "opisanie", TaskStatus.IN_PROGRESS, 1,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(30));
        taskManager.updateTask(newtask1_1);
        Epic newepic1 = new Epic("Epic po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(45));
        taskManager.createEpic(newepic1);
        Epic newepic1_1 = new Epic("Epic po spisky#1", "opisanie", TaskStatus.IN_PROGRESS, 3,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(60));
        taskManager.updateEpic(newepic1_1);
        Epic newepic1_2 = new Epic("Epic po spisky#1_1", "opisanie", TaskStatus.NEW, 3,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(75));
        taskManager.updateEpic(newepic1_2);
        Epic newepic2 = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(90));
        taskManager.createEpic(newepic2);
        Subtask newsubtask1 = new Subtask("Subtask #1", "opisanie", TaskStatus.NEW, newepic1.getId(),
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(105));
        Subtask newsubtask2 = new Subtask("Subtask #2", "opisanie", TaskStatus.NEW, newepic1.getId(),
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(120));
        Subtask newsubtask3 = new Subtask("Subtask #3", "opisanie", TaskStatus.NEW, newepic1.getId(),
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(135));
        taskManager.createSubtask(newsubtask1);
        taskManager.createSubtask(newsubtask2);
        taskManager.createSubtask(newsubtask3);
        Subtask newsubtask4 = new Subtask("Subtask #4", "opisanie", TaskStatus.NEW, newepic2.getId(),
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(150));
        Subtask newsubtask5 = new Subtask("Subtask #5", "opisanie", TaskStatus.NEW, newepic2.getId(),
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(165));
        taskManager.createSubtask(newsubtask4);
        taskManager.createSubtask(newsubtask5);
        Subtask newsubtask5_1 = new Subtask("Subtask #5_1", "opisanie 5_1", TaskStatus.NEW, newepic2.getId(), 9,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(180));
        taskManager.updateSubtask(newsubtask5_1);
        Subtask newsubtask5_2 = new Subtask("Subtask #5_1", "opisanie 5_1", TaskStatus.DONE, newepic2.getId(), 9,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(195));
        taskManager.updateSubtask(newsubtask5_2);
        Subtask newsubtask4_1 = new Subtask("Subtask #5_1", "opisanie 5_1", TaskStatus.DONE, newepic2.getId(), 8,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(210));
        taskManager.updateSubtask(newsubtask4_1);
        newsubtask5_2 = new Subtask("Subtask #5_1", "opisanie 5_1", TaskStatus.NEW, newepic2.getId(), 9,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(225));
        taskManager.updateSubtask(newsubtask5_2);
        newsubtask4_1 = new Subtask("Subtask #5_1", "opisanie 5_1", TaskStatus.NEW, newepic2.getId(), 8,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(240));
        taskManager.updateSubtask(newsubtask4_1);

        taskManager.getEpicById(4);
        List history = taskManager.getHistory();
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

    @Test
    void nullHistory(){
        Task newtask1 = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());
        taskManager.createTask(newtask1);
        Epic newepic1 = new Epic("Epic po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(45));
        taskManager.createEpic(newepic1);
        Subtask newsubtask1 = new Subtask("Subtask #1", "opisanie", TaskStatus.NEW, newepic1.getId(),
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(105));
        taskManager.createSubtask(newsubtask1);

        assertTrue(taskManager.getHistory().isEmpty(), "History not null");
    }

    @Test
    void HistoryCantHaveDuplicate(){
        Task newtask1 = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());
        taskManager.createTask(newtask1);
        taskManager.getTaskById(1);

        newtask1 = new Task("Zadacha po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());
        taskManager.updateTask(newtask1);
        taskManager.getTaskById(1);

        assertEquals(1, taskManager.getHistory().size(), "History != 1 element");
    }


    @Test
    void deleteFromHistoryStarts(){
        Task newtask1 = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());
        taskManager.createTask(newtask1);
        taskManager.getTaskById(1);

        Task newtask2 = new Task("Zadacha po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(15));
        taskManager.createTask(newtask2);
        taskManager.getTaskById(2);

        Task newtask3 = new Task("Zadacha po spisky#3", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(30));
        taskManager.createTask(newtask3);
        taskManager.getTaskById(3);

        List history = taskManager.getHistory();
        taskManager.deleteTask(1);

        //проверить удаление начало
        assertEquals(2, taskManager.getHistory().size(), "History != 2 element");
        assertTrue(history.containsAll(taskManager.getHistory()), "Main history havent other 2 element after delete first");
    }

    @Test
    void deleteFromHistoryCentre(){
        Task newtask1 = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());
        taskManager.createTask(newtask1);
        taskManager.getTaskById(1);

        Task newtask2 = new Task("Zadacha po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(15));
        taskManager.createTask(newtask2);
        taskManager.getTaskById(2);

        Task newtask3 = new Task("Zadacha po spisky#3", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(30));
        taskManager.createTask(newtask3);
        taskManager.getTaskById(3);

        List history = taskManager.getHistory();
        taskManager.deleteTask(2);

        //проверить удаление начало
        assertEquals(2, taskManager.getHistory().size(), "History != 2 element");
        assertTrue(history.containsAll(taskManager.getHistory()), "Main history havent other 2 element after delete first");
    }

    @Test
    void deleteFromHistoryFinish(){
        Task newtask1 = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());
        taskManager.createTask(newtask1);
        taskManager.getTaskById(1);

        Task newtask2 = new Task("Zadacha po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(15));
        taskManager.createTask(newtask2);
        taskManager.getTaskById(2);

        Task newtask3 = new Task("Zadacha po spisky#3", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(30));
        taskManager.createTask(newtask3);
        taskManager.getTaskById(3);

        List history = taskManager.getHistory();
        taskManager.deleteTask(3);

        //проверить удаление начало
        assertEquals(2, taskManager.getHistory().size(), "History != 2 element");
        assertTrue(history.containsAll(taskManager.getHistory()), "Main history havent other 2 element after delete first");
    }



    @Override
    protected InMemoryTaskManager getTaskManger() {
        return new InMemoryTaskManager();
    }
}