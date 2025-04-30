/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import model.Monster.Recipe;

public class MonsterInfoDialog extends JDialog {
    private final Monster monster;

    public MonsterInfoDialog(Frame owner, Monster monster) {
        super(owner, "Monster Info", true);
        this.monster = monster;
        
        setSize(400, 600);
        setLocationRelativeTo(owner);
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        addField(panel, gbc, "Name:", new JTextField(monster.getName(), 20), 
            e -> monster.setName(((JTextField)e.getSource()).getText()));

        addLabel(panel, gbc, "ID:", formatNumber(monster.getId()));
        addLabel(panel, gbc, "Description:", monster.getDescription());
        addLabel(panel, gbc, "Function:", monster.getFunction());
        addLabel(panel, gbc, "Danger Level:", formatNumber(monster.getDanger()));
        addLabel(panel, gbc, "Habitat:", monster.getHabitat());
        addLabel(panel, gbc, "first_mention:", formatDate(monster.getFirstMention()));
        addLabel(panel, gbc, "immunity:", String.join(", ", monster.getImmunities()));
        addLabel(panel, gbc, "height:", formatNumber(monster.getHeight()));
        addLabel(panel, gbc, "weight:", monster.getWeight());
        addLabel(panel, gbc, "activity_time:", monster.getActivityTime());
        addRecipeInfo(panel, gbc, monster.getRecipe());
        
        
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(closeButton, gbc);

        add(new JScrollPane(panel));
    }

    private void addRecipeInfo(JPanel panel, GridBagConstraints gbc, Recipe recipe) {
        if (recipe == null) {
            addLabel(panel, gbc, "Recipe:", "No recipe available");
            return;
        }

        addLabel(panel, gbc, "Recipe:", "");
        gbc.gridy++;

        StringBuilder ingredients = new StringBuilder("<html>");
        for (String ingredient : recipe.getIngredients()) {
            ingredients.append("• ").append(ingredient).append("<br>");
        }
        addLabel(panel, gbc, "Ingredients:", ingredients.toString());

        addLabel(panel, gbc, "Prep time:", 
            String.format("%d мин", recipe.getPreparationTime()));

        addLabel(panel, gbc, "Effectiveness:", recipe.getEffectiveness());
    }
    
    private String formatNumber(int number) {
        return String.format("%,d", number); 
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "Unknown";
        }
        return dateString;
    }

    
    private void addField(JPanel panel, GridBagConstraints gbc, String label, 
                         JTextField field, java.awt.event.ActionListener listener) {
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        field.addActionListener(listener);
        panel.add(field, gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }

    private void addLabel(JPanel panel, GridBagConstraints gbc, String label, String value) {
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(value), gbc);
        gbc.gridx = 0;
        gbc.gridy++;
    }
}