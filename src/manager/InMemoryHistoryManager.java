package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> nodeMap = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public void addTask(Task task) {
        if (Objects.isNull(task)) {
            return;
        }

        if (nodeMap.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node node = nodeMap.get(id);
        if (node != null) {
            removeNode(node);
        }
    }

    @Override
    public List<Task> getHistory() {

        List<Task> result = new ArrayList<>();
        Node node = head;
        while (Objects.nonNull(node)) {
            result.add(node.getTask());
            node = node.getNext();
        }
        return result;
    }

    public void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;

        nodeMap.put(task.getId(), newNode);
    }

    public void removeNode(Node node) {
        if (node == null) {
            return;
        }

        Node nextNode = node.next;
        Node prevNode = node.prev;
        int idRemove = -1;
//если пришёл узел единственный в списке
        if (nodeMap.size() == 1) {
            idRemove = node.task.getId();
            node.task = null;
            node.next = null;
            node.prev = null;
            tail = null;
            head = null;
            nodeMap.remove(idRemove);
            return;
        }

        // Node(Node prev, Task task, Node next)
        //если пришёл узел который не единственный в списке
        if (node.prev == null) {
            idRemove = node.task.getId();
            node.task = null;
            node.next = null;
            nextNode.prev = null;
        } else if (node.next == null) {
            idRemove = node.task.getId();
            node.task = null;
            node.prev = null;
            prevNode.next = null;
            tail = prevNode;
        } else {
            nextNode.prev = node.prev;
            node.prev = null;

            prevNode.next = node.next;
            node.next = null;
            idRemove = node.task.getId();
            node.task = null;
        }

        nodeMap.remove(idRemove);
    }

    public static class Node {

        Task task;
        Node next;
        Node prev;

        public Task getTask() {
            return task;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return prev;
        }

        public Node(Node prev, Task task, Node next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }

}
