package com.KoreaIT.JAM.controller;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

import com.KoreaIT.JAM.dto.Article;
import com.KoreaIT.JAM.service.ArticleService;
import com.KoreaIT.JAM.session.Session;
import com.KoreaIT.JAM.util.Util;

public class ArticleController {

	private Scanner sc;
	private ArticleService articleService;

	public ArticleController(Connection conn, Scanner sc) {
		this.sc = sc;
		this.articleService = new ArticleService(conn);
	}

	public void doWrite() {
		if (!Session.isLogined()) {
			System.out.println("로그인 후 이용해 주십시오.");
			return;
		}

		System.out.println("== 게시물 작성 ==");

		System.out.printf("제목 : ");
		String title = sc.nextLine();
		System.out.printf("내용 : ");
		String body = sc.nextLine();

		int id = articleService.doWrite(Session.loginedMemberId, title, body);

		System.out.printf("%d번 게시글이 생성되었습니다.\n", id);
	}

	public void showList(String cmd) {
		String searchKeyword = cmd.split(" ")[2];
		
			List<Article> articles = articleService.getArticles(searchKeyword);

			if (articles.size() == 0) {
				System.out.println("존재하는 게시물이 없습니다.");
				return;
			}

			System.out.println("== 게시물 리스트 ==");
			if (searchKeyword.length() > 0) {
				System.out.println("검색어 : " + searchKeyword);
			}
			System.out.println("번호	|	제목	|	작성자	|	날짜");

			for (Article article : articles) {
				System.out.printf("%d	|	%s	|	%s	|	%s\n", article.id, article.title, article.WriterName,
						Util.datetimeFormat(article.regDate));
			}
	}

	public void showDetail(String cmd) {
		int id = Integer.parseInt(cmd.split(" ")[2]);

		int affectedRow = articleService.increaseHit(id);
		
		if (affectedRow == 0) {
			System.out.printf("%d번 게시물은 존재하지 않습니다.\n", id);
			return;
		}
		
		Article article = articleService.getArticle(id);

		System.out.printf("== %d번 게시물 상세보기 ==\n", id);
		System.out.printf("번호 : %d\n", article.id);
		System.out.printf("작성날짜 : %s\n", Util.datetimeFormat(article.regDate));
		System.out.printf("수정날짜 : %s\n", Util.datetimeFormat(article.updateDate));
		System.out.printf("조회수 : %s\n", article.hit);
		System.out.printf("작성자 : %s\n", article.WriterName);
		System.out.printf("제목 : %s\n", article.title);
		System.out.printf("내용 : %s\n", article.body);
	}

	public void doModify(String cmd) {
		if (!Session.isLogined()) {
			System.out.println("로그인 후 이용해 주십시오.");
			return;
		}

		int id = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticle(id);

		if (article == null) {
			System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
			return;
		}

		if (article.memberId != Session.loginedMemberId) {
			System.out.println("해당 게시물에 대한 권한이 없습니다.");
			return;
		}

		System.out.printf("== %d번 게시글 수정 ==\n", id);

		System.out.printf("수정할 제목 : ");
		String title = sc.nextLine();
		System.out.printf("수정할 내용 : ");
		String body = sc.nextLine();

		articleService.doModify(id, title, body);

		System.out.printf("%d번 게시글이 수정되었습니다\n", id);
	}

	public void doDelete(String cmd) {
		if (!Session.isLogined()) {
			System.out.println("로그인 후 이용해 주십시오.");
			return;
		}

		int id = Integer.parseInt(cmd.split(" ")[2]);

		Article article = articleService.getArticle(id);

		if (article == null) {
			System.out.printf("%d번 게시글은 존재하지 않습니다.\n", id);
			return;
		}

		if (article.memberId != Session.loginedMemberId) {
			System.out.println("해당 게시물에 대한 권한이 없습니다.");
			return;
		}

		System.out.printf("== %d번 게시물 삭제 ==\n", id);

		articleService.doDelete(id);

		System.out.printf("%d번 게시글이 삭제되었습니다\n", id);
	}

}
