package com.engg.restcontroller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.engg.model.AjaxModel;
import com.engg.model.LectureNotes;
import com.engg.model.Subject;
import com.engg.service.UploadService;

@RestController
@RequestMapping
public class AjaxController {

	@Autowired
	private UploadService service;

	@PostMapping(value = "/retriveSubjectExcelData", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, List<Subject>> retriveSubjectExcelData(@RequestBody AjaxModel ajax) {
		List<Object[]> list = new ArrayList<>();
		list = service.retriveSubjectExcelData(ajax.getCourseName(), ajax.getYear());
		Subject subject = null;
		List<Subject> subList = new ArrayList<>();
		Object[] obj = null;
		Map<String, List<Subject>> map = new LinkedHashMap<>();
		for (Object object : list) {
			obj = (Object[]) object;
			subject = new Subject();
			subject.setId((Long) obj[0]);
			subject.setTopics((String) obj[1]);
			subject.setLink((String) obj[2]);
			subject.setSubject((String) obj[3]);
			subject.setWebLink((String) obj[4]);
			subList.add(subject);

		}

		Subject dept = new Subject();
		List<Subject> newList = new ArrayList<>();
		for (Subject departments : subList) {
			dept = new Subject();
			newList = new ArrayList<>();
			dept.setId(departments.getId());
			dept.setTopics(departments.getTopics());
			dept.setLink(departments.getLink());
			dept.setSubject(departments.getSubject());
			dept.setWebLink(departments.getWebLink());
			newList.add(dept);
			if (map.containsKey(departments.getSubject())) {
				newList.addAll(map.get(departments.getSubject()));
				newList.sort(Comparator.comparing(Subject::getId));
				map.put(departments.getSubject(), newList);
			} else {
				newList.sort(Comparator.comparing(Subject::getId));
				map.put(departments.getSubject(), newList);
			}
		}
		return map;
	}

	@GetMapping("/retriveSubjectExcelData/{id}")
	public Optional<Subject> getDepartmentById(@PathVariable("id") long id) {
		return Optional.ofNullable(service.getDepartmentById(id).orElse(null));
	}

	@PostMapping("/checkAjax")
	public String checkAjax(@RequestBody String firstName) {
		return "success";
	}

	@PostMapping(value = "/retriveNotesExcelData", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, List<LectureNotes>> retriveNotesExcelData(@RequestBody AjaxModel ajax) {
		List<LectureNotes> list = new ArrayList<>();
		list = service.retriveNotesExcelData(ajax.getCourseName(), ajax.getYear());
		Map<String, List<LectureNotes>> map = new LinkedHashMap<>();
		LectureNotes dept = new LectureNotes();
		List<LectureNotes> newList = new ArrayList<>();
		for (LectureNotes departments : list) {
			dept = new LectureNotes();
			newList = new ArrayList<>();
			dept.setId(departments.getId());
			dept.setLocation(departments.getLocation());
			newList.add(dept);
			map.put(departments.getTitle(), newList);
		}
		return map;
	}

}
