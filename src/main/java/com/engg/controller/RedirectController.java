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

	@GetMapping("/")
	public String landingPage(ModelAndView model, HttpServletRequest request) {
		InetAddress ip;
		try {
			ip = InetAddress.getLocalHost();
			IpAddress address = new IpAddress();
			address.setIpAddress(String.valueOf(ip));
			address.setDate(new Date());
			service.saveIpAddress(address);

		} catch (UnknownHostException e) {

			e.printStackTrace();
		}

		return "redirect:/home";
	}

	@GetMapping("/admin")
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

	@GetMapping("/aboutUs")
	public String aboutUs() {
		return "aboutUs";
	}

	@GetMapping("/signIn")
	public String signIn() {
		return "signIn";
	}

	@GetMapping("/civilEngineering")
	public String civilDepartment(Model model) {
		List<String> enggList = service.getOtherYearCourseName("1");

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

}
