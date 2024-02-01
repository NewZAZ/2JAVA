package fr.newstaz.istore.model;

import java.util.ArrayList;
import java.util.List;

public class Store {
    private int id;

    private final String name;

    private final List<User> employees = new ArrayList<>();

    private Inventory inventory;

    public Store(String name) {
        this.name = name;
    }

    public Store(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<User> getEmployees() {
        return employees;
    }

    public void addEmployee(User user) {
        employees.add(user);
    }

    public void removeEmployee(User user) {
        employees.remove(user);
    }

    public boolean isEmployee(User user) {
        return employees.contains(user);
    }

    public boolean isEmployee(String email) {
        return employees.stream().anyMatch(user -> user.getEmail().equals(email));
    }

    public void setEmployees(List<User> employees) {
        this.employees.clear();
        this.employees.addAll(employees);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
