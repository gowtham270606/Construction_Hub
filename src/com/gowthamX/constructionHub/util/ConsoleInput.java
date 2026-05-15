package com.gowthamX.constructionHub.util;

import java.util.Scanner;

public class ConsoleInput {
    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleInput() {
    }

    public static Scanner getScanner() {
        return SCANNER;
    }
}
