package com.example.syy.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Component
public class ACAutomatonFilter {

    private final Node root = new Node();

    // 项目启动时自动加载敏感词库
    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("sensitive_words.txt");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String word;
            while ((word = br.readLine()) != null) {
                word = word.trim();
                if (!word.isEmpty()) {
                    insert(word);
                }
            }
        }
        buildFailNode();
    }

    // 插入敏感词到 Trie 树
    private void insert(String word) {
        Node curr = root;
        for (char c : word.toCharArray()) {
            curr.children.computeIfAbsent(c, k -> new Node());
            curr = curr.children.get(c);
        }
        curr.isEnd = true;
        curr.value = word;
    }

    // 构建失败指针（AC 自动机核心）
    private void buildFailNode() {
        Queue<Node> queue = new LinkedList<>();
        root.fail = null;
        queue.offer(root);

        while (!queue.isEmpty()) {
            Node curr = queue.poll();
            for (Map.Entry<Character, Node> entry : curr.children.entrySet()) {
                char c = entry.getKey();
                Node child = entry.getValue();
                Node failNode = curr.fail;

                while (failNode != null && !failNode.children.containsKey(c)) {
                    failNode = failNode.fail;
                }
                child.fail = (failNode == null) ? root : failNode.children.getOrDefault(c, root);
                child.isEnd = child.isEnd || child.fail.isEnd;
                queue.offer(child);
            }
        }
    }

    // 判断是否包含敏感词
    public boolean containsSensitive(String text) {
        Node curr = root;
        for (char c : text.toCharArray()) {
            while (curr != null && !curr.children.containsKey(c)) {
                curr = curr.fail;
            }
            if (curr == null) {
                curr = root;
                continue;
            }
            curr = curr.children.get(c);
            if (curr.isEnd) {
                return true;
            }
        }
        return false;
    }

    // AC 自动机节点结构
    private static class Node {
        Map<Character, Node> children = new HashMap<>();
        Node fail;
        boolean isEnd = false;
        String value;
    }
}