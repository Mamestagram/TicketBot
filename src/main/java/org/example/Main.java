package org.example;

// Created by mames1dev for Mamestagram

import org.example.Object.Bot;
import org.example.Object.Database;
import org.example.Object.Ticket;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static Bot bot;
    public static Database database;
    public static List<Ticket> tickets = new ArrayList<>();

    public static void main(String[] args) {
        bot = new Bot();
        database = new Database();
        bot.loadJDA();
    }
}