/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import model.Bestiarum;
import model.Monster;

/**
 *
 * @author kozhe
 */
public class XmlImportHandler implements ImportHandler{
    private ImportHandler nextHandler;
    
    @Override
    public void setNextHandler(ImportHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    @Override
    public List<Monster> handleImportFile(String filePath) {
      if (filePath.toLowerCase().endsWith(".xml")){
        ObjectMapper mapper = new ObjectMapper();
        try {
            JAXBContext context = JAXBContext.newInstance(Bestiarum.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                Bestiarum bestiarum = (Bestiarum) unmarshaller.unmarshal(new File(filePath));
                List<Monster> monsters = bestiarum.getMonsters();
                for (Monster m : monsters) {
                    m.setSource(filePath);
                }
                return monsters;  
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
      }  
      else if (nextHandler != null) {
            return nextHandler.handleImportFile(filePath);
        }
        return new ArrayList<>();
    }
}
