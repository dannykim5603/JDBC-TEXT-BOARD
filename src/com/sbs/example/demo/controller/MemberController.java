package com.sbs.example.demo.controller;

import com.sbs.example.demo.dto.Member;
import com.sbs.example.demo.factory.Factory;
import com.sbs.example.demo.service.MemberService;

public class MemberController extends Controller {
	private MemberService memberService;

	public MemberController() {
		memberService = Factory.getMemberService();
	}

	public void doAction(Request reqeust) {
		if (reqeust.getActionName().equals("logout")) {
			actionLogout(reqeust);
		} else if (reqeust.getActionName().equals("login")) {
			actionLogin(reqeust);
		} else if (reqeust.getActionName().equals("whoami")) {
			actionWhoami(reqeust);
		} else if (reqeust.getActionName().equals("join")) {
			actionJoin(reqeust);
		}
	}

	private void actionJoin(Request reqeust) {
		String loginId;
		String loginPw;
		String name;
		
		if (Factory.getSession().isLogined() == false) {
			while (true) {
				System.out.printf("사용하실 아이디를 입력해 주십시오.%n");
				System.out.printf(" 아이디 : ");
				loginId = Factory.getScanner().nextLine().trim();
				if (loginId.length() == 0) {
					System.out.println("아이디를 입력해 주십시요.");
					continue;
				}
				if (memberService.getMemberByLoginId(loginId) != null) {
					System.out.println("이미 존재하는 아이디 입니다.");
					continue;
				}
				break;
			}
			while (true) {
				System.out.printf("사용하실 비밀번호를 입력해 주십시오.%n");
				System.out.printf(" 비밀번호 : ");
				loginPw = Factory.getScanner().nextLine().trim();
				if (loginPw.length() == 0) {
					System.out.println("비밀번호를 입력해 주십시요.");
					continue;
				}
				if (loginPw.length() < 4) {
					System.out.println("비밀번호는 4자 이상이어야 합니다.");
					continue;
				}
				break;
			}
			System.out.printf("사용하실 이름을 입력해 주십시오.%n");
			System.out.printf(" 이름 : ");
			name = Factory.getScanner().nextLine().trim();

			memberService.join(loginId, loginPw, name);
		} else {
			System.out.println("로그아웃 후 이용하실 수 있는 서비스 입니다.");
		}

	}

	private void actionWhoami(Request reqeust) {
		Member loginedMember = Factory.getSession().getLoginedMember();

		if (loginedMember == null) {
			System.out.println("나그네");
		} else {
			System.out.println(loginedMember.getName());
		}

	}

	private void actionLogin(Request reqeust) {
		System.out.println("아이디를 입력해 주세요.");
		System.out.printf("로그인 아이디 : ");
		String loginId = Factory.getScanner().nextLine().trim();
		System.out.println("비밀번호를 입력해 주세요.");
		System.out.printf("로그인 비번 : ");
		String loginPw = Factory.getScanner().nextLine().trim();

		Member member = memberService.getMemberByLoginIdAndLoginPw(loginId, loginPw);

		if (member == null) {
			System.out.println("일치하는 회원이 없습니다.");
		} else {
			System.out.println(member.getName() + "님 환영합니다.");
			Factory.getSession().setLoginedMember(member);
		}
	}

	private void actionLogout(Request reqeust) {
		Member loginedMember = Factory.getSession().getLoginedMember();

		if (loginedMember != null) {
			Session session = Factory.getSession();
			System.out.println("로그아웃 되었습니다.");
			session.setLoginedMember(null);
		} else if (loginedMember == null) {
			System.out.println("로그인 후 이용해 주세요.");
		}

	}
}