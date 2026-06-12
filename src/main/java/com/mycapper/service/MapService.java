package com.mycapper.service;

import com.mycapper.entity.MapData;
import com.mycapper.repository.MapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapService {
    @Autowired
    private MapRepository mapRepository;

    public List<MapData> getAllMaps() {
        return mapRepository.findAll();
    }

    public List<MapData> getLocalMaps() {
        return mapRepository.findAllLocal();
    }

    public List<MapData> getDownloadMaps() {
        return mapRepository.findAllDownload();
    }

    public MapData getMapById(Long id) {
        return mapRepository.findById(id);
    }

    public void deleteMapById(Long id) {
        MapData map = mapRepository.findById(id);
        if (map != null) {
            mapRepository.deleteById(id);
        }
    }
    // 其他方法类似，注意 save 时需要设置 source
    public Long addLocalMap(MapData map) {
        map.setSource("local");
        mapRepository.save(map);
        return map.getId();
    }

    public Long addDownloadMap(MapData map) {
        map.setSource("download");
        mapRepository.save(map);
        return map.getId();
    }

    public List<Long> getMapMods(Long mapId) {
        return mapRepository.findMapMods(mapId);
    }

    public void saveMapMods(Long mapId, List<Long> modIds) {
        mapRepository.saveMapMods(mapId, modIds);
    }

    public void deleteMapMods(Long mapId) {
        mapRepository.deleteMapMods(mapId);
    }
}
