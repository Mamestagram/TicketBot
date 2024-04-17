package org.example;

// Created by mames1dev for Mamestagram

import org.example.Object.Bot;

public class Main {

    public static Bot bot;

    public Main() {
        bot = new Bot();
    }

    public static void main(String[] args) {

        bot.loadJDA();

    }
}