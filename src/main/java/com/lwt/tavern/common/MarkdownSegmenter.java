package com.lwt.tavern.common;

import com.lwt.tavern.entity.BookContentBlock;
import lombok.RequiredArgsConstructor;
import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.markdown.MarkdownRenderer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class MarkdownSegmenter {
    private final Parser parser;
    private final MarkdownRenderer markdownRenderer;

    public List<BookContentBlock> segment(String markdown,Long bookId,Long id) {
        if (markdown == null || markdown.isBlank()) {
            return Collections.emptyList();
        }

        Node document = parser.parse(markdown);

        List<BookContentBlock> segments = new ArrayList<>();
        Node child = document.getFirstChild();
        int seq = 1;
        while (child != null) {
            BookContentBlock bookContentBlock = new BookContentBlock(
                    id, bookId, seq++, sliceSource(markdown, child), LocalDateTime.now(),
                    detectType(child), extractAttributes(child)
            );

            segments.add(bookContentBlock);
            child = child.getNext();
        }
        return segments;
    }

    /** 按节点行范围从原文切片，保证是原始 Markdown */
    private String sliceSource(String source, Node node) {
        List<SourceSpan> spans = node.getSourceSpans();
        if (spans.isEmpty()) {
            return markdownRenderer.render(node); // 兜底
        }
        int start = spans.get(0).getInputIndex();
        SourceSpan last = spans.get(spans.size() - 1);
        int end = last.getInputIndex() + last.getLength();
        return source.substring(start, end);
    }

    private String detectType(Node node) {
        if (node instanceof Heading) {
            return "heading";
        }
        if (node instanceof FencedCodeBlock) {
            return "code";
        }
        if (node instanceof IndentedCodeBlock) {
            return "code";
        }
        if (node instanceof BlockQuote) {
            return "quote";
        }
        if (node instanceof BulletList) {
            return "bullet_list";
        }
        if (node instanceof OrderedList) {
            return "ordered_list";
        }
        if (node instanceof ThematicBreak) {
            return "hr";
        }
        if (node instanceof HtmlBlock) {
            return "html";
        }
        if (node instanceof Paragraph) {
            return "paragraph";
        }

        return "unknown";
    }

    private Map<String, Object> extractAttributes(Node node) {
        Map<String, Object> attrs = new HashMap<>();
        if (node instanceof Heading h) {
            attrs.put("level", h.getLevel());
        } else if (node instanceof FencedCodeBlock c) {
            attrs.put("language", c.getInfo());
        }
        return attrs;
    }
}
