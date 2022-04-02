package ru.poezdizm.timelinetracker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class NetworkModel {

    private List<CharacterModel> characterModels;

    private List<RelationModel> relationModels;

}
