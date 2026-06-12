package com.mycapper.controller;

import com.mycapper.entity.MapData;
import com.mycapper.service.MapService;
import com.mycapper.service.ModService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/maps")
public class MapController {

    @Autowired
    private MapService mapService;
    @Autowired
    private ModService modService;

    // 显示全部
    @GetMapping
    public String list(Model model) {
        model.addAttribute("maps", mapService.getAllMaps());
        return "/maps/list";
    }
    // 显示列表页
    @GetMapping("/download")
    public String downloadList(Model model) {
        model.addAttribute("maps", mapService.getDownloadMaps());
        return "/maps/downloadList";
    }
    // 显示列表页
    @GetMapping("/local")
    public String localList(Model model) {
        model.addAttribute("maps", mapService.getLocalMaps());
        return "/maps/localList";
    }
    // 新增页面
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("map", new MapData());
        model.addAttribute("allMods", modService.getAllMods());
        return "/maps/add";
    }

    // 处理新增（接收文件上传）
    @PostMapping("/add")
    public String add(@RequestParam("mapName") String mapName,
                      @RequestParam("version") String version,
                      @RequestParam("source") String source,
                      @RequestParam("description") String description,
                      @RequestParam("minPlayers") Integer minPlayers,
                      @RequestParam("maxPlayers") Integer maxPlayers,
                      @RequestParam("zipFile") MultipartFile zipFile,
                      @RequestParam(value = "resourcePack", required = false) MultipartFile resourcePack,
                      @RequestParam(value = "modIds", required = false) List<Long> modIds) throws IOException {

        MapData map = new MapData();
        map.setMapName(mapName);
        map.setVersion(version);
        map.setSource(source);
        map.setDescription(description);
        map.setMinPlayers(minPlayers);
        map.setMaxPlayers(maxPlayers);
        map.setZipFile(zipFile.getBytes());
        if (resourcePack != null && !resourcePack.isEmpty()) {
            map.setResourcePack(resourcePack.getBytes());
        }
        map.setFileSize(zipFile.getSize());

        Long mapId = (long)0;
        if(source.equals("download")) mapId = mapService.addDownloadMap(map);
        else mapId = mapService.addLocalMap(map);
        if (modIds != null && !modIds.isEmpty()) {
            mapService.saveMapMods(mapId, modIds);
        }
        return "redirect:/maps/";
    }

    // 显示详情页
    @GetMapping("/views/{id}")
    public String view(@PathVariable Long id, Model model) {
        model.addAttribute("map", mapService.getMapById(id));
        model.addAttribute("mapMods", modService.getModsByMapId(id));
        return "/maps/views";
    }

    // 删除
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        mapService.deleteMapById(id);
        return "redirect:/maps";
    }

    //获取地图压缩包
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadMap(@PathVariable Long id) {
        MapData map = mapService.getMapById(id);
        if (map == null || map.getZipFile() == null) {
            return ResponseEntity.notFound().build();
        }
        String fileName = map.getMapName() + "_" + map.getVersion() + ".zip";
        // 处理中文文件名编码问题
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(map.getZipFile());
    }

    @GetMapping("/download-resource/{id}")
    public ResponseEntity<byte[]> downloadResource(@PathVariable Long id) {
        MapData map = mapService.getMapById(id);
        if (map == null || map.getResourcePack() == null) {
            return ResponseEntity.notFound().build();
        }
        String fileName = map.getMapName() + "_" + map.getVersion() + "_resource.zip";
        // 处理中文文件名编码问题
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(map.getResourcePack());
    }
}