package com.sbs.example.demo.controller;

import java.util.List;

import com.sbs.example.demo.dto.Article;
import com.sbs.example.demo.dto.ArticleReply;
import com.sbs.example.demo.dto.Board;
import com.sbs.example.demo.factory.Factory;
import com.sbs.example.demo.service.ArticleService;

public class ArticleController extends Controller {
	private ArticleService articleService;

	public ArticleController() {
		articleService = Factory.getArticleService();
	}

	public void doAction(Request reqeust) {
		if (reqeust.getActionName().equals("list")) {
			actionList(reqeust);
		} else if (reqeust.getActionName().equals("write")) {
			actionWrite(reqeust);
		} else if (reqeust.getActionName().equals("changeBoard")) {
			actionChangeBoard(reqeust);
		} else if (reqeust.getActionName().equals("currentBoard")) {
			actionCurrentBoard(reqeust);
		} else if (reqeust.getActionName().equals("makeBoard")) {
			actionMakeboard(reqeust);
		} else if (reqeust.getActionName().equals("listBoard")) {
			actionListboard(reqeust);
		} else if (reqeust.getActionName().equals("modify")) {
			actionModify();
		} else if (reqeust.getActionName().equals("delete")) {
			actionDelete();
		} else if (reqeust.getActionName().equals("detail")) {
			actionDetail();
//		} else if (reqeust.getActionName().equals("makeReply")) {
//			actionMakeReply();
		}
	}
	private void actionDeleteReply() {
		
	}
	private void actionMakeReply() {
		System.out.printf("\t=== 댓글 작성 ===%n%n");

		System.out.printf("내용 : ");
		String body = Factory.getScanner().nextLine().trim();

		// 현재 로그인한 회원의 id 가져오기
		int memberId = Factory.getSession().getLoginedMember().getId();
		int articleId = Factory.getSession().getCurrentArticle().getId();
		int newId = articleService.writeArticleReply(articleId, memberId, body);

		System.out.printf("%d번째 댓글이 생성되었습니다.%n%n", newId);
		System.out.printf("\t== 댓글 작성 끝 ==%n%n");
	}

	private void actionListboard(Request reqeust) {
		List<Board> boards = articleService.getBoards();

		System.out.println("\t== 게시판 리스으 ==");
		for (Board board : boards) {
			System.out.printf("게시판 이름 : %s%n게시판 번호 : %s 게시판 코드 : %s%n", board.getName(), board.getId(),
					board.getCode());
		}
		System.out.println("\t== 게시판 리스트 끝 ==");
	}

	private void actionList(Request reqeust) {
		Board currentBoard = Factory.getSession().getCurrentBoard();
		List<Article> articles = articleService.getArticlesByBoardCode(currentBoard.getCode());

		System.out.printf("\t== %s 게시물 리스트 시작 ==%n%n", currentBoard.getName());
		for (Article article : articles) {
			System.out.printf("\t%d, %s, %s\n", article.getId(), article.getRegDate(), article.getTitle());
		}
		System.out.printf("\t== %s 게시물 리스트 끝 ==%n%n", currentBoard.getName());
	}

	private void actionMakeboard(Request reqeust) {
		System.out.printf("\t ==== 게시판 생성 ====");
		System.out.println("생성하실 게시판의 이름을 입력해 주세요.");
		System.out.printf("이름 : ");
		String name = Factory.getScanner().nextLine().trim();
		System.out.println("생성하실 게시판의 코드를 입력해 주세요.");
		System.out.printf("코드 : ");
		String code = Factory.getScanner().nextLine().trim();

		articleService.makeBoard(name, code);
		System.out.printf("\t === 게시판 생성 끝 ===");
	}

	private void actionDetail() {
		System.out.println("게시물 번호를 입력해 주세요.");
		System.out.printf("번호 :");
		String numS = Factory.getScanner().nextLine().trim();
		int num = Integer.parseInt(numS);
		Article article = articleService.detail(num);
		Factory.getSession().setCurrentArticle(article);
		int id = article.getMemberId();
		String memberName = Factory.getMemberService().getMember(id).getName();
		List<ArticleReply> replies = articleService.getArticleReplyByArticleId(article.getId());
		int repliesCount = replies.size();
		

		System.out.printf("\t===== 게시물 상세 =====%n%n");
		System.out.printf("제목 : %s%n", article.getTitle());
		System.out.printf("게시판 번호 : %d ", article.getBoardId());
		System.out.printf("회원 아이디 : %s%n", memberName);
		System.out.printf("게시 번호 : %s ", article.getId());
		System.out.printf("게시 날짜 : %s%n", article.getRegDate());
		System.out.printf("내용 : %s%n", article.getBody());
		System.out.printf("댓글 갯수 : %d%n%n", repliesCount);
		System.out.printf("\t====== 댓글 ======");
		for (ArticleReply articleReply : replies) {
			System.out.printf("\t댓글 번호 : %s%n",articleReply.getArticleId());
			System.out.printf("댓글 생성 날짜 : %s%n",articleReply.getRegDate());
			int a = articleReply.getMemberId();
			String replyMemberName = Factory.getMemberService().getMember(a).getName();
			System.out.printf("댓글 게시자 : %s%n",replyMemberName);
			System.out.printf("내용 : %s%n%n",articleReply.getBody());
		}
		System.out.printf("\t===== 댓글 끝 =====%n%n");
		System.out.printf("==댓글을 작성 하시겠습니까?==%n%n   'yes' or 'no'%n%n");
		System.out.printf(" > ");
		String ans = Factory.getScanner().nextLine().trim();
		if (ans.equals("yes")) {
			actionMakeReply();
		}

		System.out.printf("\t==== 게시물 상세 끝 ====%n%n");
	}

	private void actionDelete() {
		System.out.printf("\t==== 게시물 삭제 ====%n%n");
		System.out.println("게시물 번호를 입력해 주세요.");
		System.out.printf("번호 :");
		int num = Factory.getScanner().nextInt();
		articleService.delete(num);
		System.out.println(num + "번 게시물이 삭제되었습니다.");
		System.out.printf("\t=== 게시물 삭제 끝 ===%n%n");
	}

	private void actionModify() {
		System.out.printf("\t ==== 게시물 수정 ====");
		System.out.println("게시물 번호를 입력해 주세요.");
		System.out.printf("번호 : ");
		int num = Factory.getScanner().nextInt();
		System.out.println("수정하실 제목을 입력해 주세요.");
		System.out.printf("제목 : ");
		String title = Factory.getScanner().nextLine().trim();
		System.out.println("수정하실 내용을 입력해 주세요.");
		System.out.printf("내용 : ");
		String body = Factory.getScanner().nextLine().trim();

		articleService.modify(num, title, body);
		System.out.printf("\t === 게시물 수정 끝 ===");
	}

	private void actionCurrentBoard(Request reqeust) {
		Board board = Factory.getSession().getCurrentBoard();
		System.out.printf("현재 게시판 : %s\n", board.getName());
	}

	private void actionChangeBoard(Request reqeust) {
		String boardCode = reqeust.getArg1();

		Board board = articleService.getBoardByCode(boardCode);

		if (board == null) {
			System.out.println("해당 게시판이 존재하지 않습니다.");
		} else {
			System.out.printf("%s 게시판으로 변경되었습니다.\n", board.getName());
			Factory.getSession().setCurrentBoard(board);
		}
	}

	private void actionWrite(Request reqeust) {
		System.out.printf("\t ==== 게시물 작성= ===");
		System.out.printf("제목 : ");
		String title = Factory.getScanner().nextLine();
		System.out.printf("내용 : ");
		String body = Factory.getScanner().nextLine();

		// 현재 게시판 id 가져오기
		int boardId = Factory.getSession().getCurrentBoard().getId();

		// 현재 로그인한 회원의 id 가져오기
		int memberId = Factory.getSession().getLoginedMember().getId();
		int newId = articleService.write(boardId, memberId, title, body);

		System.out.printf("%d번 글이 생성되었습니다.\n", newId);
		System.out.printf("\t === 게시물 작성 끝 ===");
	}
}