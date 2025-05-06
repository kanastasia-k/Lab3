/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Bestiarum;
import model.Monster;
import org.yaml.snakeyaml.Yaml;
/**
 *
 * @author kozhe
 */
public class YamlImportHandler implements ImportHandler{
    private ImportHandler nextHandler;
    
    @Override
    public void setNextHandler(ImportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public List<Monster> handleImportFile(String filePath) {
        if (filePath.toLowerCase().endsWith(".yml") || filePath.toLowerCase().endsWith(".yaml")) {
            try (FileInputStream inputStream = new FileInputStream(filePath)) {
                Yaml yaml = new Yaml();
                Map<String, Object> root = yaml.load(inputStream);
                Object bestiarumObj = root.get("bestiarum");
                if (bestiarumObj == null) {
                    return new ArrayList<>();
                }
                
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(bestiarumObj);
                Bestiarum bestiarum = mapper.readValue(json, Bestiarum.class);
                List<Monster> monsters = bestiarum.getMonsters();
                for (Monster m : monsters) {
                    m.setSource(filePath);
                }
                return monsters;
            } catch (Exception e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        } else if (nextHandler != null) {
            return nextHandler.handleImportFile(filePath);
        }
        return new ArrayList<>();
    }
}
