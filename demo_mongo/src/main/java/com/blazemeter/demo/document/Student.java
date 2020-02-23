package com.blazemeter.demo.document;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.io.Serializable;

@Document
public class Student implements Serializable {

    @Id
    private ObjectId studentId = new ObjectId();;
    private String firstName;
    private String lastName;
    @Indexed(unique = true)
    private String email;

    public ObjectId getStudentId() {
        return studentId;
    }

    public void setStudentId(ObjectId studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
