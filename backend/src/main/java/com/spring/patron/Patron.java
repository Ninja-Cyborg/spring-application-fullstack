package com.spring.patron;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "patron",
        uniqueConstraints = {
            @UniqueConstraint(
                name = "patron_email_uniq",
                columnNames = "email")
        })
public class Patron {
    @Id
    @SequenceGenerator(name ="patron_id_seq",
                        sequenceName = "patron_id_seq",
                        allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "patron_id_seq")
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private int age;

    public Patron(){
    }

    public Patron(Integer id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public Patron(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Patron{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patron patron = (Patron) o;
        return Objects.equals(id, patron.id) && age == patron.age && Objects.equals(name, patron.name) && Objects.equals(email, patron.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
