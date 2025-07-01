package com.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import com.google.gson.*;

public class ProductForm extends JFrame{
    private JTextField tfName = new JTextField();
    private JTextField tfPrice = new JTextField();
    private JTextField tfCategory = new JTextField();
    private JTextArea outputArea = new JTextArea(10, 30);

    public ProductForm() {
        setTitle("GraphQL Product Form");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(tfName);
        inputPanel.add(new JLabel("Price:"));
        inputPanel.add(tfPrice);
        inputPanel.add(new JLabel("Category:"));
        inputPanel.add(tfCategory);
        
        JButton btnAdd = new JButton("Add Product");
        JButton btnFetch = new JButton("Show All");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        inputPanel.add(btnAdd);
        inputPanel.add(btnFetch);
        inputPanel.add(btnUpdate);
        inputPanel.add(btnDelete);

        add(inputPanel, BorderLayout.NORTH);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
        
        btnAdd.addActionListener(e -> tambahProduk());
        btnFetch.addActionListener(e -> ambilSemuaProduk());
        btnUpdate.addActionListener(e -> updateProduk());
        btnDelete.addActionListener(e -> hapusProduk());


        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void tambahProduk() {
        try {
            String query = String.format(
                "mutation { addProduct(name: \"%s\", price: %s, category: \"%s\") { id name } }",
                tfName.getText(),
                tfPrice.getText(),
                tfCategory.getText()
                );
                String jsonRequest = new Gson().toJson(new GraphQLQuery(query));
                String response = sendGraphQLRequest(jsonRequest);
                outputArea.setText("Product added!\n\n" + response);
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    private void ambilSemuaProduk() {
        try {
            String query = "query { allProducts { id name price category } }";
            String jsonRequest = new Gson().toJson(new GraphQLQuery(query));
            String response = sendGraphQLRequest(jsonRequest);
            outputArea.setText(response);
        } catch (Exception e) {
            outputArea.setText("Error: " + e.getMessage());
        }
    }

    private void updateProduk() {
    
    }

    private void hapusProduk() {

    }

    private String sendGraphQLRequest(String json) throws Exception {
        URL url = new URL("http://localhost:4567/graphql");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(conn.getInputStream()))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line).append("\n");
                return sb.toString();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ProductForm::new);
    }

    class GraphQLQuery {
        String query;
        GraphQLQuery(String query) {
            this.query = query;
        }
    }
}
