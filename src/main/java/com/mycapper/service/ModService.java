package com.mycapper.service;

import com.mycapper.entity.ModData;
import com.mycapper.repository.ModRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ModService {
    @Autowired
    private ModRepository modRepository;

    public List<ModData> getAllMods() {
        List<ModData> list = modRepository.findAll();
        return list.stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public ModData getModById(Long id) {
        return modRepository.findById(id);
    }

    public List<ModData> getModsByMapId(Long mapId) {
        return modRepository.findModsByMapId(mapId);
    }

    public void addMod(ModData mod) {
        modRepository.save(mod);
    }

    public void deleteMod(Long id) {
        modRepository.deleteById(id);
    }

    public List<ModData> getModsByIds(List<Long> modIds) {
        return modRepository.getModsByIds(modIds);
    }
}