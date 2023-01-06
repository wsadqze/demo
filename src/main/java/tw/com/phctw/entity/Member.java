package tw.com.phctw.entity;

import java.sql.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@DynamicInsert
@DynamicUpdate
@Entity
public class Member {

	@Id
	@Column
	private String mno;
	@Column(unique = true)
	private String musername;
	@Column
	private String mpassword;
	@Column
	private String mname;
	@Column
	private Integer msex;
	@Column
	private Date mbirth;
	@Column
	private String memail;
	@Column
	private String mid;
	@Column(insertable = false)
	private String mver;
	

	@Override
	public String toString() {
		return "Member [mno=" + mno + ", musername=" + musername + ", mpassword=" + mpassword + ", mname=" + mname
				+ ", msex=" + msex + ", mbirth=" + mbirth + ", memail=" + memail + ", mid=" + mid + ", mver=" + mver
				+ "]";
	}
	
	public String getMno() {
		return mno;
	}
	public void setMno(String mno) {
		this.mno = mno;
	}
	public String getMusername() {
		return musername;
	}
	public void setMusername(String musername) {
		this.musername = musername;
	}
	public String getMpassword() {
		return mpassword;
	}
	public void setMpassword(String mpassword) {
		this.mpassword = mpassword;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public Integer getMsex() {
		return msex;
	}
	public void setMsex(Integer msex) {
		this.msex = msex;
	}
	public Date getMbirth() {
		return mbirth;
	}
	public void setMbirth(Date mbirth) {
		this.mbirth = mbirth;
	}
	public String getMemail() {
		return memail;
	}
	public void setMemail(String memail) {
		this.memail = memail;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getMver() {
		return mver;
	}
	public void setMver(String mver) {
		this.mver = mver;
	}
	
	
	
}