package com.sbs.example.demo.dto;

import java.util.Map;

public class ArticleReply extends Dto {
	private int id;
	private String regDate;
	private int articleId;
	private int memberId;
	private String body;


	@Override
	public String toString() {
		return String.format("===%d번 댓글 ===%n%n"
				+ "게시물 번호 : %s%n댓글 생성 날짜 : %s 아이디 : %s%n내용 : %s%n",
				id,articleId,regDate,memberId, body);
	}

	public ArticleReply() {

	}

	public ArticleReply(int articleId, int memberId, String body) {
		this.articleId = articleId;
		this.memberId = memberId;
		this.body = body;
	}

	public ArticleReply(Map<String, Object> row) {
		super((int) row.get("id"), (String) row.get("regDate"));
		this.body = (String) row.get("body");
		this.memberId = (int) row.get("memberId");
		this.articleId = (int) row.get("articleId");
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}