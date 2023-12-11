package com.example.EgarProject.services;

import com.example.EgarProject.models.Document;
import com.example.EgarProject.repos.DocumentRepo;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
public class DocumentService {
    @Value("${upload.dir}")
    private String uploadDir;
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
}
