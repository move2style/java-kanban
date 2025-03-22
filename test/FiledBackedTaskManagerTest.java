import manager.FileBackedTaskManager;
import manager.Managers;
import manager.TaskManager;
import myexeption.ManagerSaveException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import task.Task;
import task.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertThrows;

class FiledBackedTaskManagerTest<FiledBackedTaskManager extends TaskManager> extends TaskManagerTest<FileBackedTaskManager> {

    File tmp = File.createTempFile("str", ".tmp");
    TaskManager taskManager = Managers.getDefaultTaskManager(tmp);
    private Assertions Assert;

    FiledBackedTaskManagerTest() throws IOException {
    }

    @Test
    public void testException() {
        assertThrows(ManagerSaveException.class,() -> new FileBackedTaskManager().save());
    }


    @Test
    void shouldSaveAndReloadData() throws IOException {
        Task newtask1 = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now());
        taskManager.createTask(newtask1);
        Task newtask2 = new Task("Zadacha po spisky#2", "opisanie", TaskStatus.NEW,
                Duration.of(10, ChronoUnit.MINUTES),
                LocalDateTime.now().minusMinutes(15));
        taskManager.createTask(newtask2);

        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(tmp);

        Assert.assertTrue(taskManager.getTaskList().containsAll(taskManager2.getTaskList())
                && taskManager2.getTaskList().containsAll(taskManager.getTaskList()));
    }

    @Test
    void savingAndLoadingAnEmptyFile() throws IOException {
        FileBackedTaskManager taskManager2 = FileBackedTaskManager.loadFromFile(tmp);

        Assert.assertTrue(taskManager.getTaskList().containsAll(taskManager2.getTaskList())
                && taskManager2.getTaskList().containsAll(taskManager.getTaskList()));
    }

    @Override
    protected FileBackedTaskManager getTaskManger() {
        return new FileBackedTaskManager();
    }
}