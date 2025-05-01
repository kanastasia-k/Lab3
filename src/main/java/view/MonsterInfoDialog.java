/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.border.EmptyBorder;
import model.Monster.Recipe;

public class MonsterInfoDialog extends JDialog {
    private final Monster monster;

    public MonsterInfoDialog(Frame owner, Monster monster) {
        super(owner, "Monster Info", true);
        this.monster = monster;
        setupDialog();
    }

    private void setupDialog() {
        setSize(1000, 600);
        setLocationRelativeTo(getParent());
        
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addInfoField(panel, gbc, "ID:", String.valueOf(monster.getId()));
        addEditableField(panel, gbc, "Имя:", monster.getName(), true);
        addEditableField(panel, gbc, "Описание:", monster.getDescription(), true);
        addEditableField(panel, gbc, "Функция:", monster.getFunction(), true);
        addDangerField(panel, gbc, "Уровень опасности:", monster.getDanger(), 0, 10);
        addEditableField(panel, gbc, "Место обитания:", monster.getHabitat(), true);
        addDateField(panel, gbc,  "Первое упоминание:", monster.getFirstMention());
        addEditableField(panel, gbc, "Иммунитет:", String.join(", ", monster.getImmunities()), true);
        addHeightField(panel, gbc, "Рост:", monster.getHeight(), 0);
        addEditableField(panel, gbc, "Вес:", monster.getWeight(), true);
        addEditableField(panel, gbc, "Время активности:", monster.getActivityTime(), true);
        
        addRecipeInfo(panel, gbc, monster.getRecipe());

        
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 0, 0, 0);
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        panel.add(new JLabel());
        panel.add(closeButton);

        add(new JScrollPane(panel));
    }

    private void addEditableField(JPanel panel, GridBagConstraints gbc, String label, String value, boolean editable) {
        panel.add(new JLabel(label));
        JTextField field = new JTextField(value != null ? value : "", 20);
        field.setEditable(editable);
        if (editable) {
            field.addActionListener(e -> updateField(field, label));
        }
        panel.add(field);
    }

    private void addHeightField(JPanel panel, GridBagConstraints gbc, String label, int value, int min) {
        panel.add(new JLabel(label));
        JTextField field = new JTextField(String.valueOf(value), 5);
        field.addActionListener(e -> {
            try {
                int num = Integer.parseInt(field.getText());
                if (num >= min) {
                    updateField(field, label);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Введите число от " + min,
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
                field.setText(String.valueOf(value));
            }
        });
        panel.add(field);
    }
    
    private void addDangerField(JPanel panel, GridBagConstraints gbc, String label, int value, int min, int max) {
        panel.add(new JLabel(label));
        JTextField field = new JTextField(String.valueOf(value), 5);
        field.addActionListener(e -> {
            try {
                int num = Integer.parseInt(field.getText());
                if (num >= min && num <= max) {
                    updateField(field, label);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Введите число от " + min + "и до " + max,
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
                field.setText(String.valueOf(value));
            }
        });
        panel.add(field);
    }

    private void addInfoField(JPanel panel, GridBagConstraints gbc, String label, String value) {
        panel.add(new JLabel(label));
        panel.add(new JLabel(value != null ? value : "Unknown"));
    }

    private void addRecipeInfo(JPanel panel,GridBagConstraints gbc, Recipe recipe) {
        if (recipe == null) {
            addInfoField(panel, gbc, "Рецепт:", "No recipe available");
            return;
        }

        panel.add(new JLabel("Рецепт:"));
        panel.add(new JLabel(""));
        
        addInfoField(panel, gbc, "Ингредиенты:", String.join(", ", recipe.getIngredients()));
        addInfoField(panel, gbc, "Время приготовления:", recipe.getPreparationTime() + " мин");
        addInfoField(panel, gbc, "Эффективность:", recipe.getEffectiveness());
    }
    
private void addDateField(JPanel panel, GridBagConstraints gbc, String label, String dateValue) {
    panel.add(new JLabel(label));
    
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    format.setLenient(false);
    
    JFormattedTextField dateField = new JFormattedTextField(format);
    
    try {
        if (dateValue != null && !dateValue.isEmpty()) {
            Date date = format.parse(dateValue);
            dateField.setValue(date);
        }
    } catch (ParseException e) {
        dateField.setValue(null);
    }
    
    dateField.setColumns(10);
    
    dateField.addFocusListener(new FocusAdapter() {
    public void focusLost(FocusEvent e) {
        String text = dateField.getText();
        try {
            Date parsedDate = format.parse(text);
            String formatted = format.format(parsedDate);
            if (!text.equals(formatted)) {
                throw new ParseException("Invalid format", 0);
            }
            monster.setFirstMention(formatted);
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(MonsterInfoDialog.this,
                "Неверный формат даты. Используйте ГГГГ-ММ-ДД",
                "Ошибка ввода",
                JOptionPane.ERROR_MESSAGE);
            dateField.setValue(null);
        }
    }
});

    
    panel.add(dateField);
}

    private void updateField(JTextField field, String fieldName) {
        String value = field.getText();
        switch (fieldName) {
            case "Имя:" -> monster.setName(value);
            case "Описание:" -> monster.setDescription(value);
            case "Функция:" -> monster.setFunction(value);
            case "Уровень опасности:" -> monster.setDanger(Integer.parseInt(value));
            case "Место обитания:" -> monster.setHabitat(value);
            case "Первое упоминание:" -> monster.setFirstMention(value);
            case "Иммунитет:" -> {String[] immunitiesArray = value.split(",");
            monster.getImmunities().clear();
            for (String immunity : immunitiesArray) {
                String trimmed = immunity.trim();
                if (!trimmed.isEmpty()) {
                    monster.getImmunities().add(trimmed);
                }
            }
            }
            case "Рост:" -> monster.setHeight(Integer.parseInt(value));
            case "Вес:" -> monster.setWeight(value);
            case "Время активности:" -> monster.setActivityTime(value);
        }
    }
}
 