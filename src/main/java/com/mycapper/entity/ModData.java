package com.mycapper.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ModData {
    private Long id;
    private String modName;
    private String downloadUrl;
    private LocalDateTime createdAt;
}