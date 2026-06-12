package com.mycapper.entity;

import lombok.Data;
import java.time.LocalDateTime;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class MapData {
    private Long id;
    private String mapName;          // 地图名称
    private String version;          // 版本号
    private byte[] zipFile;          // 地图 zip 文件（二进制）
    private byte[] resourcePack;     // 资源包 zip 文件（二进制，可能为空）
    private String description;      // 地图简介
    private Integer minPlayers;      // 最少人数
    private Integer maxPlayers;      // 最多人数
    private Long fileSize;           // 文件大小（字节）
    private LocalDateTime importTime; // 导入时间
    private String source;   // 新增字段
    private List<ModData> mods;
}