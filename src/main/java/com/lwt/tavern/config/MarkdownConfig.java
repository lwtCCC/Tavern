package com.lwt.tavern.config;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.IncludeSourceSpans;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.markdown.MarkdownRenderer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MarkdownConfig {

    private List<Extension> extensions() {
        return List.of(TablesExtension.create());
    }

    @Bean
    public Parser parser() {
        return Parser.builder()
                .includeSourceSpans(IncludeSourceSpans.BLOCKS_AND_INLINES)
                .extensions(extensions())
                .build();
    }

    @Bean
    public HtmlRenderer htmlRenderer() {
        return HtmlRenderer.builder()
                .extensions(extensions())
                .escapeHtml(true)   // 降低 XSS 风险
                .build();
    }

    @Bean
    public MarkdownRenderer markdownRenderer() {
        return MarkdownRenderer.builder()
                .extensions(extensions())
                .build();
    }
}
