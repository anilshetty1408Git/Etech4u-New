package com.engg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.engg.model.Subject;

@Repository
@Transactional
public interface UploadRepositoryNew extends JpaRepository<Subject, Long>, JpaSpecificationExecutor<Subject> {

	@Modifying

	@Query(value = "DELETE FROM Subject s WHERE s.year=:year AND s.orderForDisplay=:orderForDisplay AND s.courseName=:courseName")
	void deleteDataIfAvailable(@Param("year") String year, @Param("orderForDisplay") int orderForDisplay,

			@Param("courseName") String courseName);

	@Modifying

	@Query(value = "SELECT DISTINCT s.courseName FROM Subject s where s.year=:year ORDER BY orderForDisplay")
	List<String> getFirstYearCourseName(String year);

	@Modifying

	@Query(value = "SELECT DISTINCT s.year,s.courseName FROM Subject s where s.year<>:year and s.year<>:maths ORDER BY orderForDisplay")
	List<String> getOtherYearCourseName(String year, String maths);

	@Modifying

	@Query(value = "SELECT s.id,s.topics,s.link,s.subject,s.webLink FROM Subject s WHERE s.courseName=:courseName AND s.year=:year")
	List<Object[]> retriveSubjectExcelData(String courseName, String year);

}
