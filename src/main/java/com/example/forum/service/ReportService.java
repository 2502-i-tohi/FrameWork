package com.example.forum.service;

import com.example.forum.controller.form.ReportForm;
import com.example.forum.repository.ReportRepository;
import com.example.forum.repository.entity.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    @Autowired
    ReportRepository reportRepository;

    /*
     * レコード全件取得処理
     */
    public List<ReportForm> findAllReport() {
        List<Report> results = reportRepository.findAllByOrderByIdDesc();
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }
    /*
     * DBから取得したデータをFormに設定
     */
    private List<ReportForm> setReportForm(List<Report> results) {
        List<ReportForm> reports = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            ReportForm report = new ReportForm();
            Report result = results.get(i);
            report.setId(result.getId());
            report.setContent(result.getContent());
            reports.add(report);
        }
        return reports;
    }

    /*
     * レコード追加
     */
    public void saveReport(ReportForm reqReport) {
        Report saveReport = setReportEntity(reqReport);
        reportRepository.save(saveReport);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */
    private Report setReportEntity(ReportForm reqReport) {
        Report report = new Report();
        report.setId(reqReport.getId());
        report.setContent(reqReport.getContent());
        report.setCreatedAt(reqReport.getCreatedAt());
        return report;
    }

    /*
     *レコード削除
     */
    public void deleteReport(Integer id) {
        reportRepository.deleteById(id);
    }

    /*
     * レコード1件取得
     */
    public ReportForm editReport(Integer id) {
        List<Report> results = new ArrayList<>();
        results.add((Report) reportRepository.findById(id).orElse(null));
        List<ReportForm> reports = setReportForm(results);
        return reports.get(0);
    }

    /*
    日付入力が両方アリのとき
     */
    public List<ReportForm> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN); // 開始日の00:00:00
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);   // 終了日の23:59:59
        List<Report> results = reportRepository.findByCreatedAtBetween(startDateTime, endDateTime);
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
    開始日のみ入力されているとき
     */
    public List<ReportForm> findByCreatedAtAfter(LocalDate startDate) {
        LocalDateTime startDateTime = startDate.atTime(LocalTime.MIN); // 開始日の00:00:00
        LocalDateTime endDateTime = LocalDateTime.now();              // 現在時刻
        List<Report> results = reportRepository.findByCreatedAtBetween(startDateTime, endDateTime);
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }

    /*
    終了日のみ入力されているとき
     */
    public List<ReportForm> findByCreatedAtBefore(LocalDate endDate) {
        LocalDateTime startDateTime = LocalDateTime.of(2020, 1, 1, 0, 0, 0); // 2020-01-01 00:00:00
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);     // 終了日の23:59:59
        List<Report> results = reportRepository.findByCreatedAtBetween(startDateTime, endDateTime);
        List<ReportForm> reports = setReportForm(results);
        return reports;
    }
}