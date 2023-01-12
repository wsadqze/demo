package tw.com.phctw.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.com.phctw.entity.Member;
import tw.com.phctw.service.MemberService;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class MemberController {

	@Autowired
	private MemberService memberService;
	@Autowired
	Member member;

	@RequestMapping(value = "/")
	public String index() {
		return "<input type=\"submit\" class=\"btn btn-outline-danger\" value=\"測試\">";
	}

	@PostMapping("/login")
	public Member login(@RequestBody Member member) throws JsonProcessingException {
//		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println("musername: " + member.getMusername());
		System.out.println("mpassword: " + member.getMpassword());

		if (member.getMusername() == null || (member.getMusername().trim()).length() == 0) {
			member.setSuccess(false);
			member.setMessage("請輸入帳號");
			System.out.println(member.getMessage());
			return member;
		}
		if (member.getMpassword() == null || (member.getMpassword().trim()).length() == 0) {
			member.setSuccess(false);
			member.setMessage("請輸入密碼");
			System.out.println(member.getMessage());
			return member;
		}
		if (memberService.checkUserAndPassword(member)) {
			member = memberService.getOneMember(member.getMusername());
			member.setSuccess(true);
			member.setMessage("登入成功");
			System.out.println(member.getMbirth());
			System.out.println(member);

			return member;
		}else {
			member.setSuccess(false);
			member.setMessage("帳號或密碼不正確");
			System.out.println(member.getMessage());
//			String json = objectMapper.writeValueAsString(member);
			return member;
			}
	}
	
	@PostMapping("/register")
	public Member register(@RequestBody Member member) {

		System.out.println("尚未驗證 "+member);
		if (member.getMusername() == null || (member.getMusername().trim()).length() == 0) {
			member.setSuccess(false);
			member.setMessage("請輸入帳號");
			return member;
		}
		if (member.getMpassword() == null || (member.getMpassword().trim()).length() == 0) {
			member.setSuccess(false);
			member.setMessage("請輸入密碼");
			return member;
		}
		if (member.getMname() == null || (member.getMname().trim()).length() == 0) {
			member.setSuccess(false);
			member.setMessage("請輸入姓名");
			return member;
		}
		if (member.getMsex() == null) {
			member.setSuccess(false);
			member.setMessage("請選擇性別");
			return member;
		}
		if (member.getMbirth() == null) {
			member.setSuccess(false);
			member.setMessage("請選擇生日");
			return member;
		}
		if (member.getMemail() == null || (member.getMemail().trim()).length() == 0) {
			member.setSuccess(false);
			member.setMessage("請輸入信箱");
			return member;
		}
		if (!memberService.mail(member.getMemail())) {
			member.setSuccess(false);
			member.setMessage("信箱格式錯誤");
			return member;
		}

		if (member.getMid() == null || (member.getMid().trim()).length() < 10) {
			member.setSuccess(false);
			member.setMessage("請輸入身分證");
			return member;
		}
		if (!memberService.idCheck(member.getMid())) {
			member.setSuccess(false);
			member.setMessage("身分證格式錯誤");
			return member;
		}
		
		member.setSuccess(true);
		member.setMessage("註冊成功");
		System.out.println("註冊成功 "+member);
		memberService.addMember(member);
		
		return member;
	}

	@PostMapping("/sendpass")
	public Member sendpass(@RequestBody Member member) {

		if (memberService.getByUsername(member.getMusername())) {
			String pw = memberService.changePw(member.getMusername());
			memberService.sendMail(pw);
			member.setSuccess(true);
			member.setMessage("已寄一組新密碼至信箱");
			return member;
		}
		member.setSuccess(false);
		member.setMessage("寄信失敗");
		return member;

	}
	
	@PostMapping(value = "/check")
	public Member check(@RequestBody Member member) {
		System.out.println("check");
		if (memberService.getByUsername(member.getMusername()) == false) {
			member.setSuccess(false);
			member.setMessage("帳號未重複");
			return member;
		}
		member.setSuccess(true);
		member.setMessage("帳號重複");
		return member;
	}

}
