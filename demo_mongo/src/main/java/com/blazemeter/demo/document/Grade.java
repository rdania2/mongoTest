package com.blazemeter.demo.document;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Document
public class Grade implements Serializable {

    @Id
    private ObjectId gradeId = new ObjectId();

    private Long grade;

    @Field("student")
    @DBRef
    private Student student;
    @Field("course")
    @DBRef
    private Course course;

    public Long getGrade() {
        return grade;
    }

    public void setGrade(Long grade) {
        this.grade = grade;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public ObjectId getGradeId() {
        return gradeId;
    }

    public void setGradeId(ObjectId gradeId) {
        this.gradeId = gradeId;
    }
}
