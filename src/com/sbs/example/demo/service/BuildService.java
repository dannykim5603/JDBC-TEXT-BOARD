package com.sbs.example.demo.service;

import java.util.List;

import com.sbs.example.demo.dto.Article;
import com.sbs.example.demo.dto.Board;
import com.sbs.example.demo.factory.Factory;
import com.sbs.example.demo.util.Util;

// Service
public class BuildService {
	private ArticleService articleService;

	public BuildService() {
		articleService = Factory.getArticleService();
	}

	public void buildSite() {
		Util.makeDir("site");
		Util.makeDir("site/article");
		Util.makeDir("site/home");
		Util.makeDir("site_template/resource");
		Util.makeDir("site_template/home");
		Util.makeDir("site_template/stat");
		Util.makeDir("site_template/part");
		Util.makeDir("site_template/article");

		String head = Util.getFileContents("site_template/part/head.html");
		String foot = Util.getFileContents("site_template/part/foot.html");

// 각 게시판 별 게시물리스트 페이지 생성
		List<Board> boards = articleService.getBoards();

		for (Board board : boards) {
			String fileName = board.getCode() + "-list-1.html";

			String html = "";

			List<Article> articles = articleService.getArticlesByBoardCode(board.getCode());

			String template = Util.getFileContents("site_template/article/list.html");

			for (Article article : articles) {
				html += "<tr>";
				html += "<td>" + article.getId() + "</td>";
				html += "<td>" + article.getRegDate() + "</td>";
				html += "<td>" + article.getMemberId() + "</td>";
				html += "<td><a href=\"" + article.getId() + ".html\">" + article.getTitle() + "</a></div>";
				html += "</tr>";
			}

			html = template.replace("${TR}", html);

			html = head + html + foot;

			Util.writeFileContents("site/article/" + fileName, html);
		}

// 게시물 별 파일 생성
		List<Article> articles = articleService.getArticles();

		for (Article article : articles) {
			String html = "";

			String template = Util.getFileContents("site_template/article/article.html");

			html += "<div class = article-header>" + article.getTitle() + "</div>";
			html += "<div class = article-content>" + article.getBody() + "</div>";
			html += "<div class = header>";
			html += "<div class = before><a href=\"" + (article.getId() - 1) + ".html\">이전글</a></div>";
			html += "<div class = after><a href=\"" + (article.getId() + 1) + ".html\">다음글</a></div>";
			html += "</div class>";

			html = template.replace("${TR}", html);

			html = head + html + foot;

			Util.writeFileContents("site/article/" + article.getId() + ".html", html);
		}

// 인덱스 화면 
		String fileName = "index.html";
		String html = "";
		List<Article> noticeArticles = articleService.getArticlesByBoardCode("notice");

		String indexTemplate = Util.getFileContents("site_template/home/index.html");

		for (Article article : noticeArticles) {
			html += "<tr>";
			html += "<td>" + article.getId() + "</td>";
			html += "<td>" + article.getRegDate() + "</td>";
			html += "<td>" + article.getMemberId() + "</td>";
			html += "<td><a href=\"" + "../article/" + article.getId() + ".html\">" + article.getTitle() + "</a></div>";
			html += "</tr>";
		}
		html = indexTemplate.replace("${TN}", html);

		html = head + html + foot;

		Util.writeFileContents("site/home/" + fileName, html);
	}
}