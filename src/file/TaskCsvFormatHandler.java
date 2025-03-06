package file;

import manager.InMemoryTaskManager;
import manager.Managers;
import manager.TaskManager;
import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskStatus;

import java.io.File;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;

public class TaskCsvFormatHandler {
//
//    public static final String DELIMITER = ",";
//
//    public static String getHeader() {
//        return "id,type,name,status,description,epic\n";
//    }
//
//    public static String toString (Task task) {
////        1,TASK,Task1,NEW,Description task1,
//        return task.getId() + DELIMITER + task.getType() + DELIMITER + task.getName() + DELIMITER +
//                task.getStatus() + DELIMITER + task.getDescription() + DELIMITER + "\n";
//    }
//    public static String toString (Epic epic) {
//        return epic.getId() + DELIMITER + epic.getType() + DELIMITER + epic.getName() + DELIMITER +
//                epic.getStatus() + DELIMITER + epic.getDescription() + DELIMITER + "\n";
//    }
//    public static String toString (Subtask subtask) {
//        //3,SUBTASK,Sub Task2,DONE,Description sub task3,2
//        return subtask.getId() + DELIMITER + subtask.getType() + DELIMITER + subtask.getName() + DELIMITER +
//                subtask.getStatus() + DELIMITER + subtask.getDescription() + DELIMITER + subtask.getEpicId() + "\n";
//    }
//
//    public static Task fromString(String value) {
//        String[] splValue = value.split(DELIMITER);
//        String  type = splValue[1];
//        String name = splValue[2];
//        TaskStatus status = TaskStatus.valueOf(splValue[3]);
//        String description = splValue[4];
//        int epicId = 0;
//        if(splValue.length > 5) {
//            epicId = Integer.parseInt(splValue[5]);
//        }
//
//        if (type.equals("TASK")){
//            Task task = new Task(name,description,status);
//            return task;
//        } else if (type.equals("EPIC")) {
//            Epic epic = new Epic(name,description,status);
//            return epic;
//        } else if (type.equals("SUBTASK")) {
//            Subtask subtask = new Subtask(name,description,status,epicId);
//            return subtask;
//        }
//        throw new IllegalArgumentException("Неправильный тип задачи");
//    }
}
