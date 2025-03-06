package manager;

import file.ManagerSaveException;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public void save (){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(getHeader());
            for(Map.Entry<Integer, Task> entry : this.taskMap.entrySet()){
                Task task = entry.getValue();
                writer.write(toString(task));
            }
            for(Map.Entry<Integer, Epic> entry : this.epicMap.entrySet()){
                Epic epic = entry.getValue();
                writer.write(toString(epic));
            }
            for(Map.Entry<Integer, Subtask> entry : this.subtaskMap.entrySet()){
                Subtask subtask = entry.getValue();
                writer.write(toString(subtask));
            }
//            writer.newLine();
        } catch (Exception exception) {
            throw new ManagerSaveException("Невозможно работать с файлом.");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
    final FileBackedTaskManager result = new FileBackedTaskManager(file);
        FileReader reader = new FileReader(file, StandardCharsets.UTF_8);
        try (BufferedReader br = new BufferedReader(reader)){
            while (br.ready()) {
                String line = br.readLine();
                if(line.equals("id,type,name,status,description,epic")) {
                    line = br.readLine();
                }
                Task task = fromString(line);
                if (task instanceof Epic){
                    result.createEpic((Epic) task);
                } else if (task instanceof Subtask) {
                    result.createSubtask((Subtask) task);
                } else {
                    result.createTask(task);
                }
            }
        }  catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
        return result;
    }


    public static final String DELIMITER = ",";

   private String getHeader() {
        return "id,type,name,status,description,epic\n";
    }

    private String toString (Task task) {
//        1,TASK,Task1,NEW,Description task1,
        return task.getId() + DELIMITER + task.getType() + DELIMITER + task.getName() + DELIMITER +
                task.getStatus() + DELIMITER + task.getDescription() + DELIMITER + "\n";
    }
    private String toString (Epic epic) {
        return epic.getId() + DELIMITER + epic.getType() + DELIMITER + epic.getName() + DELIMITER +
                epic.getStatus() + DELIMITER + epic.getDescription() + DELIMITER + "\n";
    }
    private String toString (Subtask subtask) {
        //3,SUBTASK,Sub Task2,DONE,Description sub task3,2
        return subtask.getId() + DELIMITER + subtask.getType() + DELIMITER + subtask.getName() + DELIMITER +
                subtask.getStatus() + DELIMITER + subtask.getDescription() + DELIMITER + subtask.getEpicId() + "\n";
    }

    private static Task fromString(String value) {
        String[] splValue = value.split(DELIMITER);
        String  type = splValue[1];
        String name = splValue[2];
        TaskStatus status = TaskStatus.valueOf(splValue[3]);
        String description = splValue[4];
        int epicId = 0;
        if(splValue.length > 5) {
            epicId = Integer.parseInt(splValue[5]);
        }

        if (type.equals("TASK")){
            Task task = new Task(name,description,status);
            return task;
        } else if (type.equals("EPIC")) {
            Epic epic = new Epic(name,description,status);
            return epic;
        } else if (type.equals("SUBTASK")) {
            Subtask subtask = new Subtask(name,description,status,epicId);
            return subtask;
        }
        throw new IllegalArgumentException("Неправильный тип задачи");
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public List getTaskList() {
        return super.getTaskList();
    }

    @Override
    public List getSubtaskList() {
        return super.getSubtaskList();
    }

    @Override
    public List getEpicList() {
        return super.getEpicList();
    }

    @Override
    public Task getTaskById(Integer idTask) {
        return super.getTaskById(idTask);
    }

    @Override
    public Subtask getSubtaskById(Integer idSubtask) {
        return super.getSubtaskById(idSubtask);
    }

    @Override
    public Epic getEpicById(Integer idEpic) {
        return super.getEpicById(idEpic);
    }

    @Override
    public void removeTaskMap() {
        super.removeTaskMap();
        save();
    }

    @Override
    public void removeSubtask() {
        super.removeSubtask();
        save();
    }

    @Override
    public void removeEpicMap() {
        super.removeEpicMap();
        save();
    }

    @Override
    public void deleteTask(Integer idTask) {
        super.deleteTask(idTask);
        save();
    }

    @Override
    public void deleteSubtask(Integer idSubtask) {
        super.deleteSubtask(idSubtask);
        save();
    }

    @Override
    public void deleteEpic(Integer idEpic) {
        super.deleteEpic(idEpic);
        save();
    }

    @Override
    public List getSubtaskListForEpic(Integer idEpic) {
        return super.getSubtaskListForEpic(idEpic);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }
}
