package com.engg.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.engg.model.IpAddress;
import com.engg.model.LectureNotes;
import com.engg.model.Subject;
import com.engg.repository.IpRepository;
import com.engg.repository.NotesRepository;
import com.engg.repository.UploadRepositoryNew;
import com.engg.service.UploadService;

@Service
public class UploadServiceImpl implements UploadService {

	@Autowired
	private UploadRepositoryNew repository;

	@Autowired
	private IpRepository ipRepository;

	@Autowired
	private NotesRepository noteRepository;

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	static String[] HEADERs = { "Id", "Title", "Description", "Published" };
	static String SHEET = "Departments";

	@Override
	public void uploadService(MultipartFile file, String year, int orderForDisplay, String courseName) {

		try {
			repository.deleteDataIfAvailable(year, orderForDisplay, courseName);
			List<Subject> dept = excelTodepartments(file.getInputStream(), year, orderForDisplay, courseName);
			repository.saveAll(dept);
		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}

	}

	private List<Subject> excelTodepartments(InputStream inputStream, String year, int displayOrder,
			String courseName) {
		List<Subject> deptList = new LinkedList<>();
		int k = 0;
		try {
			XSSFWorkbook wb = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row;
			FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
			for (int j = 2; j < sheet.getPhysicalNumberOfRows() - 1; j++) {
				Subject dept = new Subject();
				dept.setYear(year);
				dept.setOrderForDisplay(displayOrder);
				dept.setCourseName(courseName);

				row = sheet.getRow(j);
				k = 0;
				dept.setDepartmentName(wb.getSheetAt(0).getSheetName());
				System.out.println("1"+row.getCell((short) k));
				if (org.apache.commons.lang3.StringUtils.isNotBlank(row.getCell((short) k).getStringCellValue())) {
					dept.setSubject(row.getCell((short) k).getStringCellValue());
				}
				k++;
				System.out.println("2"+row.getCell((short) k));
				if (org.apache.commons.lang3.StringUtils.isNotBlank(row.getCell((short) k).getStringCellValue())) {
					dept.setTopics(row.getCell((short) k).getStringCellValue());
				}
				k = 7;
				System.out.println("3"+row.getCell((short) k));
				if (org.apache.commons.lang3.StringUtils.isNotBlank(row.getCell((short) k).getStringCellValue())) {
					dept.setLink(evaluator.evaluate(row.getCell((short) k)).getStringValue());
				}
				k++;
				System.out.println("4"+row.getCell((short) k));
				if (org.apache.commons.lang3.StringUtils.isNotBlank(row.getCell((short) k).getStringCellValue())) {
					dept.setWebLink(row.getCell((short) k).getStringCellValue());
				}

				if (org.apache.commons.lang3.StringUtils.isNotEmpty(dept.getSubject())) {
					dept.setLastUpdatedBy("anil");
					dept.setLastUpdatedOn(LocalDateTime.now());
					deptList.add(dept);
				}
			}
			wb.close();

		} catch (IOException e) {
			e.printStackTrace();

		}
		return deptList;
	}

	@Override
	public List<Subject> getDepartmentName() {
		return repository.findAll();
	}

	@Override
	public List<Object[]> retriveSubjectExcelData(String courseName, String year) {
		return repository.retriveSubjectExcelData(courseName, year);
	}

	@Override
	public Optional<Subject> getDepartmentById(Long id) {
		return repository.findById(id);
	}

	@Override
	public List<String> getFirstYearCourseName(String year) {
		return repository.getFirstYearCourseName(year);
	}

	@Override
	public List<String> getOtherYearCourseName(String year, String maths) {
		return repository.getOtherYearCourseName(year, maths);
	}

	@Override
	public void saveIpAddress(IpAddress address) {
		ipRepository.save(address);
	}

	@Override
	public List<IpAddress> getIpAddressDetails() {
		return ipRepository.findAll();
	}

	@Override
	public void uploadNotesService(MultipartFile file) {
		try {
			List<LectureNotes> notes = excelToNotes(file.getInputStream());
			noteRepository.saveAll(notes);
		} catch (IOException e) {
			throw new RuntimeException("fail to store excel data: " + e.getMessage());
		}
	}

	private List<LectureNotes> excelToNotes(InputStream inputStream) {
		List<LectureNotes> notesList = new LinkedList<>();
		int k = 0;
		try {
			XSSFWorkbook wb = new XSSFWorkbook(inputStream);
			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row;
			for (int j = 2; j < sheet.getPhysicalNumberOfRows() - 1; j++) {
				LectureNotes notes = new LectureNotes();
				row = sheet.getRow(j);
				k = 0;
				if (org.apache.commons.lang3.StringUtils.isNotBlank(row.getCell((short) k).getStringCellValue())) {
					notes.setYear(row.getCell((short) k).getStringCellValue());
				}
				k++;
				if (org.apache.commons.lang3.StringUtils.isNotBlank(row.getCell((short) k).getStringCellValue())) {
					notes.setDepartment(row.getCell((short) k).getStringCellValue());
				}
				k++;

				if (org.apache.commons.lang3.StringUtils.isNotBlank(row.getCell((short) k).getStringCellValue())) {
					notes.setTitle(row.getCell((short) k).getStringCellValue());

				}
				k++;

				if (org.apache.commons.lang3.StringUtils.isNotBlank(row.getCell((short) k).getStringCellValue())) {
					notes.setLocation(row.getCell((short) k).getStringCellValue());
				}
				notes.setLastUpdatedOn(LocalDateTime.now());
				notesList.add(notes);
			}

			wb.close();

		} catch (IOException e) {
			e.printStackTrace();

		}

		return notesList;
	}

	@Override
	public List<String> getAllYearAndDeptNotes() {
		return noteRepository.getAllYearAndDeptNotes();
	}

	@Override
	public List<LectureNotes> retriveNotesExcelData(String courseName, String year) {
		return noteRepository.retriveSubjectExcelData(courseName, year);
	}

}
