package manager;

import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager{
    private final List<Task> historyStorage = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (Objects.isNull(task)){
            return;
        }
    historyStorage.add(task.getSnapShot());
        if(historyStorage.size() >= 11){
            historyStorage.remove(0);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyStorage;
    }
}
