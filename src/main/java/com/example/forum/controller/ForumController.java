package com.example.forum.controller;

import com.example.forum.controller.form.CommentsForm;
import com.example.forum.controller.form.ReportForm;
import com.example.forum.service.CommentsService;
import com.example.forum.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ForumController {
    @Autowired
    ReportService reportService;

    @Autowired
    CommentsService commentsService;

    /*
     * 投稿内容表示処理
     */
    @GetMapping
    public ModelAndView top(@RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        ModelAndView mav = new ModelAndView();
        List<ReportForm> contentData;

        // 開始日と終了日の両方が指定されている場合
        if (startDate != null && endDate != null) {
            contentData = reportService.findByCreatedAtBetween(startDate, endDate);
        }
        // 開始日のみ指定されている場合
        else if (startDate != null) {
            contentData = reportService.findByCreatedAtAfter(startDate);
        }
        // 終了日のみ指定されている場合
        else if (endDate != null) {
            contentData = reportService.findByCreatedAtBefore(endDate);
        }
        // どちらも指定されていない場合
        else {
            contentData = reportService.findAllReport();
        }

        // コメントを全件取得
        List<CommentsForm> commentsData = commentsService.findAllReport();

        CommentsForm commentsForm = new CommentsForm();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("formModel", commentsForm);
        mav.addObject("contents", contentData);
        mav.addObject("comments", commentsData);
        return mav;
    }

    /*
     * 新規投稿画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        // form用の空のentityを準備
        ReportForm reportForm = new ReportForm();
        // 画面遷移先を指定
        mav.setViewName("/new");
        // 準備した空のFormを保管
        mav.addObject("formModel", reportForm);
        return mav;
    }

    /*
     * 新規投稿処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@ModelAttribute("formModel") ReportForm reportForm){
        // 投稿をテーブルに格納
        reportService.saveReport(reportForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        reportService.deleteReport(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * 編集画面表示処理
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集する投稿を取得
        ReportForm report = reportService.editReport(id);
        // 編集する投稿をセット
        mav.addObject("formModel", report);
        // 画面遷移先を指定
        mav.setViewName("/edit");
        return mav;
    }

    /*
     * 編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent (@PathVariable Integer id,
                                       @ModelAttribute("formModel") ReportForm report) {
        // UrlParameterのidを更新するentityにセット
        report.setId(id);
        // 編集した投稿を更新
        reportService.saveReport(report);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
    コメント投稿処理
     */
    @PostMapping("/comment/{id}")
    public ModelAndView commentContent(@PathVariable Integer id,
                                       @ModelAttribute("formModel") CommentsForm commentsForm){
        commentsForm.setId(0);
        commentsForm.setContentId(id);
        // 返信をテーブルに格納
        commentsService.saveComments(commentsForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
    コメント編集画面表示処理
     */
    @GetMapping("/edit-comments/{id}")
    public ModelAndView commentsEditContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        // 編集する投稿を取得
        CommentsForm comments = commentsService.editReport(id);
        // 編集する投稿をセット
        mav.addObject("formModel", comments);
        // 画面遷移先を指定
        mav.setViewName("/edit-comments");
        return mav;
    }

    /*
    コメント編集処理
     */
    @PutMapping("/update-comments/{id}")
    public ModelAndView updateCommentsContent (@PathVariable Integer id,
                                       @RequestParam Integer contentId,
                                       @ModelAttribute("formModel") CommentsForm comments) {
        // UrlParameterのidを更新するentityにセット
        comments.setId(id);
        comments.setContentId(contentId);
        // 編集した投稿を更新
        commentsService.saveComments(comments);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
    コメント削除処理
     */
    @DeleteMapping("/delete-comments/{id}")
    public ModelAndView deleteCommentsContent(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        commentsService.deleteComments(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }
}