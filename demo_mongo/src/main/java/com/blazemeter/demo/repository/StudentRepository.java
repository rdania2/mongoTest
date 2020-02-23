package com.blazemeter.demo.repository;

import com.blazemeter.demo.document.Student;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentRepository extends MongoRepository<Student, String> {

	Optional<Student> findStudentByStudentId(ObjectId studentId);

	Optional<Student> findStudentByEmail(String email);

}
