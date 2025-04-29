/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.Bestiarum;
import model.Monster;

/**
 *
 * @author kozhe
 */
public class JsonImportHandler implements ImportHandler{
    private ImportHandler nextHandler;
    private final ObjectMapper mapper;
    
    public JsonImportHandler(){
        this.mapper = JsonMapper.builder()
                 .enable(DeserializationFeature.UNWRAP_ROOT_VALUE)
                 .build();
    }
    
    @Override
    public void setNextHandler(ImportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public List<Monster> handleImportFile(String filePath) {
      if (filePath.toLowerCase().endsWith(".json")){
        try {
                Bestiarum bestiarum = mapper.readValue(new File(filePath), Bestiarum.class);
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

