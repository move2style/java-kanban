package manager;

import myexeption.ManagerSaveException;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String FILE_NAME = "java-kanban\\src\\resources\\storage.csv";
    private File file;
    static int ids = 0;

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public FileBackedTaskManager() {
    }

    public void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(getHeader());

            String epicString = epicMap.values().stream()
                    .map(this::toString)
                    .collect(Collectors.joining());
            String subTaskString = subtaskMap.values().stream()
                    .map(this::toString)
                    .collect(Collectors.joining());
            String taskString = taskMap.values().stream()
                    .map(this::toString)
                    .collect(Collectors.joining());

            writer.write(taskString + epicString + subTaskString);
        } catch (Exception exception) {
            throw new ManagerSaveException("Невозможно работать с файлом.");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        if (!file.exists()) {
            createFile(FILE_NAME);
        }

        final FileBackedTaskManager result = new FileBackedTaskManager(file);
        FileReader reader = new FileReader(file, StandardCharsets.UTF_8);
        try (BufferedReader br = new BufferedReader(reader)) {
            br.readLine();

            br.lines()
                    .map(line -> fromString(line))
                    .filter(task -> task != null)
                    .forEach(task -> {
                        if (task instanceof Epic) {
                            result.updateEpicLoadFromFile((Epic) task);
                            ids++;
                        } else if (task instanceof Subtask) {
                            result.updateSubtaskLoadFromFile((Subtask) task);
                            ids++;
                        } else {
                            result.updateTaskLoadFromFile(task);
                            ids++;
                        }
                    });
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        }
        result.setId(ids);
        return result;
    }


    public static final String DELIMITER = ",";

    private String getHeader() {
        return "id,type,name,status,description,duration,start,epic\n";
    }

    private String toString(Task task) {
        return task.getId() + DELIMITER
                + task.getType() + DELIMITER
                + task.getName() + DELIMITER
                + task.getStatus() + DELIMITER
                + task.getDescription() + DELIMITER
                + task.getDuration().toMillis() + DELIMITER
                + task.getStartTime() + DELIMITER
                + "\n";
    }

    private String toString(Epic epic) {
        return epic.getId() + DELIMITER
                + epic.getType() + DELIMITER
                + epic.getName() + DELIMITER
                + epic.getStatus() + DELIMITER
                + epic.getDescription() + DELIMITER
                + epic.getDuration().toMillis() + DELIMITER
                + epic.getStartTime() + DELIMITER
                + "\n";
    }

    private String toString(Subtask subtask) {
        return subtask.getId() + DELIMITER
                + subtask.getType() + DELIMITER
                + subtask.getName() + DELIMITER
                + subtask.getStatus() + DELIMITER
                + subtask.getDescription() + DELIMITER
                + subtask.getDuration().toMillis() + DELIMITER
                + subtask.getStartTime() + DELIMITER
                + subtask.getEpicId() + "\n";
    }

    private static Task fromString(String value) {
        String[] splValue = value.split(DELIMITER);
        int id = Integer.parseInt(splValue[0]);
        String type = splValue[1];
        String name = splValue[2];
        TaskStatus status = TaskStatus.valueOf(splValue[3]);
        String description = splValue[4];
        int epicId = 0;
        Duration duration = Duration.of(Long.parseLong(splValue[5]), ChronoUnit.MILLIS);
        LocalDateTime localDateTime = null;

        if (splValue[6] != null && !splValue[6].equals("null")) {
            localDateTime = LocalDateTime.parse(splValue[6]);
        }

        if (splValue.length > 7) {
            epicId = Integer.parseInt(splValue[7]);
        }

        if (type.equals("TASK")) {
            if (localDateTime != null) {
                Task task = new Task(name, description, status, id, duration, localDateTime);
                return task;
            }
            Task task = new Task(name, description, status, id, duration);
            return task;
        } else if (type.equals("EPIC")) {
            if (localDateTime != null) {
                Epic epic = new Epic(name, description, status, id, duration, localDateTime);
                return epic;
            }
            Epic epic = new Epic(name, description, status, id, duration);
            return epic;
        } else if (type.equals("SUBTASK")) {
            if (localDateTime != null) {
                Subtask subtask = new Subtask(name, description, status, epicId, id, duration, localDateTime);
                return subtask;
            }
            Subtask subtask = new Subtask(name, description, status, epicId, id, duration);
            return subtask;
        }
        throw new IllegalArgumentException("Неправильный тип задачи");
    }

    private static File createFile(String fileName) {
        try {
            return Files.createFile(Paths.get(fileName)).toFile();
        } catch (IOException exception) {
            System.out.println("Файл для файлового хранилища не создан: " + exception.getMessage());
        }
        throw new UnsupportedOperationException("Файл не создан");
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
}
