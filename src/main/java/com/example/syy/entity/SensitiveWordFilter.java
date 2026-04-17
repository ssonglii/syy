package com.example.syy.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class SensitiveWordFilter {
    private Set<String> sensitiveWords = new HashSet<>();
    private Pattern pattern;

    // 构造函数：加载敏感词库
    public SensitiveWordFilter() {
        loadSensitiveWords();
        buildPattern();
    }

    // 从文件加载敏感词
    private void loadSensitiveWords() {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getClassLoader().getResourceAsStream("sensitive_words.txt")))) {
            String word;
            while ((word = reader.readLine()) != null) {
                if (!word.trim().isEmpty()) {
                    sensitiveWords.add(word.trim());
                }
            }
        } catch (IOException e) {
            System.err.println("敏感词库加载失败：" + e.getMessage());
        }
    }

    // 构建正则表达式模式
    private void buildPattern() {
        if (!sensitiveWords.isEmpty()) {
            StringBuilder patternBuilder = new StringBuilder("(");
            for (String word : sensitiveWords) {
                // 转义特殊字符，避免正则表达式语法错误
                String escapedWord = Pattern.quote(word);
                patternBuilder.append(escapedWord).append("|");
            }
            // 移除最后一个多余的"|"，闭合括号
            patternBuilder.deleteCharAt(patternBuilder.length() - 1).append(")");
            pattern = Pattern.compile(patternBuilder.toString());
        }
    }

    // 检测文本是否包含敏感词
    public boolean containsSensitiveWord(String text) {
        if (text == null || text.isEmpty() || pattern == null) {
            return false;
        }
        return pattern.matcher(text).find();
    }

    // 替换敏感词为星号
    public String replaceSensitiveWords(String text) {
        if (text == null || text.isEmpty() || pattern == null) {
            return text;
        }
        return pattern.matcher(text).replaceAll(match -> {
            String word = match.group();
            return "".repeat(word.length()).replace("", "*");
        });
    }
}