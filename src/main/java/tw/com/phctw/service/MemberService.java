package tw.com.phctw.service;

import org.springframework.stereotype.Service;

import tw.com.phctw.entity.Member;

@Service
public interface MemberService {

	public Member get(String mno);

	public Member addMember(Member member);// 新增會員

	public Member getOneMember(String musername);// 用帳號取得會員資料

	public boolean getByUsername(String musername);// 確認此帳號是否存在

	public boolean checkUserAndPassword(Member member);// 確認登入的帳號密碼是否正確

	public String changePw(String musername);// 隨機生成一組密碼

	public boolean mail(String memail);// 驗證信箱格式是否正確

	public boolean idCheck(String mid); // 驗證身分證格式是否正確

	public void sendMail(String mpassword);// 發送一組密碼到信箱

}
