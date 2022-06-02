package com.example.cms.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Getter
@Setter

public class Page {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private  Long id;
    private  String title;
    private  String description;
    private  String slug;
    private  String menuLabel;
    private  String h1;
    private  String content;
    private  Timestamp publishedAt;
    private  Integer priority;

    public Page() {
    }

    public Page(String title, String description, String slug, String menuLabel, String h1, String content, Timestamp publishedAt, Integer priority) {
        this.title = title;
        this.description = description;
        this.slug = slug;
        this.menuLabel = menuLabel;
        this.h1 = h1;
        this.content = content;
        this.publishedAt = publishedAt;
        this.priority = priority;
    }


    @Override
    public String toString() {
        return "Page{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", slug='" + slug + '\'' +
                ", menu_label='" + menuLabel + '\'' +
                ", h1='" + h1 + '\'' +
                ", content='" + content + '\'' +
                ", published_at=" + publishedAt +
                ", priority=" + priority +
                '}';
    }
}
