package com.example.EgarProject.pojo;

import com.example.EgarProject.models.enums.ETaskCon;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TeamLeadMark {
   @NotNull
   private Long id;
   @NotBlank
   private ETaskCon taskCon;
   @NotBlank
   private String changeText;

   public TeamLeadMark() {
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public ETaskCon getTaskCon() {
      return taskCon;
   }

   public void setTaskCon(ETaskCon taskCon) {
      this.taskCon = taskCon;
   }

   public String getChangeText() {
      return changeText;
   }

   public void setChangeText(String changeText) {
      this.changeText = changeText;
   }
}
