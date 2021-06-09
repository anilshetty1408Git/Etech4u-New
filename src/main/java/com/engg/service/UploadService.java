package com.engg.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.engg.model.IpAddress;
import com.engg.model.LectureNotes;
import com.engg.model.Subject;

public interface UploadService {
	public void uploadService(MultipartFile file, String year, int displayOrder, String courseName);

	public List<Subject> getDepartmentName();

	public List<Object[]> retriveSubjectExcelData(String courseName, String year);

	public Optional<Subject> getDepartmentById(Long id);

	public List<String> getFirstYearCourseName(String string);

	public List<String> getOtherYearCourseName(String string, String maths);

	public void saveIpAddress(IpAddress address);

	public List<IpAddress> getIpAddressDetails();

	public void uploadNotesService(MultipartFile file);

	public List<String> getAllYearAndDeptNotes();

	public List<LectureNotes> retriveNotesExcelData(String courseName, String year);
	
//	void save(IpAddress address);

}
