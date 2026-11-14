package ru.ifmo.se.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.ifmo.se.persistence.converter.ZonedDateTimeConverter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "import_operations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ImportStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "added_objects_count")
    private Integer addedObjectsCount;

    @Convert(converter = ZonedDateTimeConverter.class)
    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMPTZ DEFAULT now()")
    private ZonedDateTime creationDate;
}
