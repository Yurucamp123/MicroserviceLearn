package com.lethanhbinh.notificationservice.data;

import lombok.Data;

@Data
public class EmailTemplate {
    private String message;
    private String emailTo;
}