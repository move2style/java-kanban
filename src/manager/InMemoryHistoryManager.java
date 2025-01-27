package manager;

import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> historyStorage = new ArrayList<>();
    private final int MAXCOUNTHISTORY = 11;

    @Override
    public void addTask(Task task) {
        if (Objects.isNull(task)) {
            return;
        }

        if (historyStorage.size() >= MAXCOUNTHISTORY) {
            historyStorage.remove(0);
        }
        historyStorage.add(task.getSnapShot());
    }

    @Override
    public List<Task> getHistory() {
        return historyStorage;
    }
}
