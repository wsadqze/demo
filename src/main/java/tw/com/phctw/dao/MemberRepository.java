package tw.com.phctw.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tw.com.phctw.entity.Member;




@Repository
public interface MemberRepository extends JpaRepository<Member, String>{
	
	Member findByMusername(String musername);//利用帳號取得會員資料
	
}
