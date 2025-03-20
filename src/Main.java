import manager.Managers;
import manager.TaskManager;
import task.Epic;

import java.io.File;
import java.io.IOException;

public class Main {
    private static final String FILE_NAME = "java-kanban\\src\\resources\\storage.csv";

    public static void main(String[] args) throws IOException {
        File file = new File(FILE_NAME);
        TaskManager taskManager = Managers.getDefaultTaskManager(file);

//        Task newtask1 = new Task("Zadacha po spisky#1", "opisanie", TaskStatus.NEW,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now());
//        taskManager.createTask(newtask1);
//        Task newtask2 = new Task("Zadacha po spisky#2", "opisanie", TaskStatus.NEW,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(106));
//        taskManager.createTask(newtask2);
//        Task newTask11 = new Task("Zadacha po spisky#1UP", "opisanie", TaskStatus.IN_PROGRESS, 1,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(106));
//        taskManager.updateTask(newTask11);
//        Epic newepic1 = new Epic("Epic po spisky#1", "opisanie", TaskStatus.NEW,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(105));
//        taskManager.createEpic(newepic1);
//        Epic newepic11 = new Epic("Epic po spisky#1", "opisanie", TaskStatus.IN_PROGRESS, 3,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(105));
//        taskManager.updateEpic(newepic11);
//        Epic newepic12 = new Epic("Epic po spisky#1_1", "opisanie", TaskStatus.NEW, 3,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(105));
//        taskManager.updateEpic(newepic12);
//        Epic newepic2 = new Epic("Epic po spisky#2", "opisanie", TaskStatus.NEW,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(121));
//        taskManager.createEpic(newepic2);
//        Subtask newsubtask1 = new Subtask("Subtask #1", "opisanie", TaskStatus.NEW, newepic1.getId(),
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(122));
//        Subtask newsubtask2 = new Subtask("Subtask #2", "opisanie", TaskStatus.NEW, newepic1.getId(),
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(150));
//        Subtask newsubtask3 = new Subtask("Subtask #3", "opisanie", TaskStatus.NEW, newepic1.getId(),
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(140));
//        taskManager.createSubtask(newsubtask1);
//        taskManager.createSubtask(newsubtask2);
//        taskManager.createSubtask(newsubtask3);
//        Subtask newsubtask4 = new Subtask("Subtask #4", "opisanie", TaskStatus.NEW, newepic2.getId(),
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(110));
//        Subtask newsubtask5 = new Subtask("Subtask #5", "opisanie", TaskStatus.NEW, newepic2.getId(),
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(100));
//        taskManager.createSubtask(newsubtask4);
//        taskManager.createSubtask(newsubtask5);
//        Subtask newsubtask51 = new Subtask("Subtask #5_1", "opisanie 5_1", TaskStatus.NEW, newepic2.getId(), 9,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(100));
//        taskManager.updateSubtask(newsubtask51);
//        Subtask newsubtask52 = new Subtask("Subtask #51", "opisanie 5_1", TaskStatus.DONE, newepic2.getId(), 9,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(100));
//        taskManager.updateSubtask(newsubtask52);
//        Subtask newsubtask41 = new Subtask("Subtask #52", "opisanie 5_1", TaskStatus.DONE, newepic2.getId(), 8,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(100));
//        taskManager.updateSubtask(newsubtask41);
//        newsubtask52 = new Subtask("Subtask #53", "opisanie 5_1", TaskStatus.NEW, newepic2.getId(), 9,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(100));
//        taskManager.updateSubtask(newsubtask52);
//        newsubtask41 = new Subtask("Subtask #54", "opisanie 5_1", TaskStatus.NEW, newepic2.getId(), 8,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(100));
//        taskManager.updateSubtask(newsubtask41);
//        newsubtask41 = new Subtask("Subtask #55", "opisanie 5_1", TaskStatus.DONE, newepic2.getId(), 8,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(100));
//        taskManager.updateSubtask(newsubtask41);
//        newsubtask51 = new Subtask("Subtask #56", "opisanie 5_1", TaskStatus.DONE, newepic2.getId(), 9,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(100));
//        taskManager.updateSubtask(newsubtask51);
//
//        newsubtask1 = new Subtask("Subtask #1", "opisanie", TaskStatus.DONE, newepic1.getId(), 5,
//                Duration.of(10, ChronoUnit.MINUTES),
//                LocalDateTime.now().minusMinutes(200));
//        taskManager.updateSubtask(newsubtask1);
//
//        Task newtaskNull = new Task("Zadacha po spisky null", "opisanie null", TaskStatus.NEW,
//                Duration.of(10, ChronoUnit.MINUTES));
//        taskManager.createTask(newtaskNull);
//        Task newtaskNull2 = new Task("Zadacha po spisky null2", "opisanie null2", TaskStatus.NEW,
//                Duration.of(10, ChronoUnit.MINUTES));
//        taskManager.createTask(newtaskNull2);
//        Task newtaskNull2 = new Task("Zadacha po spisky null3", "opisanie null3", TaskStatus.NEW, 51,
//                Duration.of(10, ChronoUnit.MINUTES));
//        taskManager.updateTask(newtaskNull2);

//        taskManager.getEpicById(4);
//        taskManager.getEpicById(4);
//        taskManager.getTaskById(1);
//        taskManager.getSubtaskById(8);
//        taskManager.getTaskById(1);
//        taskManager.getTaskById(1);
//        taskManager.getSubtaskListForEpic(2);

        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {

        System.out.println("Задачи:");
        for (Object task : manager.getTaskList()) {
            System.out.println(task);
        }
        System.out.println("Подзадачи:");
        for (Object subtask : manager.getSubtaskList()) {
            System.out.println(subtask);
        }
        System.out.println("Эпики:");
        for (Object epic : manager.getEpicList()) {
            System.out.println(epic);
            Epic epic1 = (Epic) epic;

            for (Object task : manager.getSubtaskListForEpic(epic1.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("История:");
        for (Object task : manager.getHistory()) {
            System.out.println(task);
        }
        System.out.println("\nPrioritizedTasks:");
        for (Object task : manager.getPrioritizedTasks()) {
            System.out.println(task);
        }
    }
}