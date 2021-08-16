package com.engg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.engg.model.LectureNotes;

public interface NotesRepository extends JpaRepository<LectureNotes, Long> {

	@Modifying
	@Query(value = "SELECT DISTINCT s.year,s.department FROM LectureNotes s ")
	List<String> getAllYearAndDeptNotes();

	@Modifying
	@Query(value = "SELECT s FROM LectureNotes s WHERE s.department=:courseName AND s.year=:year")
	List<LectureNotes> retriveSubjectExcelData(String courseName, String year);

}
