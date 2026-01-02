package com.zgqf.house.dto;

import com.zgqf.house.entity.House;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class HouseFormDTO extends House {
    private List<String> picturePaths;
}
