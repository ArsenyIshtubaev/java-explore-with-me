package ru.practicum.ewm.common.model;

import lombok.*;
import ru.practicum.ewm.common.enums.State;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "requests", schema = "public")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @OneToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State status;
    @Column(nullable = false)
    private LocalDateTime created;

}
