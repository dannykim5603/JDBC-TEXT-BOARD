package com.sbs.example.demo.service;

import java.util.List;

import com.sbs.example.demo.dao.ArticleDao;
import com.sbs.example.demo.dto.Article;
import com.sbs.example.demo.dto.ArticleReply;
import com.sbs.example.demo.dto.Board;
import com.sbs.example.demo.factory.Factory;

public class ArticleService {
	private ArticleDao articleDao;

	public ArticleService() {
		articleDao = Factory.getArticleDao();
	}

	public List<Article> getArticlesByBoardCode(String code) {
		return articleDao.getArticlesByBoardCode(code);
	}

	public List<Board> getBoards() {
		return articleDao.getBoards();
	}

	public int makeBoard(String name, String code) {
		Board oldBoard = articleDao.getBoardByCode(code);

		if (oldBoard != null) {
			return -1;
		}

		Board board = new Board(name, code);
		return articleDao.saveBoard(board);
	}

	public Board getBoard(int id) {
		return articleDao.getBoard(id);
	}

	public int write(int boardId, int memberId, String title, String body) {
		Article article = new Article(boardId, memberId, title, body);
		return articleDao.save(article);
	}

	public List<Article> getArticles() {
		return articleDao.getArticles();
	}

	public void makeBoardIfNotExists(String name, String code) {
		Board board = articleDao.getBoardByCode(code);
		
		if ( board == null ) {
			makeBoard(name, code);
		}
	}

	public Board getBoardByCode(String boardCode) {
		return articleDao.getBoardByCode(boardCode);
	}

	public void modify(int num, String title, String body) {
		articleDao.modify(num,title,body);
	}

	public void delete(int num) {
		articleDao.delete(num);
	}

	public Article detail(int num) {
		return articleDao.detail(num);
	}

	public int writeArticleReply(int articleId, int memberId, String body) {
		ArticleReply articleReply = new ArticleReply(articleId, memberId, body);
		return articleDao.saveReply(articleReply);
	}

	public List<ArticleReply> getArticleReplyByArticleId(int id) {
		return articleDao.getArticleReplyByArticleId(id);
	}

	public ArticleReply getReplyById(int id) {
		return articleDao.getArticleReplyById(id);
	}

	public void deleteReply(int id) {
		articleDao.deleteReply(id);
	}

	public void deleteBoard(int id) {
		articleDao.deleteBoardBycode(id);
	}

}