package org.example.entity;

public class Entity extends OrmId {


    private String firstName;
    private String lastName;
    private String email;


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }


    public Entity(String firstName, String lastName, String email) {
        super(null);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Entity() {
        super(null);

    }
}
