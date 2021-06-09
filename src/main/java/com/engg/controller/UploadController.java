package com.engg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.engg.model.LectureNotes;
import com.engg.model.Subject;
import com.engg.service.UploadService;

@Controller
public class UploadController {

	@Autowired
	private UploadService uploadService;

	@PostMapping("/upload")
	public String uploadFile(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,
			@ModelAttribute("subject") Subject subject, BindingResult result) {

		// check if file is empty
		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/20NanU14";
		}

		// normalize the file path
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		uploadService.uploadService(file, subject.getYear(), subject.getOrderForDisplay(), subject.getCourseName());

		// return success response
		attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

		return "redirect:/20NanU14";
	}

	@PostMapping("/uploadMaths")
	public String uploadMaths(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,
			@ModelAttribute("subject") Subject subject, BindingResult result) {

		// check if file is empty
		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/20NanU14";
		}

		// normalize the file path
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		uploadService.uploadService(file, subject.getYear(), subject.getOrderForDisplay(), subject.getCourseName());

		// return success response
		attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

		return "redirect:/uploadMaths";
	}
	
	@PostMapping("/uploadNotes")
	public String uploadNotes(@RequestParam("file") MultipartFile file, RedirectAttributes attributes,
			@ModelAttribute("LectureNotes") LectureNotes notes, BindingResult result) {

		// check if file is empty
		if (file.isEmpty()) {
			attributes.addFlashAttribute("message", "Please select a file to upload.");
			return "redirect:/20NanU14";
		}

		// normalize the file path
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		uploadService.uploadNotesService(file);

		// return success response
		attributes.addFlashAttribute("message", "You successfully uploaded " + fileName + '!');

		return "redirect:/uploadNotes";
	}
	
}
