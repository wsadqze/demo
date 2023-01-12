package tw.com.phctw;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.RequestMapping;

import tw.com.phctw.entity.Member;
import tw.com.phctw.service.MemberService;
@SpringBootApplication
@ComponentScan(basePackages = { "tw.com.phctw.service", "tw.com.phctw.dao", "tw.com.phctw.entity", "tw.com.phctw.controller" })
@EntityScan("tw.com.phctw.entity")
@EnableJpaRepositories("tw.com.phctw.dao")

public class DemoApplication {

	public static void main(String[] args) throws ParseException {
//		SpringApplication.run(DemoApplication.class, args);
		
		ConfigurableApplicationContext context = SpringApplication.run(DemoApplication.class, args); // 取得Spring Context

		MemberService memberService = context.getBean(MemberService.class);

//        Member member = memberService.get("a06");
//        System.out.println(member);
        
        
//		String new_pw = memberService.changePw("q");
//        memberService.sendMail(new_pw); 
//        System.out.println(new_pw); 
//		Member member = new Member();
//		member.setMusername("q");
//		member.setMpassword(new_pw);
//		System.out.println(memberService.checkUserAndPassword(member));
		
		String startDate="2000-10-10"; // Input String
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd"); // New Pattern
        java.util.Date date = sdf1.parse(startDate); // Returns a Date format object with the pattern
        java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
		
		
		
//		Member member = new Member();
//		
//		member.setMusername("5");
//		member.setMpassword("aaa");
//		member.setMbirth(sqlStartDate);
//		member.setMname("test");
//		member.setMsex(1);
//		member.setMemail("a@a");
//		member.setMid("A123456789");
//		System.out.println(member);
//		member = memberService.addMember(member);
//		System.out.println(member);
        
        
        
      
        

	}

}
