package com.sbs.example.demo.controller;

import java.util.List;

import com.sbs.example.demo.dto.Article;
import com.sbs.example.demo.dto.ArticleReply;
import com.sbs.example.demo.dto.Board;
import com.sbs.example.demo.dto.Member;
import com.sbs.example.demo.factory.Factory;
import com.sbs.example.demo.service.ArticleService;

public class ArticleController extends Controller {
	private ArticleService articleService;

	public ArticleController() {
		articleService = Factory.getArticleService();
	}

	public void doAction(Request request) {
		if (request.getActionName().equals("list")) {
			actionList(request);
		} else if (request.getActionName().equals("write")) {
			actionWrite(request);
		} else if (request.getActionName().equals("changeBoard")) {
			actionChangeBoard(request);
		} else if (request.getActionName().equals("currentBoard")) {
			actionCurrentBoard(request);
		} else if (request.getActionName().equals("makeBoard")) {
			actionMakeboard(request);
		} else if (request.getActionName().equals("listBoard")) {
			actionListboard(request);
		} else if (request.getActionName().equals("deleteBoard")) {
			actionDeleteBoard(request);
		} else if (request.getActionName().equals("modify")) {
			actionModify();
		} else if (request.getActionName().equals("delete")) {
			actionDelete();
		} else if (request.getActionName().equals("detail")) {
			actionDetail();
		}
	}

	private void actionDeleteBoard(Request request) {
		if (Factory.getSession().getLoginedMember().getId() == 1) {
			System.out.println("\t삭제하실 게시판의 번호를 입력해 주세요.");
			System.out.printf("번호 : ");
			String numS = Factory.getScanner().nextLine().trim();
			int id = Integer.parseInt(numS);
			articleService.deleteBoard(id);
			System.out.printf("\t"+id + "번 게시판이 삭제 되었습니다.%n");
		} else if (Factory.getSession().getLoginedMember().getId() != 1) {
			System.out.println("권한이 필요한 서비스입니다.");
		}		
	}

	private void actionDeleteReply() {

		if (Factory.getSession().isLogined() == true) {
			System.out.printf("\t==== 댓글 삭제 ====%n%n");
			System.out.println("댓글 번호를 입력해 주세요.");
			System.out.printf("번호 :");
			String numS = Factory.getScanner().nextLine().trim();
			int id = Integer.parseInt(numS);
			Member member = Factory.getSession().getLoginedMember();
			ArticleReply articleReply = Factory.getArticleService().getReplyById(id);
			if (articleReply.getMemberId() == member.getId()) {
				articleService.deleteReply(id);
				System.out.println(id + "번 게시물이 삭제되었습니다.");
				System.out.printf("\t=== 게시물 삭제 끝 ===%n%n");
			} else {
				System.out.println("삭제 권한이 없습니다.");
			}
		}
	}

	private void actionMakeReply() {
		if (Factory.getSession().isLogined() == true) {
			System.out.printf("\t=== 댓글 작성 ===%n%n");

			System.out.printf("내용 : ");
			String body = Factory.getScanner().nextLine().trim();

			// 현재 로그인한 회원의 id 가져오기
			int memberId = Factory.getSession().getLoginedMember().getId();
			int articleId = Factory.getSession().getCurrentArticle().getId();
			int newId = articleService.writeArticleReply(articleId, memberId, body);

			System.out.printf("%d번째 댓글이 생성되었습니다.%n%n", newId);
			System.out.printf("\t== 댓글 작성 끝 ==%n%n");
		} else {
			System.out.println("로그인이 필요한 서비스 입니다.");
		}
	}

	private void actionListboard(Request reqeust) {
		List<Board> boards = articleService.getBoards();

		System.out.println("\t== 게시판 리스트 ==");
		for (Board board : boards) {
			System.out.printf("%n게시판 이름 : %s%n게시판 번호 : %s%n게시판 코드 : %s%n%n", board.getName(), board.getId(),
					board.getCode());
		}
		System.out.println("\t== 게시판 리스트 끝 ==");
	}

	private void actionList(Request reqeust) {
		Board currentBoard = Factory.getSession().getCurrentBoard();
		List<Article> articles = articleService.getArticlesByBoardCode(currentBoard.getCode());

		System.out.printf("\t== %s 게시물 리스트 시작 ==%n%n", currentBoard.getName());
		for (Article article : articles) {
			System.out.printf("게시물 번호 : %d%n게시 날짜 : %s%n게시물 제목 : %s\n", article.getId(), article.getRegDate(),
					article.getTitle());
		}
		System.out.printf("\t== %s 게시물 리스트 끝 ==%n%n", currentBoard.getName());
	}

	private void actionMakeboard(Request reqeust) {
		if (Factory.getSession().getLoginedMember().getId() == 1) {
			System.out.printf("\t ==== 게시판 생성 ====%n%n");
			System.out.println("생성하실 게시판의 이름을 입력해 주세요.");
			System.out.printf("이름 : ");
			String name = Factory.getScanner().nextLine().trim();
			System.out.println("생성하실 게시판의 코드를 입력해 주세요.");
			System.out.printf("코드 : ");
			String code = Factory.getScanner().nextLine().trim();

			articleService.makeBoard(name, code);
			System.out.printf("\t === 게시판 생성 끝 ===%n");
		} else if (Factory.getSession().getLoginedMember().getId() != 1) {
			System.out.println("권한이 필요한 서비스입니다.");
		}

	}

	private void actionDetail() {
		if (Factory.getSession().isLogined() == true) {
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
			System.out.printf("\t====== 댓글 ======%n%n");
			for (ArticleReply articleReply : replies) {
				System.out.printf("댓글 번호 : %s%n", articleReply.getId());
				System.out.printf("댓글 생성 날짜 : %s%n", articleReply.getRegDate());
				int a = articleReply.getMemberId();
				String replyMemberName = Factory.getMemberService().getMember(a).getName();
				System.out.printf("댓글 게시자 : %s%n", replyMemberName);
				System.out.printf("내용 : %s%n%n", articleReply.getBody());
			}
			System.out.printf("\t===== 댓글 끝 =====%n%n");
			System.out.printf("\t댓글을 작성 하시려면%n");
			System.out.printf("\t    reply%n");
			System.out.printf("\t댓글 삭제를 원하시면%n");
			System.out.printf("\t    delete %n");
			System.out.printf("\t을 입력해 주세요.%n");
			System.out.printf(" > ");
			String ans = Factory.getScanner().nextLine().trim();
			if (ans.equals("reply")) {
				actionMakeReply();
			} else if (ans.equals("delete")){
				actionDeleteReply();
			}

			System.out.printf("\t==== 게시물 상세 끝 ====%n%n");
		} else {
			System.out.println("\t로그인이 필요한 서비스 입니다.");
		}
	}

	private void actionDelete() {
		if (Factory.getSession().isLogined() == true) {
			System.out.printf("\t==== 게시물 삭제 ====%n%n");
			System.out.println("게시물 번호를 입력해 주세요.");
			System.out.printf("번호 :");
			String numS = Factory.getScanner().nextLine().trim();
			int num = Integer.parseInt(numS);
			Member member = Factory.getSession().getLoginedMember();
			Article article = Factory.getArticleDao().detail(num);
			if (article.getMemberId() == member.getId()) {
				articleService.delete(num);
				System.out.println(num + "번 게시물이 삭제되었습니다.");
				System.out.printf("\t=== 게시물 삭제 끝 ===%n%n");
			} else {
				System.out.println("삭제 권한이 없습니다.");
			}
		}
	}

	private void actionModify() {
		if (Factory.getSession().isLogined() == true) {
			System.out.printf("\t ==== 게시물 수정 ====");
			System.out.println("게시물 번호를 입력해 주세요.");
			System.out.printf("번호 : ");
			String numS = Factory.getScanner().nextLine().trim();
			int num = Integer.parseInt(numS);
			System.out.println("수정하실 제목을 입력해 주세요.");
			System.out.printf("제목 : ");
			String title = Factory.getScanner().nextLine().trim();
			System.out.println("수정하실 내용을 입력해 주세요.");
			System.out.printf("내용 : ");
			String body = Factory.getScanner().nextLine().trim();

			Member member = Factory.getSession().getLoginedMember();
			Article article = Factory.getArticleDao().detail(num);
			if (article.getMemberId() == member.getId()) {
				articleService.modify(num, title, body);
				System.out.printf("\t === 게시물 수정 끝 ===");
			}
		} else {
			System.out.println("로그인이 필요한 서비스 입니다.");
		}
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
		if (Factory.getSession().isLogined() == true) {
			System.out.printf("\t==== 게시물 작성 ====%n%n");
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
			System.out.printf("%n\t=== 게시물 작성 끝 ===%n%n");
		} else {
			System.out.println("로그인이 필요한 서비스 입니다.");
		}
	}
}