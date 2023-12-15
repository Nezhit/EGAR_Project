package com.example.EgarProject.controllers;

import com.example.EgarProject.models.Document;
import com.example.EgarProject.models.Team;
import com.example.EgarProject.models.User;
import com.example.EgarProject.repos.TeamRepo;
import com.example.EgarProject.repos.UserRepo;
import com.example.EgarProject.services.CookiesService;
import com.example.EgarProject.services.DocumentService;
import com.example.EgarProject.services.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

@Controller
public class DocumentController {
    private final DocumentService documentService;
    private final CookiesService cookiesService;
    private final UserInfo userInfo;
    private  final TeamRepo teamRepo;
    private final UserRepo userRepo;

    public DocumentController(DocumentService documentService, CookiesService cookiesService, UserInfo userInfo, TeamRepo teamRepo, UserRepo userRepo) {
        this.documentService = documentService;
        this.cookiesService = cookiesService;
        this.userInfo = userInfo;
        this.teamRepo = teamRepo;
        this.userRepo = userRepo;
    }


    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        try {
            // Получение InputStream из MultipartFile
            //InputStream inputStream = file.getInputStream();

            documentService.processBookAsync(file);
            System.out.println("ЗАВЕРШЕНО В КОНТРОЛЛЕРЕ");
            return ResponseEntity.ok("Файл успешно загружен");
        } catch (IOException e) {
            e.printStackTrace();
            // Вернуть ответ с ошибкой
            return ResponseEntity.status(500).body("Произошла ошибка при загрузке файла");
        }

    }
    @GetMapping("/pdf/{id}")
    public String pdfPage(@PathVariable int id, Model model){
        model.addAttribute("id",id);
        int currentPage=documentService.getReadingProgress((long) id);
        model.addAttribute("currentPage",currentPage);
        return "pdf";
    }
    @GetMapping("/pdf/{bookId}/{pageNumber}")
    public ResponseEntity<?> serveFile(@PathVariable Long bookId,@PathVariable int pageNumber) throws IOException {
        Document book=documentService.getBookById(bookId);

        //File file = new File("src/main/uploads/1701901688860.pdf");
        File file = new File(book.getFilePath());
        // int pageNumber=book.getReadingProgress();
        List<PDDocument> Pages = null;
        try (PDDocument document = PDDocument.load(file)) {
            Splitter splitter = new Splitter();
            Pages = splitter.split(document);
            Iterator<PDDocument> iterator = Pages.listIterator();


            PDDocument pd = new PDDocument();
            if (Pages.size() >= pageNumber) {
                pd = Pages.get(pageNumber - 1);
            } else {
                return ResponseEntity.badRequest().body("Не правильная страница");
            }


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            pd.save(byteArrayOutputStream);


            byte[] pdfBytes = byteArrayOutputStream.toByteArray();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("inline").filename("output.pdf").build());
            pd.close();
            book.setReadingProgress(pageNumber);
            documentService.saveProgress(book);
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);


        } finally {

            for (PDDocument pageDocument : Pages) {
                pageDocument.close();
            }
        }


    }
    @GetMapping("/teamleadpanel/report/{reportName}")
    public ResponseEntity<?> generateReport(HttpServletRequest request,@PathVariable String reportName ) throws IOException {
        String username=cookiesService.extractUsername(request);
        User teamLead=userRepo.findByUsername(username).orElseThrow();
        Team team=teamRepo.findByTeamLead(teamLead).get();
        byte[] pdfReport= documentService.generateReport(team);

        documentService.processBookAsync(pdfReport,username,reportName);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename("output.pdf").build());
        return new ResponseEntity<>(pdfReport, headers, HttpStatus.OK);
    }
    @GetMapping("/reportdesk")
    public String getReportDesk(Model model){
        List<Document> documents=documentService.getAll();
        model.addAttribute("documents",documents);
        return "reportdesk";
    }

}
