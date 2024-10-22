package com.wasin.wasin.domain.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ProfileDTO {

    private Long index;
    private String title;
    private String description;
    private String tip;
    private String ssh;

    public ProfileDTO(Long index, String title, String description, String tip, String ssh) {
        this.index = index;
        this.title = title;
        this.description = description;
        this.tip = tip;
        this.ssh = ssh;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfileDTO dto = (ProfileDTO) o;
        return Objects.equals(index, dto.index) && Objects.equals(title, dto.title)
                && Objects.equals(description, dto.description) && Objects.equals(tip, dto.tip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, title, description, tip);
    }
}
