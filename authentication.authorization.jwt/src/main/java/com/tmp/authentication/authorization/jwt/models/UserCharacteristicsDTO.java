package com.tmp.authentication.authorization.jwt.models;

import com.tmp.authentication.authorization.jwt.models.enums.RoleValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCharacteristicsDTO {

    private String username;
    private List<RoleValue> roleValues;
}
