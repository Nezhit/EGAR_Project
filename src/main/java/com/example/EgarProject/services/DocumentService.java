package com.example.EgarProject.services;

import com.example.EgarProject.models.*;
import com.example.EgarProject.repos.DocumentRepo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {
    @Value("${upload.dir}")
    private String uploadDir;
    @Value("${fonts.arial}")
    private String arialFont;
    private final DocumentRepo documentRepo;

    public DocumentService(DocumentRepo documentRepo) {
        this.documentRepo = documentRepo;
    }
    public void processBookAsync(MultipartFile file) throws IOException {
        InputStream inputStream=file.getInputStream();
        try (PDDocument document = PDDocument.load(inputStream)) {
            Document book=new Document();


            PDPage page = new PDPage();
            document.addPage(page);

            String fileName = System.currentTimeMillis() + ".pdf";
            Path filePath = Paths.get(uploadDir, fileName);

            try (OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }



            document.save(filePath.toString());
            book.setFilePath(filePath.toString());
            book.setTitle(file.getOriginalFilename());
            book.setAuthor(document.getDocumentInformation().getAuthor());
            book.setReadingProgress(1);
            documentRepo.save(book);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("ЗАВЕРШЕНО В СЕРВИСЕ");
        }
    }
    public void processBookAsync(byte[] array,String username, String reportName) throws IOException {
        InputStream inputStream=new ByteArrayInputStream(array);
        try (PDDocument document = PDDocument.load(inputStream)) {
            Document book=new Document();


            PDPage page = new PDPage();
            document.addPage(page);

            String fileName = System.currentTimeMillis() + ".pdf";
            Path filePath = Paths.get(uploadDir, fileName);

            try (OutputStream outputStream = Files.newOutputStream(filePath, StandardOpenOption.CREATE)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }



            document.save(filePath.toString());
            book.setFilePath(filePath.toString());
            book.setTitle(reportName);
            book.setAuthor(username);
            book.setReadingProgress(1);
            documentRepo.save(book);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("ЗАВЕРШЕНО В СЕРВИСЕ");
        }
    }
    public List<Document> getAll(){
        return documentRepo.findAll();
    }
    public Document getBookById(Long id){
        Document book= documentRepo.findById(id).orElseThrow(()->{
            return new UsernameNotFoundException("Книги с id "+id+"не найден");// Сделать кастом класс
        });
        return book;
    }
    public int getReadingProgress(Long id){
        return documentRepo.findById(id).get().getReadingProgress();
    }
    public void saveProgress(Document doc){
        documentRepo.save(doc);
    }
    public byte[] generateReport(Team team) {
        // Создание PDF-отчета с использованием PDFBox
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
            //contentStream.setFont(PDType1Font.SYMBOL, 12);
            PDType0Font font = PDType0Font.load(document, new FileInputStream(arialFont));
            contentStream.setFont(font, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText("Team Report");

            float y = 680; // начальная координата Y для остальных строк

// Добавление информации о команде, тим лиде, участниках и задачах в отчет
            contentStream.newLineAtOffset(0, -20); // установите интервал между строками

            contentStream.showText("Team Name: " + team.getName());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Team Lead: " + team.getTeamLead().getName());
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Members: " + team.getMembers().stream().map(User::getName).collect(Collectors.joining(", ")));
            contentStream.newLineAtOffset(0, -20);
            contentStream.showText("Tasks:");

// Вставка информации о задачах
//            for (Task task : team.getTasks()) {
//                contentStream.newLineAtOffset(0, -20);
//                contentStream.showText("- " + task.getDescription() + " (Assigned to: " + task.getUser().getName() + ")");
//            }
            for (User user : team.getMembers()){


                for(Task task : user.getTasks()){
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("- " + task.getDescription() + " (Assigned to: " + task.getUser().getName() + ")");
                    for(ChangeJournal changeJournal:task.getChanges()){
                        contentStream.newLineAtOffset(0, -20);
                        contentStream.showText(changeJournal.getChangeText()+" made in "+changeJournal.getChangeTime());

                    }
                }
            }

            contentStream.endText();

        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            document.save(byteArrayOutputStream);
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteArrayOutputStream.toByteArray();
    }



}
