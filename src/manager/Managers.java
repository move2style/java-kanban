package manager;

import java.io.File;
import java.io.IOException;

public final class Managers {

    private Managers() {

    }

    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefaultTaskManager(File file) throws IOException {
        return FileBackedTaskManager.loadFromFile(file);
    }
}
