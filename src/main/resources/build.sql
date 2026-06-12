-- 主表
CREATE TABLE maps (
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      map_name VARCHAR(255) NOT NULL,
                      version VARCHAR(50),
                      resource_pack LONGBLOB,
                      map_zip LONGBLOB NOT NULL,
                      description TEXT,
                      min_players INT NOT NULL,
                      max_players INT NOT NULL,
                      file_size BIGINT NOT NULL,
                      import_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      source VARCHAR(20) DEFAULT 'local',   -- 'local' 表示本地，'download' 表示下载
                      CHECK (max_players >= min_players)
);

-- 模组表
CREATE TABLE mods(
                      id INT PRIMARY KEY AUTO_INCREMENT,
                      mod_name VARCHAR(100) NOT NULL,
                      download_url VARCHAR(500) NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 地图与模组的关联表
CREATE TABLE map_mods (
                          map_id INT,
                          mod_id INT,
                          PRIMARY KEY (map_id, mod_id),
                          FOREIGN KEY (map_id) REFERENCES maps(id) ON DELETE CASCADE,
                          FOREIGN KEY (mod_id) REFERENCES mods(id) ON DELETE CASCADE
);