package com.mycapper.repository;

import com.mycapper.entity.MapData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Repository
public class MapRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // 查询所有地图（不分来源）
    public List<MapData> findAll() {
        String sql = "SELECT id, map_name as mapName, version, description, min_players as minPlayers, max_players as maxPlayers, file_size as fileSize, import_time as importTime, source FROM maps ORDER BY import_time DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MapData.class));
    }

    // 按来源查询
    public List<MapData> findBySource(String source) {
        String sql = "SELECT id, map_name as mapName, version, description, min_players as minPlayers, max_players as maxPlayers, file_size as fileSize, import_time as importTime, source FROM maps WHERE source = ? ORDER BY import_time DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(MapData.class), source);
    }

    // 查询所有本地地图（source = 'local'）
    public List<MapData> findAllLocal() {
        return findBySource("local");
    }

    // 查询所有下载地图（source = 'download'）
    public List<MapData> findAllDownload() {
        return findBySource("download");
    }

    // 根据ID查询详情（包含二进制字段）
    public MapData findById(Long id) {
        String sql = "SELECT id, map_name as mapName, version, map_zip as zipFile, resource_pack as resourcePack, description, min_players as minPlayers, max_players as maxPlayers, file_size as fileSize, import_time as importTime, source FROM maps WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(MapData.class), id);
    }

    // 保存新地图（需指定 source）
    public void save(MapData map) {
        String sql = "INSERT INTO maps (map_name, version, map_zip, resource_pack, description, min_players, max_players, file_size, import_time, source) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, map.getMapName());
            ps.setString(2, map.getVersion());
            ps.setBytes(3, map.getZipFile());
            ps.setBytes(4, map.getResourcePack());
            ps.setString(5, map.getDescription());
            ps.setInt(6, map.getMinPlayers());
            ps.setInt(7, map.getMaxPlayers());
            ps.setLong(8, map.getFileSize());
            ps.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            ps.setString(10, map.getSource());
            return ps;
        }, keyHolder);
        map.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    // 更新地图元数据（不更新二进制文件）
    public void update(MapData map) {
        String sql = "UPDATE maps SET map_name = ?, version = ?, description = ?, min_players = ?, max_players = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                map.getMapName(),
                map.getVersion(),
                map.getDescription(),
                map.getMinPlayers(),
                map.getMaxPlayers(),
                map.getId()
        );
    }

    // 删除地图
    public void deleteById(Long id) {
        String sql = "DELETE FROM maps WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // 保存地图和模组的关联关系
    public void saveMapMods(Long mapId, List<Long> modIds) {
        String sql = "INSERT INTO map_mods (map_id, mod_id) VALUES (?, ?)";
        jdbcTemplate.batchUpdate(sql, modIds, modIds.size(), (ps, modId) -> {
            ps.setLong(1, mapId);
            ps.setLong(2, modId);
        });
    }
    // 删除地图和模组的关联关系
    public void deleteMapMods(Long mapId) {
        String sql = "DELETE FROM map_mods WHERE map_id = ?";
        jdbcTemplate.update(sql, mapId);
    }
    // 查询地图和模组的关联关系
    public List<Long> findMapMods(Long mapId) {
        String sql = "SELECT mod_id FROM map_mods WHERE map_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, mapId);
    }
}