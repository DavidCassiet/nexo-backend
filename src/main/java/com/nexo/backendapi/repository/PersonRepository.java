package com.nexo.backendapi.repository;

import com.nexo.backendapi.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByDni(Long dni);

    @Query("SELECT p FROM Person p WHERE (:dni IS NULL OR CAST(p.dni AS string) LIKE %:dni%) " +
            "AND (:firstName IS NULL OR p.firstName LIKE %:firstName%) " +
            "AND (:age IS NULL OR p.age = :age)")
    List<Person> findPeopleByFilter(
            @Param("dni") String dni,
            @Param("firstName") String firstName,
            @Param("age") Integer age);
}
