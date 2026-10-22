package com.lwt.tavern.component;

import lombok.RequiredArgsConstructor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MarkdownService {
    private final Parser parser;
    private final HtmlRenderer htmlRenderer;

    /** Markdown → HTML */
    public String toHtml(String markdown) {
        if (markdown == null || markdown.isBlank()) {
            return "";
        }
        return htmlRenderer.render(parser.parse(markdown));
    }

    /** 拿到 AST，供分段/遍历使用 */
    public Node parseToAst(String markdown) {
        return parser.parse(markdown);
    }

    /*public List<Segment> splitToSegments(String markdown) {
        Node doc = markdownService.parseToAst(markdown);
        List<Segment> segments = new ArrayList<>();
        Node child = doc.getFirstChild();
        int seq = 1;
        while (child != null) {
            segments.add(new Segment(seq++, detectType(child), extractText(child)));
            child = child.getNext();
        }
        return segments;
    }*/
}
