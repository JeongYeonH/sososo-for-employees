package com.han_batang.back.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="club_info")
@Table(name="club_info")
public class ClubInfoEntity {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "club_info_id")
    private Integer clubInfoId;

    @Column(name = "club_type")
    private String clubType;

    @Column(name = "club_location")
    private String clubLocation;

    @Column(name = "club_total_num")
    private Integer clubTotalNum;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "club_id")
    @JsonIgnore
    ClubEntity clubEntity;
}
