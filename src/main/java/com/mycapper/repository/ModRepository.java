package com.mycapper.repository;

import com.mycapper.entity.ModData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class ModRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ModData> findAll() {
        String sql = "SELECT id, mod_name as modName, download_url as downloadUrl, created_at as createdAt FROM mods";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            ModData mod = new ModData();
            mod.setId(rs.getLong("id"));
            mod.setModName(rs.getString("modName"));
            mod.setDownloadUrl(rs.getString("downloadUrl"));
            mod.setCreatedAt(rs.getTimestamp("createdAt") != null ? rs.getTimestamp("createdAt").toLocalDateTime() : null);
            return mod;
        });
    }

    public ModData findById(Long id) {
        String sql = "SELECT id, mod_name as modName, download_url as downloadUrl, created_at as createdAt FROM mods WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(ModData.class), id);
    }

    public List<ModData> findModsByMapId(Long mapId) {
        String sql = "SELECT m.id, m.mod_name as modName, m.download_url as downloadUrl, m.created_at as createdAt FROM mods m JOIN map_mods mm ON m.id = mm.mod_id WHERE mm.map_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ModData.class), mapId);
    }

    public void save(ModData mo) {
        String sql = "INSERT INTO mods (mod_name, download_url) VALUES (?, ?)";
        jdbcTemplate.update(sql, mo.getModName(), mo.getDownloadUrl());
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM mods WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<ModData> getModsByIds(List<Long> modIds) {
        if (modIds == null || modIds.isEmpty()) {
            return Collections.emptyList();
        }
        return jdbcTemplate.query("SELECT id, mod_name as modName, download_url as downloadUrl, created_at as createdAt FROM mods WHERE id IN (" + String.join(",", Collections.nCopies(modIds.size(), "?")) + ")",
                new BeanPropertyRowMapper<>(ModData.class),
                modIds.toArray());
    }
}