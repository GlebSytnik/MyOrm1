package org.example;

public class Entity {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;

    public Entity() {

    }

    public Entity(Integer id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

}
