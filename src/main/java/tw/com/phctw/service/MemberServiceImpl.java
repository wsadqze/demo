package tw.com.phctw.service;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tw.com.phctw.dao.MemberRepository;
import tw.com.phctw.entity.Member;

@Service
public class MemberServiceImpl implements MemberService {

	@Autowired
	private MemberRepository memberRepository;
	@Autowired(required = false)
	private Member member;

	private String userName = "oq8es2ee@gmail.com"; // 寄件者信箱
	private String password = "zvdmzvuvfcdmmfoz"; // 寄件者密碼
	private String customer = "oq8es2ee@gmail.com"; // 收件者郵箱
	private String subject = "測試郵件"; // 標題
	private String txt = "<h1>++</h1><h2>文字內容</h2>"; // 內容

	@Override
	public Member get(String mno) {
		member = memberRepository.findById(mno).orElse(new Member());
		System.out.println("測試");

		return member;
	}

	// 新增會員
	@Override
	public Member addMember(Member member) {

		long num = memberRepository.count() + 1;
		String mno;

		if (num < 10) {
			mno = "a0" + Long.toString(num);
		} else {
			mno = "a" + Long.toString(num);
		}
		member.setMno(mno);

		String mpassword = md5(member.getMpassword());
		member.setMpassword(mpassword);

		String mid = member.getMid().toUpperCase();
		member.setMid(mid);
		memberRepository.save(member);

		return member;
	}

	// 取得一筆會員資料
	@Override
	public Member getOneMember(String musername) {
		return memberRepository.findByMusername(musername);
	}

	// 確認此帳號是否存在。如果存在，回傳true，否則回傳false
	@Override
	public boolean getByUsername(String musername) {
		if (memberRepository.findByMusername(musername) == null) {
			return false;
		}
		return true;
	}

	// 確認登入的帳號密碼是否正確
	@Override
	public boolean checkUserAndPassword(Member member) {

		String mpasswordCheck = md5(member.getMpassword());
		String mpassword;

		if (memberRepository.findByMusername(member.getMusername()) != null) {
			System.out.println("帳號存在");
			mpassword = memberRepository.findByMusername(member.getMusername()).getMpassword();

			if (mpassword.equals(mpasswordCheck)) {
				System.out.println("密碼正確");
				return true;
			}
		}
		return false;
	}

	// 隨機產生一組密碼存入資料庫
	@Override
	public String changePw(String musername) {
		String mpassword = generateRandomPassword();
		String md5pw = md5(mpassword);
		member = memberRepository.findByMusername(musername);
		member.setMpassword(md5pw);
		memberRepository.save(member);

		return mpassword;

	}

	// 驗證信箱格式是否正確
	@Override
	public boolean mail(String memail) {

		String regx = "^[A-Za-z0-9+_.-]+@(.+)$";

		// Compile regular expression to get the pattern
		Pattern pattern = Pattern.compile(regx);
		// Iterate emails array list
		Matcher matcher = pattern.matcher(memail);
		System.out.println(memail + " : " + matcher.matches() + "\n");

		return matcher.matches();
	}

	// 驗證身分證格式是否正確
	@Override
	public boolean idCheck(String mid) {

		mid = mid.toUpperCase();
		int id1 = mid.charAt(1) - '0';

		// 驗證輸入格式是否正確

		if (!mid.matches("[A-Z]\\d{9}")) { // 一個英文字加九個數字
			System.out.println("格式錯誤！");
			return false;
		}

		if (id1 != 1 && id1 != 2) { // 第一個數字為1或2
			System.out.println("格式錯誤！");
			return false;
		}

		// 驗證演算法是否正確

		String s = "ABCDEFGHJKLMNOPQRSTUVXYWZI";
		int id00 = (s.indexOf(mid.charAt(0)) + 10) / 10;
		int id01 = (s.indexOf(mid.charAt(0)) + 10) % 10;
		int id9 = mid.charAt(9) - '0';
		int sum = 0;

		for (int i = 1, j = 8; i < 9; i++) {
			sum += (mid.charAt(i) - '0') * j;
			j--;
		}
		sum = sum + id00 * 1 + id01 * 9;

		if (sum % 10 == 10 - id9) {
			System.out.print("身分證驗證正確！");
			return true;
		} else if (sum % 10 == 0 && id9 == 0) {
			System.out.print("身分證驗證正確！");
			return true;
		} else {
			System.out.print("身分證驗證錯誤！");
			return true;
		}
	}

	// 寄出一組新的密碼到信箱
	@Override
	public void sendMail(String mpassword) {
		// ---------------------------------------------------------連線設定
		Properties prop = new Properties();
		System.out.println(mpassword);
		// 設定連線為smtp
		prop.setProperty("mail.transport.protocol", "smtp");

		// host主機:smtp.gmail.com
		prop.setProperty("mail.host", "smtp.gmail.com");

		// host port:465
		prop.put("mail.smtp.port", "465");

		// 寄件者帳號需要驗證：是
		prop.put("mail.smtp.auth", "true");

		// 需要安全資料傳輸層 (SSL)：是
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		// 安全資料傳輸層 (SSL) 通訊埠：465
		prop.put("mail.smtp.socketFactory.port", "465");

		// 顯示連線資訊
		prop.put("mail.debug", "true");

		// ---------------------------------------------------------驗證
		// ---------------------------------------------------------Session默認屬性設定值
		// 匿名類別
		Session session = Session.getDefaultInstance(prop, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(userName, password);
			}
		});

		// class
		// Auth auth = new Auth(userName, password);
		// Session session = Session.getDefaultInstance(prop, auth);

		// ---------------------------------------------------------Message郵件格式
		MimeMessage message = new MimeMessage(session);

		try {
			// 寄件者
			// 匿名類別
			// message.setSender(new InternetAddress(userName));

			// class
			InternetAddress sender = new InternetAddress(userName);
			message.setSender(sender);

			// 收件者
			message.setRecipient(RecipientType.TO, new InternetAddress(customer));

			// 標題
			message.setSubject(subject);

			// 內容/格式
			message.setContent(mpassword, "text/html;charset = UTF-8");

			// ---------------------------------------------------------Transport傳送Message
			Transport transport = session.getTransport();

			// transport將message送出

			transport.send(message);

			// 關閉Transport
			transport.close();

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// 將密碼轉成md5
	public String md5(String str) {
		String md5 = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] barr = md.digest(str.getBytes()); // 將 byte 陣列加密
			StringBuffer sb = new StringBuffer(); // 將 byte 陣列轉成 16 進制
			for (int i = 0; i < barr.length; i++) {
				sb.append(byte2Hex(barr[i]));
			}
			String hex = sb.toString();
			md5 = hex.toUpperCase(); // 一律轉成大寫
		} catch (Exception e) {
			e.printStackTrace();
		}
		return md5;
	}

	public String byte2Hex(byte b) {
		String[] h = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
		int i = b;
		if (i < 0) {
			i += 256;
		}
		return h[i / 16] + h[i % 16];
	}

	// 產生隨機六位數大小寫英文及數字組合而成的字串
	public static String generateRandomPassword() {
		// ASCII 範圍 – 字母數字 (0-9, a-z, A-Z)
		final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

		SecureRandom random = new SecureRandom();
		StringBuilder sb = new StringBuilder();

		// 循環的每次迭代從給定的字符中隨機選擇一個字符
		// ASCII 範圍並將其附加到 `StringBuilder` 實例

		for (int i = 0; i < 6; i++) {
			int randomIndex = random.nextInt(chars.length());
			sb.append(chars.charAt(randomIndex));
		}

		return sb.toString();
	}

}