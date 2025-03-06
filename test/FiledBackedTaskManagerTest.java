import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class FiledBackedTaskManagerTest {

    File tmp = File.createTempFile("str", ".tmp");
    TaskManager taskManager = Managers.getDefaultTaskManager(tmp);

    FiledBackedTaskManagerTest() throws IOException {
    }

    @Test
    void shouldSaveAndReloadData() throws IOException {
        Task newtask1 = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW);
        taskManager.createTask(newtask1);
        Task newtask2 = new Task("Zadacha po spisky#2", "opisanie", TaskStatus.NEW);
        taskManager.createTask(newtask2);

        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(tmp);

        assertArrayEquals(taskManager.getTaskList().toArray(new Object[0]), taskManager2.getTaskList().toArray(new Object[0]));
    }

    @Test
    void shouldNullData() throws IOException {
        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(tmp);

        assertArrayEquals(taskManager.getTaskList().toArray(new Object[0]), taskManager2.getTaskList().toArray(new Object[0]));
        assertArrayEquals(taskManager.getEpicList().toArray(new Object[0]), taskManager2.getEpicList().toArray(new Object[0]));
        assertArrayEquals(taskManager.getSubtaskList().toArray(new Object[0]), taskManager2.getSubtaskList().toArray(new Object[0]));
    }


}