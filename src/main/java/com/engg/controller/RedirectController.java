package com.engg.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.engg.model.IpAddress;
import com.engg.model.Subject;
import com.engg.service.UploadService;

@Controller
public class RedirectController {

	@Autowired
	private UploadService service;

	private static final String[] HEADERS_TO_TRY = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
			"HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
			"HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

	private static String getClientIpAddress(HttpServletRequest request) {

		for (String header : HEADERS_TO_TRY) {
			String ip = request.getHeader(header);
			if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
				return ip;
			}
		}

		return "Hello";
	}

	@GetMapping("/")
	public String landingPage(ModelAndView model, HttpServletRequest request) {

		InetAddress ip;
		IpAddress address = new IpAddress();
		try {
			ip = InetAddress.getLocalHost();
			address.setIpAddress(String.valueOf(ip));
			address.setDate(new Date());
			service.saveIpAddress(address);

		} catch (UnknownHostException e) {

			e.printStackTrace();
		}

		String s = getClientIpAddress(request);

		return "redirect:/home";
	}

	@GetMapping("/20NanU14")
	public String admin(Model model) {
		model.addAttribute("subject", new Subject());
		return "admin";
	}

	@GetMapping("/getLoggedIPs")
	public String getLoggedIPs(Model model) {
		List<IpAddress> list = new ArrayList<>();
		list = service.getIpAddressDetails();
		Collections.reverse(list);
		long count = list.stream().count();
		model.addAttribute("count", count);
		model.addAttribute("list", list.stream().limit(50).collect(Collectors.toList()));
		return "ipAddress";
	}

	@GetMapping("/home")
	public String home(ModelAndView model, HttpServletRequest request) {
		return "home";
	}

	@GetMapping("/smartPresentation")
	public String smartPresentation() {
		return "smartPresentation";
	}

	@GetMapping("/signIn")
	public String signIn() {
		return "signIn";
	}

	@GetMapping("/civilEngineering")
	public String civilDepartment(Model model) {
		List<String> enggList = service.getOtherYearCourseName("1", "Maths");

		Map<Integer, List<String>> map = new TreeMap<>();
		List<String> list = new ArrayList<>();
//		enggList.stream().map(s -> s.split(",")[0]).forEach(map.put(s, value));

		for (String string : enggList) {
			if (map.containsKey(Integer.valueOf(string.split(",")[0]))) {
				list = map.get(Integer.valueOf(string.split(",")[0]));
				list.add(string.split(",")[1]);
				map.put(Integer.valueOf(string.split(",")[0]), list);
			} else {
				list = new ArrayList<>();
				list.add(string.split(",")[1]);
				map.put(Integer.valueOf(string.split(",")[0]), list);
			}
		}

		model.addAttribute("courseList", map);

		return "civilEngg";
	}

	@GetMapping("/header")
	public String header() {
		return "header";
	}

	@GetMapping("/video")
	public String getVideo() {
		return "video";
	}

	@GetMapping("/firstYear")
	public String firstYear(Model model) {
		List<String> firstYearList = service.getFirstYearCourseName("1");
		model.addAttribute("courseList", firstYearList);
		return "firstYear";
	}

	@GetMapping("/index")
	public String showIndex() {
		return "index";
	}

	@GetMapping("/mathematics")
	public String mathematics(Model model) {
		List<String> firstYearList = service.getFirstYearCourseName("Maths");
		model.addAttribute("courseList", firstYearList);
		return "maths";
	}

	@GetMapping("/uploadMaths")
	public String uploadMaths(Model model) {
		model.addAttribute("subject", new Subject());
		return "uploadMaths";
	}

	@GetMapping("/pdfDisplayDemo")
	public String displayPDF() {
		return "PDFDemo";
	}

	@GetMapping("/aboutUs")
	public String AboutUs() {
		return "aboutUs";
	}

	@GetMapping("/lectureNotes")
	public String getLetcureNotes(Model model) {
//		List<String> firstYearList = service.getFirstYearCourseName("Maths");
//		model.addAttribute("courseList", firstYearList);

		List<String> enggList = service.getAllYearAndDeptNotes();

		Map<String, List<String>> map = new TreeMap<>();
		List<String> list = new ArrayList<>();
//		enggList.stream().map(s -> s.split(",")[0]).forEach(map.put(s, value));

		for (String string : enggList) {
			if (map.containsKey(string.split(",")[0])) {
				list = map.get(string.split(",")[0]);
				list.add(string.split(",")[1]);
				map.put(string.split(",")[0], list);
			} else {
				list = new ArrayList<>();
				list.add(string.split(",")[1]);
				map.put(string.split(",")[0], list);
			}
		}

		model.addAttribute("courseList", map);

		return "lectureNotes";
	}

	@GetMapping("/questBank")
	public String questBank() {
		return "questBank";
	}

	@GetMapping("/internship")
	public String internship() {
		return "internship";
	}

	@GetMapping("/uploadNotes")
	public String uploadNotes() {
		return "uploadNotes";
	}
}
