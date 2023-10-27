package com.example.EgarProject.services;

import com.example.EgarProject.models.ChangeJournal;
import com.example.EgarProject.models.Task;
import com.example.EgarProject.models.TaskCon;
import com.example.EgarProject.models.User;
import com.example.EgarProject.models.enums.ETaskCon;
import com.example.EgarProject.pojo.ChangeConRequest;
import com.example.EgarProject.repos.ChangeJournalRepo;
import com.example.EgarProject.repos.TaskConRepo;
import com.example.EgarProject.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class ChangeConService {
    @Autowired
    TaskRepo taskRepo;
    @Autowired
    ChangeJournalRepo changeJournalRepo;
    @Autowired
    TaskConRepo taskConRepo;
    public void fixChanges(ChangeConRequest changeConRequest,Optional<User> user){
        Optional<Task> oneTask = taskRepo.findById(changeConRequest.getTaskId());

        System.out.println(oneTask.stream().iterator().next().getDescription());
        if (oneTask.isPresent() && user.isPresent()) {
            Task task = oneTask.get();
            User currentUser = user.get();

            Set<TaskCon> taskCons = new HashSet<>();

            switch (changeConRequest.getSectionId()) {
                case "section1":

                    taskCons.add(taskConRepo.findByCondition(ETaskCon.TODO).get());
                    break;
                case "section2":
                    taskCons.add(taskConRepo.findByCondition(ETaskCon.IN_PROGRESS).get());
                    break;
                case "section3":
                    taskCons.add(taskConRepo.findByCondition(ETaskCon.TESTING).get());
                    break;
                case "section4":
                    taskCons.add(taskConRepo.findByCondition(ETaskCon.DONE).get());
                    break;
            }

            task.setTaskCon(taskCons);

            ChangeJournal changeJournal = new ChangeJournal(task, currentUser, LocalDateTime.now(), changeConRequest.getTextInput());

            // Сохраните объект ChangeJournal в базе данных
            changeJournalRepo.save(changeJournal);
        } else {
            // Обработка ситуации, когда задача или пользователь не найдены в базе данных
        }
    }
}
