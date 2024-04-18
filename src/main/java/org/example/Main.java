package org.example;

// Created by mames1dev for Mamestagram

import org.example.Object.Bot;
import org.example.Object.Database;

public class Main {

    public static Bot bot;
    public static Database database;

    public static void main(String[] args) {
        bot = new Bot();
        database = new Database();
        bot.loadJDA();
    }
}