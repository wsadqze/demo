package tw.com.phctw.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import tw.com.phctw.entity.Member;
import tw.com.phctw.service.MemberService;

@RestController
public class MemberController {

	@Autowired
	private MemberService memberService;
	@GetMapping(value = "/index")
	public ModelAndView index(@RequestParam(value = "index", required = false, defaultValue = "default") String a) {
		ModelAndView mv = new ModelAndView("register");
		return mv;
	}
	
	@GetMapping(value="/send")
	public ModelAndView send() {
		ModelAndView mv = new ModelAndView("sendPassword");
		return mv;
	}

	@GetMapping(value = "/regi")
	public ModelAndView manage(@RequestParam(value = "index", required = false, defaultValue = "default") String a) {
		ModelAndView mv = new ModelAndView("register");
		return mv;
	}

	@GetMapping(value = "/log")
	public ModelAndView login(@RequestParam(value = "index", required = false, defaultValue = "default") String a) {
		ModelAndView mv = new ModelAndView("login");
		return mv;
	}

	@PostMapping(value = "/check")
	public String yourUrl(@PathVariable(name = "musername") String musername) {

		if (memberService.getByUsername(musername) == false) {
			return "no";
		}
		return "yes";
	}

	@PostMapping("/register")
	public ModelAndView register(Member member, ModelMap map) {
		ModelAndView mv = new ModelAndView("register");
		ModelAndView lo = new ModelAndView("login");
		List<String> errorMsgs = new LinkedList<String>();

		map.addAttribute("errorMsgs", errorMsgs);

		if (member.getMusername() == null || (member.getMusername().trim()).length() == 0) {
			errorMsgs.add("請輸入帳號");
			return mv;
		}
		if (member.getMpassword() == null || (member.getMpassword().trim()).length() == 0) {
			errorMsgs.add("請輸入密碼");
			return mv;
		}
		if (member.getMname() == null || (member.getMname().trim()).length() == 0) {
			errorMsgs.add("請輸入姓名");
			return mv;
		}
		if (member.getMsex() == null) {
			errorMsgs.add("請選擇性別");
			return mv;
		}
		if (member.getMbirth() == null) {
			errorMsgs.add("請選擇生日");
			return mv;
		}
		if (member.getMemail() == null || (member.getMemail().trim()).length() == 0) {
			errorMsgs.add("請輸入信箱");
			return mv;
		}
		if (!memberService.mail(member.getMemail())) {
			errorMsgs.add("信箱格式不正確");
			return mv;
		}

		if (member.getMid() == null || (member.getMid().trim()).length() < 10) {
			errorMsgs.add("身分證格式錯誤");
			return mv;
		}
		if (!memberService.idCheck(member.getMid())) {
			errorMsgs.add("身分證格式錯誤");
			return mv;
		}

		if (memberService.getByUsername(member.getMusername()) == true) {
			return mv;
		}
		memberService.addMember(member);
		return lo;
	}

	@PostMapping("/login")
	public ModelAndView login(Member member, ModelMap map) {
		ModelAndView mv = new ModelAndView("login");
		ModelAndView lo = new ModelAndView("info");
		List<String> errorMsgs = new LinkedList<String>();
		List<Member> mem = new ArrayList<Member>();
		
		map.addAttribute("errorMsgs", errorMsgs);

		if (member.getMusername() == null || (member.getMusername().trim()).length() == 0) {
			errorMsgs.add("請輸入帳號");
			return mv;
		}
		if (member.getMpassword() == null || (member.getMpassword().trim()).length() == 0) {
			errorMsgs.add("請輸入密碼");
			return mv;
		}
		if (memberService.checkUserAndPassword(member)) {

			member = memberService.getOneMember(member.getMusername());
			mem.add(member);
			map.addAttribute("member", mem);
		
			return lo;
		} else
			return mv;
	}
	
	
	
	@PostMapping("/sendpass")
	public ModelAndView pw(@PathVariable("musername") String musername, ModelMap map) {
		
		ModelAndView mv = new ModelAndView("login");
		ModelAndView pv = new ModelAndView("sendPassword");
	
		if(memberService.getByUsername(musername)) {
			String pw = memberService.changePw(musername);
			memberService.sendMail(pw);
			return mv;	
		}
		return pv;
		
	}
	
}
